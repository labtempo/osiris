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
import br.uff.labtempo.osiris.to.collector.SampleCoTo;
import br.uff.labtempo.osiris.to.sensornet.SensorSnTo;
import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensorLink;
import br.uff.labtempo.osiris.virtualsensornet.model.state.ModelState;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.virtualsensornet.model.util.SensorCoWrapper;
import br.uff.labtempo.osiris.virtualsensornet.persistence.AnnouncerDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.LinkDao;
import java.util.List;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class NotifyController extends Controller {

    private final String UNIQUE_SENSOR = ControllerPath.MODULE_SENSORNET.toString() + ControllerPath.SENSOR_BY_ID;
    private final DaoFactory factory;

    public NotifyController(DaoFactory factory) {
        this.factory = factory;
    }

    @Override
    public Response process(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException {

        if (request.getMethod() == RequestMethod.NOTIFY) {
            if (request.getModule().contains(ControllerPath.COLLECTOR_MESSAGEGROUP.toString())) {
                SampleCoTo sample = request.getContent(SampleCoTo.class);
                analyzeCollector(sample);
            }

            if (request.getModule().contains(ControllerPath.UPDATE_MESSAGEGROUP.toString())) {
                if (match(request.getResource(), UNIQUE_SENSOR)) {
                    SensorSnTo sensor = request.getContent(SensorSnTo.class);
                    analyzeSensorNet(sensor);
                }
            }
            return new ResponseBuilder().buildNull();
        }
        return null;
    }

    private void analyzeCollector(SampleCoTo sample) {
        SensorCoWrapper wrapper = new SensorCoWrapper(sample);
        updateLink(wrapper);
    }

    private void analyzeSensorNet(SensorSnTo sensor) {
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
        AnnouncerDao announcerDao = factory.getAnnouncerDao();

        String sensorId = sensor.getId();
        String networkId = sensor.getNetworkId();
        String collectorId = sensor.getCollectorId();

        List<VirtualSensorLink> links = lDao.getAll(networkId, collectorId, sensorId);

        for (VirtualSensorLink link : links) {
            if (link != null) {
                if (ModelState.INACTIVE.equals(modelState)) {
                    link.deactivate();
                    lDao.update(link);
                    announcerDao.notifyDeactivation(link.getTransferObject());
                    announcerDao.broadcastIt(link.getTransferObject());
                }
                if (ModelState.MALFUNCTION.equals(modelState)) {
                    link.malfunction();
                    lDao.update(link);
                    announcerDao.notifyDeactivation(link.getTransferObject());
                    announcerDao.broadcastIt(link.getTransferObject());
                }
            }
        }
    }

    private void updateLink(SensorCoWrapper wrapper) {
        LinkDao lDao = factory.getLinkDao();
        AnnouncerDao announcerDao = factory.getAnnouncerDao();

        String sensorId = wrapper.getSensorId();
        String networkId = wrapper.getNetworkId();
        String collectorId = wrapper.getCollectorId();

        List<VirtualSensorLink> links = lDao.getAll(networkId, collectorId, sensorId);

        for (VirtualSensorLink link : links) {
            if (link != null) {
                link.update(wrapper);
                lDao.update(link);
                if (ModelState.REACTIVATED.equals(link.getModelState())) {
                    announcerDao.notifyReactivation(link.getTransferObject());
                }
                announcerDao.broadcastIt(link.getTransferObject());
            }
        }
    }

}
