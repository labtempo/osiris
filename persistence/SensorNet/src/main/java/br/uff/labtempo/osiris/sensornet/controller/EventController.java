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
import br.uff.labtempo.omcp.common.utils.ResponseBuilder;
import br.uff.labtempo.osiris.collector.to.CollectorCoTo;
import br.uff.labtempo.osiris.collector.to.NetworkCoTo;
import br.uff.labtempo.osiris.collector.to.SampleCoTo;
import br.uff.labtempo.osiris.collector.to.SensorCoTo;
import br.uff.labtempo.osiris.omcp.Controller;
import br.uff.labtempo.osiris.omcp.Notification;
import br.uff.labtempo.osiris.sensornet.model.jpa.Collector;
import br.uff.labtempo.osiris.sensornet.model.jpa.Network;
import br.uff.labtempo.osiris.sensornet.model.jpa.Sensor;
import br.uff.labtempo.osiris.sensornet.persistence.CollectorDao;
import br.uff.labtempo.osiris.sensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.sensornet.persistence.NetworkDao;
import br.uff.labtempo.osiris.sensornet.persistence.SensorDao;

/**
 *
 * @author Felipe
 */
public class EventController extends Controller {

    private DaoFactory factory;

    public EventController(DaoFactory factory) {
        this.factory = factory;
    }

    @Override
    public Response process(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException {
        if (request.getMethod() == RequestMethod.NOTIFY) {
            if (request.getResource().contains(ControllerPath.COLLECTOR_EXCHANGE.toString())) {
                Notification notice = super.fromJson(request.getContent(), Notification.class);
                SampleCoTo sample = notice.getData(SampleCoTo.class);
                insert(sample);
                return new ResponseBuilder().buildNull();
            }
        }
        return null;
    }

    public void insert(SampleCoTo sample) {
        NetworkCoTo networkTo = sample.getNetwork();
        CollectorCoTo collectorTo = sample.getCollector();
        SensorCoTo sensorTo = sample.getSensor();

        Network nw = getNetwok(networkTo.getId());
        Collector cw = getCollector(networkTo.getId(), collectorTo.getId());
        Sensor sw = getSensor(networkTo.getId(), collectorTo.getId(), sensorTo.getId());

        if (nw == null) {
            insertNetwork(networkTo, collectorTo, sensorTo);
        } else if (cw == null) {
            nw.update(networkTo);
            insertCollector(nw, collectorTo, sensorTo);
        } else if (sw == null) {
            nw.update(networkTo);
            cw.update(collectorTo);
            insertSensor(nw, cw, sensorTo);
        } else {
            nw.update(networkTo);
            cw.update(collectorTo);
            sw.update(sensorTo);
            applyUpdateToAll(nw, cw, sw);
        }
    }

    private Sensor getSensor(String networkId, String collectorId, String sensorId) {
        SensorDao<Sensor> sdao = factory.getSensorDao();
        try {
            Sensor sensor = sdao.get(networkId, collectorId, sensorId);
            return sensor;
        } catch (Exception ex) {
            return null;
        }
    }

    private Collector getCollector(String networkId, String collectorId) {
        CollectorDao<Collector> cdao = factory.getCollectorDao();
        try {
            Collector collector = cdao.get(networkId, collectorId);
            return collector;
        } catch (Exception ex) {
            return null;
        }
    }

    private Network getNetwok(String networkId) {
        NetworkDao<Network> ndao = factory.getNetworkDao();
        try {
            Network network = ndao.get(networkId);
            return network;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * POST /
     */
    private void insertNetwork(NetworkCoTo networkTo, CollectorCoTo collectorTo, SensorCoTo sensorTo) {
        NetworkDao<Network> ndao = factory.getNetworkDao();
        SensorDao<Sensor> sdao = factory.getSensorDao();
        CollectorDao<Collector> cdao = factory.getCollectorDao();

        Network nw = Network.build(networkTo);
        Collector cw = Collector.build(collectorTo);
        Sensor sw = Sensor.generate(sensorTo);

        nw.addCollector(cw);
        cw.addSensor(sw);

        ndao.save(nw);
//        cdao.save(cw);
//        sdao.save(sw);
    }

    /**
     * POST /:networkid/collectors/
     *
     * PUT /:networkid/
     */
    private void insertCollector(Network nw, CollectorCoTo collectorTo, SensorCoTo sensorTo) {
        NetworkDao<Network> ndao = factory.getNetworkDao();
        SensorDao<Sensor> sdao = factory.getSensorDao();
        CollectorDao<Collector> cdao = factory.getCollectorDao();

        Collector cw = Collector.build(collectorTo);
        Sensor sw = Sensor.generate(sensorTo);

        nw.addCollector(cw);
        cw.addSensor(sw);

        ndao.update(nw);
//        cdao.save(cw);
//        sdao.save(sw);
    }

    /**
     * POST /:networkid/collectors/:collectorid/sensors/
     *
     * PUT /:networkid/collectors/:collectorid/
     * 
     * PUT /:networkid/
     */
    private void insertSensor(Network nw, Collector cw, SensorCoTo sensorTo) {
        NetworkDao<Network> ndao = factory.getNetworkDao();
        SensorDao<Sensor> sdao = factory.getSensorDao();
        CollectorDao<Collector> cdao = factory.getCollectorDao();

        Sensor sw = Sensor.generate(sensorTo);

        nw.addSensor(sw);
        cw.addSensor(sw);
        ndao.update(nw);
//        cdao.update(cw);
//        sdao.save(sw);
    }

    /**
     * PUT /:networkid/collectors/:collectorid/sensors/:sensorid
     *
     * PUT /:networkid/collectors/:collectorid/
     *
     * PUT /:networkid/
     */
    private void applyUpdateToAll(Network nw, Collector cw, Sensor sw) {
        NetworkDao<Network> ndao = factory.getNetworkDao();
        SensorDao<Sensor> sdao = factory.getSensorDao();
        CollectorDao<Collector> cdao = factory.getCollectorDao();

        ndao.update(nw);
//        cdao.update(cw);
//        sdao.update(sw);
    }

}
