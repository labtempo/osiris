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
import br.uff.labtempo.omcp.common.exceptions.BadRequestException;
import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.omcp.common.exceptions.MethodNotAllowedException;
import br.uff.labtempo.omcp.common.exceptions.NotFoundException;
import br.uff.labtempo.omcp.common.exceptions.NotImplementedException;
import br.uff.labtempo.omcp.common.utils.ResponseBuilder;
import br.uff.labtempo.osiris.omcp.Controller;
import br.uff.labtempo.osiris.utils.persistence.jpa.batch.BatchPersistence;
import br.uff.labtempo.osiris.utils.requestpool.RequestHandler;
import br.uff.labtempo.osiris.utils.requestpool.RequestPool;
import br.uff.labtempo.osiris.sensornet.model.Collector;
import br.uff.labtempo.osiris.sensornet.model.Network;
import br.uff.labtempo.osiris.sensornet.model.Sensor;
import br.uff.labtempo.osiris.sensornet.model.state.ModelState;
import br.uff.labtempo.osiris.sensornet.controller.util.AnnouncerWrapper;
import br.uff.labtempo.osiris.sensornet.model.util.ConsumableInfo;
import br.uff.labtempo.osiris.sensornet.controller.util.SchedulerAgentWrapper;
import br.uff.labtempo.osiris.sensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.sensornet.thirdparty.announcer.AnnouncerAgent;
import br.uff.labtempo.osiris.sensornet.thirdparty.scheduler.SchedulerAgent;
import br.uff.labtempo.osiris.to.collector.CollectorCoTo;
import br.uff.labtempo.osiris.to.collector.NetworkCoTo;
import br.uff.labtempo.osiris.to.collector.SampleCoTo;
import br.uff.labtempo.osiris.to.collector.SensorCoTo;
import br.uff.labtempo.osiris.to.common.definitions.Path;
import br.uff.labtempo.osiris.to.sensornet.CollectorSnTo;
import br.uff.labtempo.osiris.to.sensornet.NetworkSnTo;
import br.uff.labtempo.osiris.to.sensornet.SensorSnTo;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class NotifyController extends Controller implements RequestHandler {

    private final BatchPersistence persistence;
    private AnnouncerAgent announcer;
    private SchedulerAgent scheduler;
    private RequestPool requestPool;

    public NotifyController(DaoFactory factory, AnnouncerAgent announcer, SchedulerAgent scheduler, RequestPool requestPool) {
        this.persistence = factory.getBatchPersistence();
        this.announcer = new AnnouncerWrapper(announcer);
        this.scheduler = new SchedulerAgentWrapper(scheduler);
        this.requestPool = requestPool;
        if (requestPool != null) {
            requestPool.setHandler(this);
        }
    }

    public NotifyController(DaoFactory factory, RequestPool requestPool) {
        this(factory, null, null, requestPool);
    }

    public NotifyController(DaoFactory factory) {
        this(factory, null, null, null);
    }

    @Override
    public Response process(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException, BadRequestException {
        if (request.getMethod() == RequestMethod.NOTIFY) {
            if (request.getModule().contains(Path.NAMING_MESSAGEGROUP_COLLECTOR.toString())) {
                if (requestPool != null) {
                    requestPool.add(request);
                } else {
                    handle(request);
                }
            }
            return new ResponseBuilder().buildNull();
        }
        return null;
    }

    @Override
    public void handle(Request request) throws BadRequestException {
        SampleCoTo sample = request.getContent(SampleCoTo.class);
        analyzeSample(sample);
    }

    public void analyzeSample(SampleCoTo sample) {
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

    private Network operate(NetworkCoTo networkTo, List<Command> commands) {
        boolean isModified;
        Network network = persistence.get(Network.class, networkTo.getId());
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
                    AnnouncerAgent announcer = getAnnouncerAgent();
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

    private Collector operate(CollectorCoTo collectorTo, Network network, List<Command> commands) {
        //TODO: resolver problama do DAO inconsistente - contornar o problema, um loop é usado
        boolean isModified;
        Collector collector = network.getCollectorsByName(collectorTo.getId());
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
                    AnnouncerAgent announcer = getAnnouncerAgent();
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

    private Sensor operate(SensorCoTo sensorTo, final Network network, final Collector collector, List<Command> commands) {
        boolean isModified;
        //TODO: resolver problama do DAO inconsistente - contornar o problema, um loop é usado
        Sensor sensor = collector.getSensorsByName(sensorTo.getId());

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
                    AnnouncerAgent announcer = getAnnouncerAgent();
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

    private void persist(Network network) {
        try {
            persistence.save(network);
        } catch (Exception ex) {
            persistence.update(network);
        }
    }

    private void executeCommands(List<Command> commands) {
        for (Command command : commands) {
            command.execute();
        }
    }

    private interface Command {

        void execute();
    }

    private AnnouncerAgent getAnnouncerAgent() {
        return announcer;
    }

    private void schedule(Sensor sensor) {
        scheduler.schedule(sensor);
    }

    public void setAnnouncerAgent(AnnouncerAgent announcer) {
        this.announcer = announcer;
    }

    public void setSchedulerAgent(SchedulerAgent scheduler) {
        this.scheduler = scheduler;
    }
}
