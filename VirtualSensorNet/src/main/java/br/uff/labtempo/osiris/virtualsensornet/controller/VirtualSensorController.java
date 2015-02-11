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
package br.uff.labtempo.osiris.virtualsensornet.controller;

import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.omcp.common.exceptions.MethodNotAllowedException;
import br.uff.labtempo.omcp.common.exceptions.NotFoundException;
import br.uff.labtempo.omcp.common.exceptions.NotImplementedException;
import br.uff.labtempo.omcp.common.utils.ResponseBuilder;
import br.uff.labtempo.osiris.omcp.Controller;
import br.uff.labtempo.osiris.to.common.definitions.Path;
import br.uff.labtempo.osiris.to.virtualsensornet.VirtualSensorVsnTo;
import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensor;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.virtualsensornet.persistence.VirtualSensorDao;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class VirtualSensorController extends Controller {

    private final DaoFactory factory;

    public VirtualSensorController(DaoFactory factory) {
        this.factory = factory;
    }

    @Override
    public Response process(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException {
        String contentType = request.getContentType();
        if (match(request.getResource(), Path.RESOURCE_VIRTUALSENSORNET_VIRTUALSENSOR_ALL.toString())) {
            switch (request.getMethod()) {
                case GET:
                    List<VirtualSensorVsnTo> all = getAll();
                    Response response = new ResponseBuilder().ok(all, contentType).build();
                    return response;
                case POST:
                    create(null);
                default:
                    throw new MethodNotAllowedException("Action not allowed for this resource!");
            }
        } else if (match(request.getResource(), Path.RESOURCE_VIRTUALSENSORNET_VIRTUALSENSOR_BY_ID.toString())) {
            Map<String, String> map = extract(request.getResource(), Path.RESOURCE_VIRTUALSENSORNET_VIRTUALSENSOR_BY_ID.toString());
            String urlId = map.get(Path.ID1.toString());
            switch (request.getMethod()) {
                case GET:
                    long id = Long.valueOf(urlId);
                    VirtualSensorVsnTo to = get(id);
                    Response response = new ResponseBuilder().ok(to, contentType).build();
                    return response;
                case PUT:
                    update(0,null);
                case DELETE:
                    delete(0);
                default:
                    throw new MethodNotAllowedException("Action not allowed for this resource!");
            }
        }
        return null;
    }

    public VirtualSensorVsnTo get(long id) throws NotFoundException {
        VirtualSensorDao sensorDao = factory.getVirtualSensorDao();
        VirtualSensor virtualSensor = sensorDao.get(id);

        if (virtualSensor == null) {
            throw new NotFoundException("VirtualSensor not found!");
        }

        VirtualSensorVsnTo to = virtualSensor.getTransferObject();
        return to;
    }

    public List<VirtualSensorVsnTo> getAll() {
        VirtualSensorDao sensorDao = factory.getVirtualSensorDao();
        List<VirtualSensor> virtualSensors = sensorDao.getAll();

        List<VirtualSensorVsnTo> vsnTos = new ArrayList<>();
        for (VirtualSensor vs : virtualSensors) {
            vsnTos.add(vs.getTransferObject());
        }

        return vsnTos;
    }

    public long create(VirtualSensorVsnTo vsnTo) throws MethodNotAllowedException {
        throw new MethodNotAllowedException("A VirtualSensor cannot be created directly!");
    }

    public boolean delete(long id) throws MethodNotAllowedException {
        throw new MethodNotAllowedException("A VirtualSensor is a read-only resource!");
    }

    public boolean update(long id, VirtualSensorVsnTo to) throws MethodNotAllowedException {
        throw new MethodNotAllowedException("A VirtualSensor is a read-only resource!");
    }

}
