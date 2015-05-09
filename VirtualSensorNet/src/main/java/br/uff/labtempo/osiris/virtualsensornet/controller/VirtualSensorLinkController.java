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

import br.uff.labtempo.osiris.virtualsensornet.controller.internal.FieldController;
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
import br.uff.labtempo.osiris.virtualsensornet.controller.util.AnnouncerWrapper;
import br.uff.labtempo.osiris.virtualsensornet.model.util.FieldListManager;
import br.uff.labtempo.osiris.virtualsensornet.persistence.ConverterDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DataTypeDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.LinkDao;
import br.uff.labtempo.osiris.virtualsensornet.thirdparty.announcer.AnnouncerAgent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class VirtualSensorLinkController extends Controller {

    private final DaoFactory factory;
    private AnnouncerAgent announcer;

    public VirtualSensorLinkController(DaoFactory factory, AnnouncerAgent announcer) {
        this.factory = factory;
        this.announcer = new AnnouncerWrapper(announcer);
    }

    public VirtualSensorLinkController(DaoFactory factory) {
        this(factory, null);
    }

    public void setAnnouncerAgent(AnnouncerAgent announcer) {
        this.announcer = announcer;
    }

    @Override
    public Response process(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException, BadRequestException {
        try {
            return routing(request);
        } finally {
            factory.clear();
        }
    }

    public Response routing(Request request) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, NotImplementedException, BadRequestException {
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
                    // /link/{id}
                    String uri = Path.SEPARATOR.toString() + Path.NAMING_RESOURCE_LINK.toString() + Path.SEPARATOR + String.valueOf(id);
                    response = new ResponseBuilder().created(uri).build();
                    return response;
                default:
                    throw new MethodNotAllowedException("Action not allowed for this resource!");
            }
        } else if (match(request.getResource(), Path.RESOURCE_VIRTUALSENSORNET_LINK_BY_ID.toString())) {
            Map<String, String> map = extractParams(request.getResource(), Path.RESOURCE_VIRTUALSENSORNET_LINK_BY_ID.toString());
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
                    throw new MethodNotAllowedException("Action not allowed for this resource!");
            }
        }
        return null;
    }

    public LinkVsnTo get(long id) throws NotFoundException, InternalServerErrorException {
        LinkDao lDao;
        try {
            lDao = factory.getPersistentLinkDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        VirtualSensorLink sensorLink = lDao.get(id);
        if (sensorLink == null) {
            throw new NotFoundException("VirtualSensor not found!");
        }
        LinkVsnTo to = sensorLink.getUniqueTransferObject();
        return to;
    }

    public List<LinkVsnTo> getAll() throws NotFoundException, InternalServerErrorException {
        LinkDao lDao;
        try {
            lDao = factory.getPersistentLinkDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        List<VirtualSensorLink> virtualSensors = lDao.getAll();
        List<LinkVsnTo> linkVsnTos = new ArrayList<>();
        for (VirtualSensorLink vs : virtualSensors) {
            linkVsnTos.add(vs.getUniqueTransferObject());
        }
        return linkVsnTos;
    }

    public synchronized long create(LinkVsnTo linkTo) throws NotFoundException, InternalServerErrorException, BadRequestException {
        String sensorId;
        String collectorId;
        String networkId;
        String label;
        List<? extends FieldTo> fieldsTo;

        try {
            label = linkTo.getLabel().trim();
            sensorId = linkTo.getSensorId().trim();
            collectorId = linkTo.getCollectorId().trim();
            networkId = linkTo.getNetworkId().trim();
            fieldsTo = linkTo.getFields();

            //check nullpointer           
            fieldsTo.size();
        } catch (Exception e) {
            throw new BadRequestException("Params cannot be null!");
        }

        List<Field> fields = createFields(fieldsTo);
        //not able to fields with no entries

        if (fieldsTo.isEmpty()) {
            throw new BadRequestException("Declared Fields cannot be empty!");
        }
        if (fields.isEmpty()) {
            throw new BadRequestException("Declared Fields not found!");
        }

        VirtualSensorLink link = new VirtualSensorLink(label, networkId, collectorId, sensorId, fields);

        LinkDao lDao;
        try {
            lDao = factory.getPersistentLinkDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        lDao.save(link);
        announcer.broadcastIt(link.getTransferObject());
        return link.getId();
    }

    private synchronized List<Field> createFields(List<? extends FieldTo> fieldsTo) throws NotFoundException, InternalServerErrorException, BadRequestException {
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

    public boolean delete(long id) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, BadRequestException {
        LinkDao lDao;
        try {
            lDao = factory.getPersistentLinkDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        VirtualSensorLink sensorLink = lDao.get(id);
        if (sensorLink == null) {
            throw new NotFoundException("VirtualSensor not found!");
        }

        //remove fields
        //remove initialized fields
        FieldController fieldSubController = new FieldController(factory);
        List<Field> currentFields = sensorLink.getFields();
        List<Field> removedFields = new ArrayList<>(currentFields);
        for (Field field : removedFields) {
            fieldSubController.deleteIgnoringInitialization(currentFields, field);
            //detaches sensor from field
            field.setVirtualSensor(null);
        }

        /**
         * Needs
         *
         * update sensor - remove binds between sensor to fields
         *
         * remove fields - remove binds between fields to sensor
         *
         * remove sensor
         */
        try {
            lDao.update(sensorLink);
            for (Field removedField : removedFields) {
                fieldSubController.delete(removedField);
            }
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
        //check arguments
        try {
            sensorId = linkTo.getSensorId().trim();
            collectorId = linkTo.getCollectorId().trim();
            networkId = linkTo.getNetworkId().trim();
        } catch (Exception e) {
            throw new BadRequestException("Params cannot be null!");
        }

        //get DAO
        LinkDao lDao;
        try {
            lDao = factory.getPersistentLinkDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        //recovery virtual sensor link
        VirtualSensorLink sensorLink = lDao.get(id);
        if (sensorLink == null) {
            throw new NotFoundException("VirtualSensor not found!");
        }

        boolean isUpdated = false;

        //update properties 
        if (!sensorLink.getSensorId().equals(sensorId)
                || !sensorLink.getCollectorId().equals(collectorId)
                || !sensorLink.getNetworkId().equals(networkId)) {

            sensorLink.setSensorId(sensorId);
            sensorLink.setCollectorId(collectorId);
            sensorLink.setNetworkId(networkId);
            isUpdated = true;
        }

        //check field list
        List<? extends FieldTo> fieldsTo = linkTo.getFields();
        if (fieldsTo == null || fieldsTo.isEmpty()) {
            throw new BadRequestException("Is required at least one field!");
        }

        List<Field> newFields = createFields(fieldsTo);
        if (newFields == null || newFields.isEmpty()) {
            throw new InternalServerErrorException("Fields cannot had been loaded!");
        }

        //get current fields        
        List<Field> currentFields = sensorLink.getFields();

        //upgrade the fields by field upgrade helper
        FieldListManager listManager = new FieldListManager(newFields);
        try {
            isUpdated = sensorLink.updateFields(listManager);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        List<Field> removedFields = listManager.getExcluded();
        List<Field> insertedFields = listManager.getIncluded();

        //bind field to virtualsensor
        for (Field inserted : insertedFields) {
            inserted.setVirtualSensor(sensorLink);
        }

        //commit changes
        if (isUpdated) {
            //commit sensor changes
            lDao.update(sensorLink);
            //remove unusable fields
            FieldController fieldSubController = new FieldController(factory);
            for (Field removed : removedFields) {
                fieldSubController.delete(removed);
            }
        }
        return isUpdated;
    }
}
