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
import br.uff.labtempo.osiris.sensornet.model.NetworkWrapper;
import br.uff.labtempo.osiris.sensornet.model.jpa.Network;
import br.uff.labtempo.osiris.sensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.sensornet.persistence.NetworkDao;
import br.uff.labtempo.osiris.sensornet.to.NetworkTo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public class NetworkController extends Controller {

    private final String ALL = ControllerPath.NETWORK_ALL.toString();
    private final String UNIQUE = ControllerPath.NETWORK_BY_ID.toString();

    private final DaoFactory factory;

    public NetworkController(DaoFactory factory) {
        this.factory = factory;
    }
    
    @Override
    public Response process(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException {
        String contentType = request.getContentType();
        String networkId = null;

        if (match(request.getResource(), ALL)) {

            if (request.getMethod() != RequestMethod.GET) {
                throw new NotImplementedException("Action not implemented");
            }
            return builder(getAll(), contentType);
        }

        if (match(request.getResource(), UNIQUE)) {
            Map<String, String> map = extract(request.getResource(), UNIQUE);
            networkId = map.get(ControllerPath.NETWORK_KEY.toString());

            if (request.getMethod() != RequestMethod.GET) {
                throw new NotImplementedException("Action not implemented");
            }
            return builder(getById(networkId), contentType);
        }
        return null;
    }

    private List<NetworkTo> getAll() throws NotFoundException {
        NetworkDao<Network> ndao = factory.getNetworkDao();

        List<Network> nwlist = ndao.getAll();

        List<NetworkTo> networks = new ArrayList<>();

        for (Network nw : nwlist) {
            networks.add(nw.getTransferObject());
        }

        return networks;
    }

    private NetworkTo getById(String networkId) throws NotFoundException {
        NetworkDao<Network> ndao = factory.getNetworkDao();

        Network nw = ndao.get(networkId);

        if (nw == null) {
            throw new NotFoundException("Network not exists");
        }

        return nw.getTransferObject();
    }
}
