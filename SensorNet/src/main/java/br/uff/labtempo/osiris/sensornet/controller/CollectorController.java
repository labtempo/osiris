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
import br.uff.labtempo.omcp.common.exceptions.BadRequestException;
import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.omcp.common.exceptions.MethodNotAllowedException;
import br.uff.labtempo.omcp.common.exceptions.NotFoundException;
import br.uff.labtempo.omcp.common.exceptions.NotImplementedException;
import br.uff.labtempo.omcp.common.utils.ResponseBuilder;
import br.uff.labtempo.osiris.omcp.Controller;
import br.uff.labtempo.osiris.sensornet.model.Collector;
import br.uff.labtempo.osiris.sensornet.model.Network;
import br.uff.labtempo.osiris.sensornet.persistence.CollectorDao;
import br.uff.labtempo.osiris.sensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.sensornet.persistence.NetworkDao;
import br.uff.labtempo.osiris.to.common.definitions.Path;
import br.uff.labtempo.osiris.to.sensornet.CollectorSnTo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class CollectorController extends Controller {

    private final String ALL = Path.RESOURCE_SENSORNET_COLLECTOR_All.toString();
    private final String UNIQUE = Path.RESOURCE_SENSORNET_COLLECTOR_BY_ID.toString();

    private final DaoFactory factory;

    public CollectorController(DaoFactory factory) {
        this.factory = factory;
    }

    @Override
    public Response process(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException, BadRequestException {
        try {
            return routing(request);
        } finally {
            factory.clear();
        }
    }

    public Response routing(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException {
        String contentType = request.getContentType();
        String networkId;
        String collectorId;

        if (match(request.getResource(), ALL)) {
            Map<String, String> map = extract(request.getResource(), ALL);
            networkId = map.get(Path.ID1.toString());
            switch (request.getMethod()) {
                case GET:
                    List<CollectorSnTo> to = getAll(networkId);
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
            collectorId = map.get(Path.ID2.toString());

            switch (request.getMethod()) {
                case GET:
                    CollectorSnTo to = getById(networkId, collectorId);
                    Response response = new ResponseBuilder().ok(to, contentType).build();
                    return response;
                case PUT:
                    update();
                case DELETE:
                    delete(networkId, collectorId);
                    response = new ResponseBuilder().ok().build();
                    return response;
                default:
                    throw new MethodNotAllowedException("Action not allowed for this resource!");
            }
        }
        return null;
    }

    public List<CollectorSnTo> getAll(String networkId) throws NotFoundException, InternalServerErrorException {
        CollectorDao cdao;
        try {
            cdao = factory.getCollectorDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }
        List<Collector> cwlist = cdao.getAll(networkId);

        List<CollectorSnTo> collectors = new ArrayList<>();

        for (Collector cw : cwlist) {
            collectors.add(cw.getTransferObject());
        }

        return collectors;
    }

    public CollectorSnTo getById(String networkId, String collectorId) throws NotFoundException, InternalServerErrorException {
        CollectorDao cdao;
        try {
            cdao = factory.getCollectorDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }
        Collector collector = cdao.get(networkId, collectorId);
        if (collector == null) {
            throw new NotFoundException("Selected Collector not found!");
        }
        return collector.getTransferObject();
    }

    public boolean delete(String networkId, String collectorId) throws NotFoundException, InternalServerErrorException {
        CollectorDao cdao;
        try {
            cdao = factory.getCollectorDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }
        Collector collector = cdao.get(networkId, collectorId);
        if (collector == null) {
            throw new NotFoundException("Collector not found!");
        }
        Network network = collector.getNetwork();
        network.removeCollector(collector);

        try {
            NetworkDao networkDao = factory.getNetworkDao();
            networkDao.update(network);
            Collector collector1 = collector;
            cdao.delete(collector);
            return true;
        } catch (Exception e) {
            throw new InternalServerErrorException("Collector couldn't removed!");
        }
    }

    public long create() throws MethodNotAllowedException {
        throw new MethodNotAllowedException("A Collector cannot be created directly!");
    }

    public boolean update() throws MethodNotAllowedException {
        throw new MethodNotAllowedException("A Collector is a read-only resource!");
    }
}
