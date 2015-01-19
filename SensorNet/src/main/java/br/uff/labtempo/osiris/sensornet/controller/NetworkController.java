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
import br.uff.labtempo.omcp.common.RequestMethod;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.omcp.common.exceptions.MethodNotAllowedException;
import br.uff.labtempo.omcp.common.exceptions.NotFoundException;
import br.uff.labtempo.omcp.common.exceptions.NotImplementedException;
import br.uff.labtempo.osiris.omcp.Controller;
import br.uff.labtempo.osiris.sensornet.model.jpa.Network;
import br.uff.labtempo.osiris.sensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.sensornet.persistence.NetworkDao;
import br.uff.labtempo.osiris.to.sensornet.NetworkSnTo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
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
        String networkId ;

        if (match(request.getResource(), ALL)) {

            if (request.getMethod() != RequestMethod.GET) {
                throw new NotImplementedException("Action not implemented");
            }
            return builderOk(getAll(), contentType);
        }

        if (match(request.getResource(), UNIQUE)) {
            Map<String, String> map = extract(request.getResource(), UNIQUE);
            networkId = map.get(ControllerPath.NETWORK_KEY.toString());

            if (request.getMethod() != RequestMethod.GET) {
                throw new NotImplementedException("Action not implemented");
            }
            return builderOk(getById(networkId), contentType);
        }
        return null;
    }

    private List<NetworkSnTo> getAll() throws NotFoundException {
        NetworkDao ndao = factory.getNetworkDao();

        List<Network> nwlist = ndao.getAll();

        List<NetworkSnTo> networks = new ArrayList<>();

        for (Network nw : nwlist) {
            networks.add(nw.getTransferObject());
        }

        return networks;
    }

    private NetworkSnTo getById(String networkId) throws NotFoundException {
        NetworkDao ndao = factory.getNetworkDao();

        Network nw = ndao.get(networkId);

        if (nw == null) {
            throw new NotFoundException("Network not exists");
        }

        return nw.getTransferObject();
    }
    
    
}
