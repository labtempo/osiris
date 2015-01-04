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
import br.uff.labtempo.osiris.sensornet.persistence.CollectorDao;
import br.uff.labtempo.osiris.sensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.to.sensornet.CollectorSnTo;
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

    private final DaoFactory factory;

    public CollectorController(DaoFactory factory) {
        this.factory = factory;
    }

    @Override
    public Response process(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException {
        String contentType = request.getContentType();
        String networkId;
        String collectorId;

        if (match(request.getResource(), ALL)) {
            Map<String, String> map = extract(request.getResource(), ALL);
            networkId = map.get(ControllerPath.NETWORK_KEY.toString());

            if (request.getMethod() != RequestMethod.GET) {
                throw new NotImplementedException("Action not implemented");
            }
            List<CollectorSnTo> collectors = getAll(networkId);
            return builder(collectors, contentType);
        }

        if (match(request.getResource(), UNIQUE)) {
            Map<String, String> map = extract(request.getResource(), UNIQUE);
            networkId = map.get(ControllerPath.NETWORK_KEY.toString());
            collectorId = map.get(ControllerPath.COLLECTOR_KEY.toString());

            if (request.getMethod() != RequestMethod.GET) {
                throw new NotImplementedException("Action not implemented");
            }
            CollectorSnTo collector = getById(networkId, collectorId);
            return builder(collector, contentType);
        }
        return null;
    }

    private List<CollectorSnTo> getAll(String networkId) throws NotFoundException {
        CollectorDao cdao = factory.getCollectorDao();
        List<Collector> cwlist = cdao.getAll(networkId);

        List<CollectorSnTo> collectors = new ArrayList<>();

        for (Collector cw : cwlist) {
            collectors.add(cw.getTransferObject());
        }

        return collectors;
    }

    private CollectorSnTo getById(String networkId, String collectorId) throws NotFoundException {
        CollectorDao cdao = factory.getCollectorDao();
        Collector cw = cdao.get(networkId, collectorId);
        return cw.getTransferObject();
    }
}
