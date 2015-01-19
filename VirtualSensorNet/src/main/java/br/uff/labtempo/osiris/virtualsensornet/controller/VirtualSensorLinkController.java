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

import br.uff.labtempo.osiris.to.virtualsensornet.LinkVsnTo;
import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.exceptions.BadRequestException;
import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.omcp.common.exceptions.MethodNotAllowedException;
import br.uff.labtempo.omcp.common.exceptions.NotFoundException;
import br.uff.labtempo.omcp.common.exceptions.NotImplementedException;
import br.uff.labtempo.omcp.common.utils.ResponseBuilder;
import br.uff.labtempo.osiris.omcp.Controller;
import br.uff.labtempo.osiris.to.common.data.FieldTo;
import br.uff.labtempo.osiris.to.common.definitions.Path;
import br.uff.labtempo.osiris.virtualsensornet.model.DataConverter;
import br.uff.labtempo.osiris.virtualsensornet.model.DataType;
import br.uff.labtempo.osiris.virtualsensornet.model.Field;
import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensorLink;
import br.uff.labtempo.osiris.virtualsensornet.persistence.ConverterDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DataTypeDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.LinkDao;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class VirtualSensorLinkController extends Controller {

    private final DaoFactory factory;

    public VirtualSensorLinkController(DaoFactory factory) {
        this.factory = factory;
    }

    @Override
    public Response process(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException, BadRequestException {
        String contentType = request.getContentType();
        if (match(request.getResource(), Path.RESOURCE_VIRTUALSENSORNET_LINK_ALL.toString())) {
            switch (request.getMethod()) {
                case GET:
                    List<LinkVsnTo> all = getAll();
                    Response response = new ResponseBuilder().ok(all, contentType).build();
                    return response;
                case POST:
                    LinkVsnTo to = request.getContent(LinkVsnTo.class);
                    long id = create(to);
                    // omcp://virtualsensornet/link/{id}
                    String uri = getContext().getHost() + Path.NAMING_RESOURCE_LINK + Path.SEPARATOR + String.valueOf(id);
                    response = new ResponseBuilder().created(uri).build();
                    return response;
                default:
                    throw new NotImplementedException("Action not implemented");
            }
        } else if (match(request.getResource(), Path.RESOURCE_VIRTUALSENSORNET_LINK_BY_ID.toString())) {
            Map<String, String> map = extract(request.getResource(), Path.RESOURCE_VIRTUALSENSORNET_LINK_BY_ID.toString());
            String urlId = map.get(Path.ID1.toString());
            switch (request.getMethod()) {
                case GET:
                    long id = Long.valueOf(urlId);
                    LinkVsnTo to = get(id);
                    Response response = new ResponseBuilder().ok(to, contentType).build();
                    return response;
                case PUT:
                    id = Long.valueOf(urlId);
                    to = request.getContent(LinkVsnTo.class);
                    update(id, to);
                    response = new ResponseBuilder().ok().build();
                    return response;
                case DELETE:
                    id = Long.valueOf(urlId);
                    delete(id);
                    response = new ResponseBuilder().ok().build();
                    return response;
                default:
                    throw new NotImplementedException("Action not implemented");
            }
        }
        return null;
    }

    public LinkVsnTo get(long id) throws NotFoundException, InternalServerErrorException {
        LinkDao lDao;
        try {
            lDao = factory.getLinkDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        VirtualSensorLink sensorLink = lDao.get(id);
        if (sensorLink == null) {
            throw new NotFoundException("VirtualSensor not found!");
        }
        LinkVsnTo to = sensorLink.getLinkTransferObject();
        return to;

    }

    public List<LinkVsnTo> getAll() throws NotFoundException, InternalServerErrorException {
        LinkDao lDao;
        try {
            lDao = factory.getLinkDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        List<VirtualSensorLink> virtualSensors = lDao.getAll();
        List<LinkVsnTo> linkVsnTos = new ArrayList<>();
        for (VirtualSensorLink vs : virtualSensors) {
            linkVsnTos.add(vs.getLinkTransferObject());
        }
        return linkVsnTos;
    }

    public synchronized long create(LinkVsnTo linkTo) throws NotFoundException, InternalServerErrorException, BadRequestException {
        String sensorId;
        String collectorId;
        String networkId;
        List<? extends FieldTo> fieldsTo;

        try {
            sensorId = linkTo.getSensorId().trim();
            collectorId = linkTo.getCollectorId().trim();
            networkId = linkTo.getNetworkId().trim();
            fieldsTo = linkTo.getFields();

            //check nulpointer           
            fieldsTo.size();
        } catch (Exception e) {
            throw new BadRequestException("Params cannot be null!");
        }

        List<Field> fields = getFields(fieldsTo);
        //not able to fields with no entries

        if (fieldsTo.isEmpty()) {
            throw new BadRequestException("Declared Fields cannot be empty!");
        }
        if (fields.isEmpty()) {
            throw new BadRequestException("Declared Fields not found!");
        }

        VirtualSensorLink link = new VirtualSensorLink(networkId, collectorId, sensorId, fields);

        LinkDao lDao;
        try {
            lDao = factory.getLinkDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        lDao.save(link);
        return link.getId();

    }

    private synchronized List<Field> getFields(List<? extends FieldTo> fieldsTo) throws NotFoundException, InternalServerErrorException, BadRequestException {
        DataTypeDao dtDao;
        ConverterDao cDao;
        try {
            dtDao = factory.getDataTypeDao();
            cDao = factory.getConverterDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        List<Field> list = new ArrayList<>();
        for (FieldTo fieldTo : fieldsTo) {

            DataType dataType = dtDao.getById(fieldTo.getDataTypeId());
            DataConverter converter = cDao.getById(fieldTo.getConverterId());

            if (dataType == null) {
                throw new BadRequestException("Selected DataType not found!");
            }

            Field field = new Field(fieldTo.getId(), fieldTo.getName(), dataType);
            if (converter != null) {
                try {
                    field.setConverter(converter);
                } catch (Exception e) {
                    throw new BadRequestException(e.getMessage());
                }
            }
            list.add(field);

        }
        return list;
    }

    public boolean delete(long id) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException {
        LinkDao lDao;
        try {
            lDao = factory.getLinkDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        VirtualSensorLink sensorLink = lDao.get(id);
        if (sensorLink == null) {
            throw new NotFoundException("VirtualSensor not found!");
        }
        try {
            lDao.delete(sensorLink);
            return true;
        } catch (Exception e) {
            throw new InternalServerErrorException("VirtuaSensorLink couldn't removed!");
        }
    }

    public synchronized boolean update(long id, LinkVsnTo linkTo) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, BadRequestException {
        String sensorId;
        String collectorId;
        String networkId;
        try {
            sensorId = linkTo.getSensorId().trim();
            collectorId = linkTo.getCollectorId().trim();
            networkId = linkTo.getNetworkId().trim();
        } catch (Exception e) {
            throw new BadRequestException("Params cannot be null!");
        }

        LinkDao lDao;
        try {
            lDao = factory.getLinkDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        VirtualSensorLink sensorLink = lDao.get(id);
        if (sensorLink == null) {
            throw new NotFoundException("VirtualSensor not found!");
        }

        boolean isUpdated = false;

        if (!sensorLink.getSensorId().equals(sensorId)
                || !sensorLink.getCollectorId().equals(collectorId)
                || !sensorLink.getNetworkId().equals(networkId)) {

            sensorLink.setSensorId(sensorId);
            sensorLink.setCollectorId(collectorId);
            sensorLink.setNetworkId(networkId);
            isUpdated = true;
        }

        List<? extends FieldTo> fieldsTo = linkTo.getFields();
        if (fieldsTo == null || fieldsTo.isEmpty()) {
            throw new BadRequestException("Is required at least one field!");
        }

        List<Field> fields = getFields(fieldsTo);

        if (!fields.isEmpty()) {
            try {
                if (sensorLink.upgradeFields(fields)) {
                    isUpdated = true;
                }
            } catch (Exception e) {
                throw new BadRequestException(e.getMessage());
            }
        }

        if (isUpdated) {
            lDao.update(sensorLink);
        }
        return isUpdated;
    }
}
