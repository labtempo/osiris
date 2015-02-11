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
package br.uff.labtempo.osiris.virtualsensornet.controller;

import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.common.RequestMethod;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.omcp.common.exceptions.MethodNotAllowedException;
import br.uff.labtempo.omcp.common.exceptions.NotFoundException;
import br.uff.labtempo.omcp.common.exceptions.NotImplementedException;
import br.uff.labtempo.omcp.common.utils.ResponseBuilder;
import br.uff.labtempo.osiris.omcp.Controller;
import br.uff.labtempo.osiris.utils.scheduling.Scheduler;
import br.uff.labtempo.osiris.to.collector.SampleCoTo;
import br.uff.labtempo.osiris.to.common.definitions.Path;
import br.uff.labtempo.osiris.to.sensornet.SensorSnTo;
import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensorLink;
import br.uff.labtempo.osiris.virtualsensornet.model.state.ModelState;
import br.uff.labtempo.osiris.virtualsensornet.model.util.AnnouncerWrapper;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.virtualsensornet.model.util.SensorCoToWrapper;
import br.uff.labtempo.osiris.virtualsensornet.thirdparty.announcer.AnnouncerAgent;
import br.uff.labtempo.osiris.virtualsensornet.persistence.LinkDao;
import java.util.List;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class NotifyController extends Controller {

    private final String UNIQUE_SENSOR = Path.SEPARATOR.toString() + Path.NAMING_MODULE_SENSORNET + Path.RESOURCE_SENSORNET_SENSOR_BY_ID;
    private final DaoFactory factory;
    private AnnouncerAgent announcer;
    private Scheduler scheduler;

    public NotifyController(DaoFactory factory, AnnouncerAgent announcer) {
        this.factory = factory;
        this.announcer = new AnnouncerWrapper(announcer);
    }

    public NotifyController(DaoFactory factory) {
        this(factory,null);
    }

    @Override
    public Response process(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException {

        if (request.getMethod() == RequestMethod.NOTIFY) {
            if (request.getModule().contains(Path.NAMING_MESSAGEGROUP_COLLECTOR.toString())) {
                SampleCoTo sample = request.getContent(SampleCoTo.class);
                updateValues(sample);
            }

            if (request.getModule().contains(Path.NAMING_MESSAGEGROUP_UPDATE.toString())) {
                if (match(request.getResource(), UNIQUE_SENSOR)) {
                    SensorSnTo sensor = request.getContent(SensorSnTo.class);
                    updateModelStatus(sensor);
                }
            }
            return new ResponseBuilder().buildNull();
        }
        return null;
    }

    public synchronized void updateValues(SampleCoTo sample) {
        SensorCoToWrapper wrapper = new SensorCoToWrapper(sample);
        updateLink(wrapper);
    }

    public synchronized void updateModelStatus(SensorSnTo sensor) {
        switch (sensor.getState()) {
            case INACTIVE:
                changeLinkModelState(sensor, ModelState.INACTIVE);
                break;
            case MALFUNCTION:
                changeLinkModelState(sensor, ModelState.MALFUNCTION);
                break;
        }
    }

    private void changeLinkModelState(SensorSnTo sensor, ModelState modelState) {
        LinkDao lDao = factory.getLinkDao();

        String sensorId = sensor.getId();
        String networkId = sensor.getNetworkId();
        String collectorId = sensor.getCollectorId();

        List<VirtualSensorLink> links = lDao.getAllByReferences(networkId, collectorId, sensorId);

        for (VirtualSensorLink link : links) {
            if (link != null) {
                if (ModelState.INACTIVE.equals(modelState)) {
                    link.deactivate();
                    lDao.update(link);
                    announcer.notifyDeactivation(link.getTransferObject());
                    announcer.broadcastIt(link.getTransferObject());
                }
                if (ModelState.MALFUNCTION.equals(modelState)) {
                    link.malfunction();
                    lDao.update(link);
                    announcer.notifyDeactivation(link.getTransferObject());
                    announcer.broadcastIt(link.getTransferObject());
                }
            }
        }
    }

    private void updateLink(SensorCoToWrapper wrapper) {
        LinkDao lDao = factory.getLinkDao();

        String sensorId = wrapper.getSensorId();
        String networkId = wrapper.getNetworkId();
        String collectorId = wrapper.getCollectorId();

        List<VirtualSensorLink> links = lDao.getAllByReferences(networkId, collectorId, sensorId);

        for (VirtualSensorLink link : links) {
            if (link != null) {
                link.updateVirtualSensorDataFromCollectorData(wrapper);
                lDao.save(link);
                if (ModelState.REACTIVATED.equals(link.getModelState())) {
                    announcer.notifyReactivation(link.getTransferObject());
                }
                announcer.broadcastIt(link.getTransferObject());
            }
        }
    }

    public void setAnnouncerAgent(AnnouncerAgent announcer) {
        this.announcer = announcer;
    }

    public void setSchedulerAgent(Scheduler scheduler) {
        this.scheduler = scheduler;
    }
}
