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
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.omcp.common.exceptions.MethodNotAllowedException;
import br.uff.labtempo.omcp.common.exceptions.NotFoundException;
import br.uff.labtempo.omcp.common.exceptions.NotImplementedException;
import br.uff.labtempo.omcp.common.utils.ResponseBuilder;
import br.uff.labtempo.osiris.omcp.Controller;
import br.uff.labtempo.osiris.sensornet.model.Collector;
import br.uff.labtempo.osiris.sensornet.model.Network;
import br.uff.labtempo.osiris.sensornet.model.Sensor;
import br.uff.labtempo.osiris.sensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.sensornet.persistence.NetworkDao;
import br.uff.labtempo.osiris.sensornet.persistence.SensorDao;
import br.uff.labtempo.osiris.to.common.definitions.Path;
import br.uff.labtempo.osiris.to.sensornet.SensorSnTo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class SensorController extends Controller {

    private final String ALL_OF_NETWORK = Path.RESOURCE_SENSORNET_NETWORK_SENSOR_All.toString();
    private final String ALL_OF_COLLECTOR = Path.RESOURCE_SENSORNET_SENSOR_All.toString();
    private final String UNIQUE = Path.RESOURCE_SENSORNET_SENSOR_BY_ID.toString();

    private DaoFactory factory;

    public SensorController(DaoFactory factory) {
        this.factory = factory;
    }

    @Override
    public Response process(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException {
        try {
            return routing(request);
        } finally {
            factory.clear();
        }
    }

    public Response routing(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException {
        String contentType = request.getContentType();
        String networkId = null;
        String collectorId = null;
        String sensorId = null;
        //One sensor
        if (match(request.getResource(), UNIQUE)) {
            Map<String, String> map = extractParams(request.getResource(), UNIQUE);
            networkId = map.get(Path.ID1.toString());
            collectorId = map.get(Path.ID2.toString());
            sensorId = map.get(Path.ID3.toString());
            switch (request.getMethod()) {
                case GET:
                    SensorSnTo to = getById(networkId, collectorId, sensorId);
                    Response response = new ResponseBuilder().ok(to, contentType).build();
                    return response;
                case PUT:
                    update();
                case DELETE:
                    delete(networkId, collectorId, sensorId);
                    response = new ResponseBuilder().ok().build();
                    return response;
                default:
                    throw new MethodNotAllowedException("Action not allowed for this resource!");
            }
        }
        //All of Collector
        if (match(request.getResource(), ALL_OF_COLLECTOR)) {
            Map<String, String> map = extractParams(request.getResource(), ALL_OF_COLLECTOR);
            networkId = map.get(Path.ID1.toString());
            collectorId = map.get(Path.ID2.toString());

            switch (request.getMethod()) {
                case GET:
                    List<SensorSnTo> to = getAll(networkId, collectorId);
                    Response response = new ResponseBuilder().ok(to, contentType).build();
                    return response;
                case POST:
                    create();
                default:
                    throw new MethodNotAllowedException("Action not allowed for this resource!");
            }
        } else if (match(request.getResource(), ALL_OF_NETWORK)) {
            Map<String, String> map = extractParams(request.getResource(), ALL_OF_NETWORK);
            networkId = map.get(Path.ID1.toString());

            switch (request.getMethod()) {
                case GET:
                    List<SensorSnTo> to = getAllInNetwork(networkId);
                    Response response = new ResponseBuilder().ok(to, contentType).build();
                    return response;
                case POST:
                    create();
                default:
                    throw new MethodNotAllowedException("Action not allowed for this resource!");
            }
        }
        return null;
    }

    public List<SensorSnTo> getAllInNetwork(String networkId) throws NotFoundException, InternalServerErrorException {
        SensorDao sdao;
        try {
            sdao = factory.getSensorDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }
        List<Sensor> swlist = sdao.getAll(networkId);
        List<SensorSnTo> sensors = new ArrayList<>();
        for (Sensor sw : swlist) {
            sensors.add(sw.getTransferObject());
        }
        return sensors;
    }

    public List<SensorSnTo> getAll(String networkId, String collectorId) throws NotFoundException, InternalServerErrorException {
        SensorDao sdao;
        try {
            sdao = factory.getSensorDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }
        List<Sensor> swlist = sdao.getAll(networkId, collectorId);
        List<SensorSnTo> sensors = new ArrayList<>();
        for (Sensor sw : swlist) {
            sensors.add(sw.getTransferObject());
        }
        return sensors;
    }

    public SensorSnTo getById(String networkId, String collectorId, String sensorId) throws NotFoundException, InternalServerErrorException {
        SensorDao sdao;
        try {
            sdao = factory.getSensorDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }
        Sensor sw = sdao.get(networkId, collectorId, sensorId);
        if (sw == null) {
            throw new NotFoundException("Sensor not exists");
        }
        return sw.getTransferObject();
    }

    public boolean delete(String networkId, String collectorId, String sensorId) throws InternalServerErrorException, NotFoundException {
        SensorDao sdao;
        try {
            sdao = factory.getSensorDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }
        Sensor sensor = sdao.get(networkId, collectorId, sensorId);
        if (sensor == null) {
            throw new NotFoundException("Sensor not exists");
        }
        Collector collector = sensor.getCollector();
        Network network = sensor.getNetwork();
        //remove from collector->network and set null to both        
        network.removeSensor(sensor);
        collector.removeSensor(sensor);

        try {
            //sdao.update(sensor);
            sdao.delete(sensor);
            //NetworkDao networkDao = factory.getNetworkDao();
            //networkDao.update(network);
            
            return true;
        } catch (Exception e) {
            throw new InternalServerErrorException("Sensor couldn't removed!");
        }
    }

    public long create() throws MethodNotAllowedException {
        throw new MethodNotAllowedException("A Sensor cannot be created directly!");
    }

    public boolean update() throws MethodNotAllowedException {
        throw new MethodNotAllowedException("A Sensor is a read-only resource!");
    }
}
