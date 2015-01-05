/* 
 * Copyright 2015 Felipe Santos <fralph at ic.uff.br>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.uff.labtempo.osiris.sensornet.controller;

import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.common.RequestMethod;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.omcp.common.exceptions.MethodNotAllowedException;
import br.uff.labtempo.omcp.common.exceptions.NotFoundException;
import br.uff.labtempo.omcp.common.exceptions.NotImplementedException;
import br.uff.labtempo.omcp.common.utils.ResponseBuilder;
import br.uff.labtempo.osiris.omcp.Controller;
import br.uff.labtempo.osiris.sensornet.model.jpa.Collector;
import br.uff.labtempo.osiris.sensornet.model.jpa.Network;
import br.uff.labtempo.osiris.sensornet.model.jpa.Sensor;
import br.uff.labtempo.osiris.sensornet.model.state.ModelState;
import br.uff.labtempo.osiris.sensornet.model.util.ConsumableInfo;
import br.uff.labtempo.osiris.sensornet.persistence.AnnouncerDao;
import br.uff.labtempo.osiris.sensornet.persistence.CollectorDao;
import br.uff.labtempo.osiris.sensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.sensornet.persistence.NetworkDao;
import br.uff.labtempo.osiris.sensornet.persistence.SchedulerDao;
import br.uff.labtempo.osiris.sensornet.persistence.SensorDao;
import br.uff.labtempo.osiris.to.collector.CollectorCoTo;
import br.uff.labtempo.osiris.to.collector.NetworkCoTo;
import br.uff.labtempo.osiris.to.collector.SampleCoTo;
import br.uff.labtempo.osiris.to.collector.SensorCoTo;
import br.uff.labtempo.osiris.to.sensornet.CollectorSnTo;
import br.uff.labtempo.osiris.to.sensornet.NetworkSnTo;
import br.uff.labtempo.osiris.to.sensornet.SensorSnTo;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class NotifyController extends Controller {

    private final DaoFactory factory;

    public NotifyController(DaoFactory factory) {
        this.factory = factory;
    }

    @Override
    public Response process(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException {
        if (request.getMethod() == RequestMethod.NOTIFY) {
            if (request.getModule().contains(ControllerPath.COLLECTOR_MESSAGEGROUP.toString())) {
                SampleCoTo sample = request.getContent(SampleCoTo.class);
                analyze(sample);
            }
            return new ResponseBuilder().buildNull();
        }
        return null;
    }

    private void analyze(SampleCoTo sample) {
        NetworkCoTo networkTo = sample.getNetwork();
        CollectorCoTo collectorTo = sample.getCollector();
        SensorCoTo sensorTo = sample.getSensor();
        postProcess(sensorTo, collectorTo, networkTo);
    }

    private void postProcess(SensorCoTo sensorTo, CollectorCoTo collectorTo, NetworkCoTo networkTo) {
        List<Command> commands = new ArrayList<>();

        /*pre processing - operate/binding*/
        Network network = operate(networkTo, commands);
        Collector collector = operate(collectorTo, network, commands);
        Sensor sensor = operate(sensorTo, network, collector, commands);

        /*persist on db*/
        persist(network);

        /*post processing - execute commands */
        executeCommands(commands);

        /*schedule*/
        schedule(sensor);

    }

    private Sensor operate(SensorCoTo sensorTo, final Network network, final Collector collector, List<Command> commands) {
        SensorDao sdao = factory.getSensorDao();
        boolean isModified;
        Sensor sensor = sdao.get(network.getId(), collector.getId(), sensorTo.getId());

        if (sensor != null) {
            isModified = sensor.update(sensorTo);
        } else {
            sensor = Sensor.build(sensorTo);
            network.addSensor(sensor);
            collector.addSensor(sensor);
            isModified = true;
        }

        if (isModified) {
            //check collector and network reactivation            
            switch (sensor.getModelState()) {
                case REACTIVATED:
                    if (collector.getModelState().equals(ModelState.INACTIVE)) {
                        collector.reactivate();
                        if (network.getModelState().equals(ModelState.INACTIVE)) {
                            network.reactivate();
                        }
                    }
                    break;
            }

            final Sensor sensorCopy = sensor;
            Command command = new Command() {
                @Override
                public void execute() {
                    AnnouncerDao announcer = getAnnouncerDao();
                    SensorSnTo to = sensorCopy.getTransferObject();

                    switch (sensorCopy.getModelState()) {
                        case NEW:
                            announcer.notifyNew(to);
                            break;
                        case REACTIVATED:
                            announcer.notifyReactivation(to);
                            break;
                    }

                    List<ConsumableInfo> infosCopy = sensorCopy.checkConsumables();
                    if (infosCopy != null && infosCopy.size() > 0) {
                        announcer.notifyBrokenConsumableRule(infosCopy, to);
                    }

                    announcer.broadcastIt(to);

                }
            };
            commands.add(command);
        }
        return sensor;
    }

    private Collector operate(CollectorCoTo collectorTo, Network network, List<Command> commands) {
        CollectorDao cdao = factory.getCollectorDao();
        boolean isModified;
        Collector collector = cdao.get(network.getId(), collectorTo.getId());
        if (collector != null) {
            isModified = collector.update(collectorTo);
            if (collector.getModelState().equals(ModelState.INACTIVE)) {
                isModified = true;
            }
        } else {
            collector = Collector.build(collectorTo);
            network.addCollector(collector);
            isModified = true;
        }

        if (isModified) {
            final Collector collectorCopy = collector;
            Command command = new Command() {
                @Override
                public void execute() {
                    AnnouncerDao announcer = getAnnouncerDao();
                    CollectorSnTo to = collectorCopy.getTransferObject();
                    switch (collectorCopy.getModelState()) {
                        case NEW:
                            announcer.notifyNew(to);
                            break;
                        case REACTIVATED:
                            announcer.notifyReactivation(to);
                            break;
                    }
                    announcer.broadcastIt(to);
                }
            };
            commands.add(command);
        }
        return collector;
    }

    private Network operate(NetworkCoTo networkTo, List<Command> commands) {
        NetworkDao ndao = factory.getNetworkDao();
        boolean isModified;
        Network network = ndao.get(networkTo.getId());
        if (network != null) {
            isModified = network.update(networkTo);
            if (network.getModelState().equals(ModelState.INACTIVE)) {
                isModified = true;
            }
        } else {
            network = Network.build(networkTo);
            isModified = true;
        }

        if (isModified) {
            final Network networkCopy = network;
            Command command = new Command() {
                @Override
                public void execute() {
                    AnnouncerDao announcer = getAnnouncerDao();
                    NetworkSnTo to = networkCopy.getTransferObject();
                    switch (networkCopy.getModelState()) {
                        case NEW:
                            announcer.notifyNew(to);
                            break;
                        case REACTIVATED:
                            announcer.notifyReactivation(to);
                            break;
                    }
                    announcer.broadcastIt(to);
                }
            };
            commands.add(command);
        }
        return network;
    }

    private void persist(Network network) {
        NetworkDao ndao = factory.getNetworkDao();
        ndao.save(network);
    }

    private AnnouncerDao getAnnouncerDao() {
        return factory.getAnnouncerDao();
    }

    private void executeCommands(List<Command> commands) {
        for (Command command : commands) {
            command.execute();
        }
    }

    private void schedule(Sensor sensor) {
        SchedulerDao sdao = factory.getSchedulerDao();
        sdao.schedule(sensor);
    }

    private interface Command {

        void execute();
    }
}
