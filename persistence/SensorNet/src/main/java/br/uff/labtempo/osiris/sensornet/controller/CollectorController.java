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
import br.uff.labtempo.osiris.sensornet.model.jpa.Collector;
import br.uff.labtempo.osiris.sensornet.model.jpa.Network;
import br.uff.labtempo.osiris.sensornet.persistence.CollectorDao;
import br.uff.labtempo.osiris.sensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.sensornet.persistence.NetworkDao;
import br.uff.labtempo.osiris.sensornet.to.CollectorSnTo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public class CollectorController extends Controller {

    private final String ALL = ControllerPath.COLLECTOR_ALL.toString();
    private final String UNIQUE = ControllerPath.COLLECTOR_BY_ID.toString();

    private DaoFactory factory;

    public CollectorController(DaoFactory factory) {
        this.factory = factory;
    }

    @Override
    public Response process(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException {
        String contentType = request.getContentType();
        String networkId = null;
        String collectorId = null;

        if (match(request.getResource(), ALL)) {
            Map<String, String> map = extract(request.getResource(), ALL);
            networkId = map.get(ControllerPath.NETWORK_KEY.toString());

            if (request.getMethod() != RequestMethod.GET) {
                throw new NotImplementedException("Action not implemented");
            }
            return builder(getAll(networkId), contentType);
        }

        if (match(request.getResource(), UNIQUE)) {
            Map<String, String> map = extract(request.getResource(), UNIQUE);
            networkId = map.get(ControllerPath.NETWORK_KEY.toString());
            collectorId = map.get(ControllerPath.COLLECTOR_KEY.toString());

            if (request.getMethod() != RequestMethod.GET) {
                throw new NotImplementedException("Action not implemented");
            }
            return builder(getById(networkId, collectorId), contentType);
        }
        return null;
    }

    private List<CollectorSnTo> getAll(String networkId) throws NotFoundException {
//        NetworkDao<Network> ndao = factory.getNetworkDao();
        CollectorDao<Collector> cdao = factory.getCollectorDao();

//        Network nw = ndao.get(networkId);
//
//        if (nw == null) {
//            throw new NotFoundException("Network not exists");
//        }

        List<Collector> cwlist = cdao.getAll(networkId);

        List<CollectorSnTo> collectors = new ArrayList<>();

        for (Collector cw : cwlist) {
            collectors.add(cw.getTransferObject());
        }

        return collectors;
    }

    private CollectorSnTo getById(String networkId, String collectorId) throws NotFoundException {
//        NetworkDao<Network> ndao = factory.getNetworkDao();
        CollectorDao<Collector> cdao = factory.getCollectorDao();

//        Network nw = ndao.get(networkId);
//
//        if (nw == null) {
//            throw new NotFoundException("Network not exists");
//        }

        Collector cw = cdao.get(networkId, collectorId);

//        if (cw == null) {
//            throw new NotFoundException("Collector not exists");
//        }

        return cw.getTransferObject();
    }
}
