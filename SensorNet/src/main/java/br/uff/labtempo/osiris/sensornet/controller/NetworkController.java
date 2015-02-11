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
import br.uff.labtempo.osiris.sensornet.model.Network;
import br.uff.labtempo.osiris.sensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.sensornet.persistence.NetworkDao;
import br.uff.labtempo.osiris.to.common.definitions.Path;
import br.uff.labtempo.osiris.to.sensornet.NetworkSnTo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class NetworkController extends Controller {

    private final String ALL = Path.RESOURCE_SENSORNET_NETWORK_All.toString();
    private final String UNIQUE = Path.RESOURCE_SENSORNET_NETWORK_BY_ID.toString();

    private final DaoFactory factory;

    public NetworkController(DaoFactory factory) {
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
        String networkId;

        if (match(request.getResource(), ALL)) {
            switch (request.getMethod()) {
                case GET:
                    List<NetworkSnTo> to = getAll();
                    Response response = new ResponseBuilder().ok(to, contentType).build();
                    return response;
                case POST:
                    create();
                default:
                    throw new MethodNotAllowedException("Action not allowed for this resource!");
            }
        } else if (match(request.getResource(), UNIQUE)) {
            Map<String, String> map = extract(request.getResource(), UNIQUE);
            networkId = map.get(Path.ID1.toString());
            switch (request.getMethod()) {
                case GET:
                    NetworkSnTo to = getById(networkId);
                    Response response = new ResponseBuilder().ok(to, contentType).build();
                    return response;
                case PUT:
                    update();
                case DELETE:
                    delete(networkId);
                    response = new ResponseBuilder().ok().build();
                    return response;
                default:
                    throw new MethodNotAllowedException("Action not allowed for this resource!");
            }
        }
        return null;
    }

    public List<NetworkSnTo> getAll() throws NotFoundException, InternalServerErrorException {
        NetworkDao ndao;
        try {
            ndao = factory.getNetworkDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }
        List<Network> nwlist = ndao.getAll();

        List<NetworkSnTo> networks = new ArrayList<>();

        for (Network nw : nwlist) {
            networks.add(nw.getTransferObject());
        }

        return networks;
    }

    public NetworkSnTo getById(String networkUniqueName) throws NotFoundException, InternalServerErrorException {
        NetworkDao ndao;
        try {
            ndao = factory.getNetworkDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }
        Network nw = ndao.get(networkUniqueName);

        if (nw == null) {
            throw new NotFoundException("Network not exists");
        }

        return nw.getTransferObject();
    }

    public boolean delete(String name) throws NotFoundException, InternalServerErrorException {
        NetworkDao ndao;
        try {
            ndao = factory.getNetworkDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }
        Network network = ndao.get(name);
        if (network == null) {
            throw new NotFoundException("Network not found!");
        }
        try {
            ndao.delete(network);
            return true;
        } catch (Exception e) {
            throw new InternalServerErrorException("Network couldn't removed!");
        }
    }

    public long create() throws MethodNotAllowedException {
        throw new MethodNotAllowedException("A Network cannot be created directly!");
    }

    public boolean update() throws MethodNotAllowedException {
        throw new MethodNotAllowedException("A Network is a read-only resource!");
    }
}
