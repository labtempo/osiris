/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.controller;

import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.common.RequestMethod;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.StatusCode;
import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.omcp.common.exceptions.MethodNotAllowedException;
import br.uff.labtempo.omcp.common.exceptions.NotFoundException;
import br.uff.labtempo.omcp.common.exceptions.NotImplementedException;
import br.uff.labtempo.omcp.common.utils.ResponseBuilder;
import br.uff.labtempo.osiris.collector.temp.Collector;
import br.uff.labtempo.osiris.collector.temp.Network;
import br.uff.labtempo.osiris.collector.temp.Sample;
import br.uff.labtempo.osiris.collector.temp.Sensor;
import br.uff.labtempo.osiris.omcp.Controller;
import br.uff.labtempo.osiris.omcp.Notification;
import br.uff.labtempo.osiris.sensornet.model.CollectorWrapper;
import br.uff.labtempo.osiris.sensornet.model.NetworkWrapper;
import br.uff.labtempo.osiris.sensornet.model.SensorWrapper;
import br.uff.labtempo.osiris.sensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.sensornet.persistence.memory.CollectorDao;
import br.uff.labtempo.osiris.sensornet.persistence.memory.NetworkDao;
import br.uff.labtempo.osiris.sensornet.persistence.memory.SensorDao;

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
                Sample sample = notice.getData(Sample.class);
                insert(sample);
                return new ResponseBuilder().buildNull();
            }
        }
        return null;
    }

    public void insert(Sample sample) {
        Network network = sample.getNetwork();
        Collector collector = sample.getCollector();
        Sensor sensor = sample.getSensor();

        NetworkDao ndao = new NetworkDao();
        CollectorDao cdao = new CollectorDao();
        SensorDao sdao = new SensorDao();

        NetworkWrapper nw = ndao.getById(network.getId());
        if (nw == null) {
            nw = new NetworkWrapper(network);
            ndao.insert(nw);
        } else {
            ndao.update(nw, new NetworkWrapper(network));
        }

        CollectorWrapper cw = cdao.getById(collector.getId(), nw);
        if (cw == null) {
            cw = new CollectorWrapper(collector);
            cdao.insert(cw, nw);
        } else {
            cdao.update(cw, new CollectorWrapper(collector));
        }

        SensorWrapper sw = sdao.getById(sensor.getId(), nw, cw);
        if (sw == null) {
            sw = new SensorWrapper(sensor);
            sdao.insert(sw, nw, cw);
        } else {
            sdao.update(sw, new SensorWrapper(sensor));
        }

    }

}
