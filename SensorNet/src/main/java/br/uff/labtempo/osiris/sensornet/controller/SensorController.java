/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.controller;

import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.common.RequestMethod;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.omcp.common.exceptions.MethodNotAllowedException;
import br.uff.labtempo.omcp.common.exceptions.NotFoundException;
import br.uff.labtempo.omcp.common.exceptions.NotImplementedException;
import br.uff.labtempo.osiris.omcp.Controller;
import br.uff.labtempo.osiris.sensornet.model.jpa.Sensor;
import br.uff.labtempo.osiris.sensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.sensornet.persistence.SensorDao;
import br.uff.labtempo.osiris.to.sensornet.SensorSnTo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public class SensorController extends Controller {

    private final String ALL_OF_NETWORK = ControllerPath.SENSOR_ALL_BY_NETWORK.toString();
    private final String ALL_OF_COLLECTOR = ControllerPath.SENSOR_ALL_BY_COLLECTOR.toString();
    private final String UNIQUE = ControllerPath.SENSOR_BY_ID.toString();

    private DaoFactory factory;

    public SensorController(DaoFactory factory) {
        this.factory = factory;
    }

    @Override
    public Response process(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException {
        String contentType = request.getContentType();
        String networkId = null;
        String collectorId = null;
        String sensorId = null;
        //One sensor
        if (match(request.getResource(), UNIQUE)) {
            Map<String, String> map = extract(request.getResource(), UNIQUE);
            networkId = map.get(ControllerPath.NETWORK_KEY.toString());
            collectorId = map.get(ControllerPath.COLLECTOR_KEY.toString());
            sensorId = map.get(ControllerPath.SENSOR_KEY.toString());

            if (request.getMethod() != RequestMethod.GET) {
                throw new NotImplementedException("Action not implemented");
            }
            return builder(getById(networkId, collectorId, sensorId), contentType);
        }
        //All of Collector
        if (match(request.getResource(), ALL_OF_COLLECTOR)) {
            Map<String, String> map = extract(request.getResource(), ALL_OF_COLLECTOR);
            networkId = map.get(ControllerPath.NETWORK_KEY.toString());
            collectorId = map.get(ControllerPath.COLLECTOR_KEY.toString());

            if (request.getMethod() != RequestMethod.GET) {
                throw new NotImplementedException("Action not implemented");
            }
            return builder(getAll(networkId, collectorId), contentType);
        }
        //All of Network
        if (match(request.getResource(), ALL_OF_NETWORK)) {
            Map<String, String> map = extract(request.getResource(), ALL_OF_NETWORK);
            networkId = map.get(ControllerPath.NETWORK_KEY.toString());

            if (request.getMethod() != RequestMethod.GET) {
                throw new NotImplementedException("Action not implemented");
            }
            return builder(getAllOfNetwork(networkId), contentType);
        }

        return null;
    }

    private List<SensorSnTo> getAllOfNetwork(String networkId) throws NotFoundException {
//        NetworkDao<Network> ndao = factory.getNetworkDao();
        SensorDao sdao = factory.getSensorDao();

//        Network nw = ndao.get(networkId);
//
//        if (nw == null) {
//            throw new NotFoundException("Network not exists");
//        }

        List<Sensor> swlist = sdao.getAll(networkId);

        List<SensorSnTo> sensors = new ArrayList<>();
        for (Sensor sw : swlist) {
            sensors.add(sw.getTransferObject());
        }

        return sensors;
    }

    private List<SensorSnTo> getAll(String networkId, String collectorId) throws NotFoundException {
//        NetworkDao<Network> ndao = factory.getNetworkDao();
//        CollectorDao<Collector> cdao = factory.getCollectorDao();
        SensorDao sdao = factory.getSensorDao();

//        Network nw = ndao.get(networkId);
//
//        if (nw == null) {
//            throw new NotFoundException("Network not exists");
//        }

        //Collector cw = cdao.get(networkId, collectorId);

        //if (cw == null) {
            //throw new NotFoundException("Collector not exists");
        //}

        List<Sensor> swlist = sdao.getAll(networkId, collectorId);

        List<SensorSnTo> sensors = new ArrayList<>();
        for (Sensor sw : swlist) {
            sensors.add(sw.getTransferObject());
        }

        return sensors;
    }

    private SensorSnTo getById(String networkId, String collectorId, String sensorId) throws NotFoundException {
//        NetworkDao<Network> ndao = factory.getNetworkDao();
//        CollectorDao<Collector> cdao = factory.getCollectorDao();
        SensorDao sdao = factory.getSensorDao();

//        Network nw = ndao.get(networkId);
//
//        if (nw == null) {
//            throw new NotFoundException("Network not exists");
//        }
//
//        Collector cw = cdao.get(networkId, collectorId);
//
//        if (cw == null) {
//            throw new NotFoundException("Collector not exists");
//        }

        Sensor sw = sdao.get(networkId, collectorId, sensorId);

//        if (sw == null) {
//            throw new NotFoundException("Sensor not exists");
//        }

        if (sw != null) {
            return sw.getTransferObject();
        }
        return null;
    }

}