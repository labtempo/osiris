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
import br.uff.labtempo.osiris.to.virtualsensornet.CompositeVsnTo;
import br.uff.labtempo.osiris.virtualsensornet.model.Field;
import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensor;
import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensorComposite;
import br.uff.labtempo.osiris.virtualsensornet.controller.util.AnnouncerWrapper;
import br.uff.labtempo.osiris.virtualsensornet.model.util.FieldListManager;
import br.uff.labtempo.osiris.virtualsensornet.persistence.CompositeDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.virtualsensornet.persistence.FieldDao;
import br.uff.labtempo.osiris.virtualsensornet.thirdparty.announcer.AnnouncerAgent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class VirtualSensorCompositeController extends Controller {

    private final DaoFactory factory;
    private AnnouncerAgent announcer;

    public VirtualSensorCompositeController(DaoFactory factory, AnnouncerAgent announcer) {
        this.factory = factory;
        this.announcer = new AnnouncerWrapper(announcer);
    }

    public VirtualSensorCompositeController(DaoFactory factory) {
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
        if (match(request.getResource(), Path.RESOURCE_VIRTUALSENSORNET_COMPOSITE_ALL.toString())) {
            switch (request.getMethod()) {
                case GET:
                    List<CompositeVsnTo> all = getAll();
                    Response response = new ResponseBuilder().ok(all, contentType).build();
                    return response;
                case POST:
                    CompositeVsnTo to = request.getContent(CompositeVsnTo.class);
                    long id = create(to);
                    // /composite/{id}
                    String uri = Path.SEPARATOR.toString() + Path.NAMING_RESOURCE_COMPOSITE.toString() + Path.SEPARATOR + String.valueOf(id);
                    response = new ResponseBuilder().created(uri).build();
                    return response;
                default:
                    throw new MethodNotAllowedException("Action not allowed for this resource!");
            }
        } else if (match(request.getResource(), Path.RESOURCE_VIRTUALSENSORNET_COMPOSITE_BY_ID.toString())) {
            Map<String, String> map = extract(request.getResource(), Path.RESOURCE_VIRTUALSENSORNET_COMPOSITE_BY_ID.toString());
            String urlId = map.get(Path.ID1.toString());
            switch (request.getMethod()) {
                case GET:
                    long id = Long.valueOf(urlId);
                    CompositeVsnTo to = get(id);
                    Response response = new ResponseBuilder().ok(to, contentType).build();
                    return response;
                case PUT:
                    id = Long.valueOf(urlId);
                    to = request.getContent(CompositeVsnTo.class);
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

    public CompositeVsnTo get(long id) throws NotFoundException, InternalServerErrorException {
        CompositeDao cd;
        try {
            cd = factory.getPersistentCompositeDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        VirtualSensorComposite composite = cd.getById(id);
        if (composite == null) {
            throw new NotFoundException("VirtualSensor not found!");
        }
        CompositeVsnTo to = composite.getUniqueTransferObject();
        return to;

    }

    public List<CompositeVsnTo> getAll() throws NotFoundException, InternalServerErrorException {
        CompositeDao cd;
        try {
            cd = factory.getPersistentCompositeDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        List<VirtualSensorComposite> virtualSensors = cd.getAll();
        List<CompositeVsnTo> compositeVsnTos = new ArrayList<>();
        for (VirtualSensorComposite vs : virtualSensors) {
            compositeVsnTos.add(vs.getUniqueTransferObject());
        }
        return compositeVsnTos;
    }

    //pronto
    public synchronized long create(CompositeVsnTo compositeVsnTo) throws NotFoundException, InternalServerErrorException, BadRequestException {
        List<? extends FieldTo> fieldsTo;
        String label;
        try {
            fieldsTo = compositeVsnTo.getBoundFields();
            label = compositeVsnTo.getLabel().trim();
            //check nullpointer           
            fieldsTo.size();
        } catch (Exception e) {
            throw new BadRequestException("Params cannot be null!");
        }
        if (fieldsTo.isEmpty()) {
            throw new BadRequestException("Declared Fields cannot be empty!");
        }

        List<Field> fields = getFields(fieldsTo);
        //not able to fields with no entries

        if (fields.isEmpty()) {
            throw new BadRequestException("Declared Fields not found!");
        }

        Field minorUpdateIntervalField = VirtualSensorComposite.selectMinorUpdateIntervalSensor(fields);
        VirtualSensor minorUpdateIntervalSensor = minorUpdateIntervalField.getVirtualSensor();

        VirtualSensorComposite composite = new VirtualSensorComposite(label, fields, minorUpdateIntervalSensor.getCreationInterval(), minorUpdateIntervalSensor.getCreationIntervalTimeUnit());

        CompositeDao cd;
        try {
            cd = factory.getPersistentCompositeDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        cd.save(composite);
        announcer.broadcastIt(composite.getTransferObject());
        return composite.getId();

    }

    //pronto
    private synchronized List<Field> getFields(List<? extends FieldTo> fieldsTo) throws NotFoundException, InternalServerErrorException, BadRequestException {
        FieldController fsc = new FieldController(factory);

        List<Long> ids = new ArrayList<>();
        for (FieldTo fieldTo : fieldsTo) {
            ids.add(fieldTo.getId());
        }

        List<Field> list = fsc.getAllByIds(ids);
        return list;
    }

    //pronto
    public boolean delete(long id) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException {
        FieldController fsc = new FieldController(factory);
        CompositeDao cd;
        try {
            cd = factory.getPersistentCompositeDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        VirtualSensorComposite composite = cd.getById(id);
        if (composite == null) {
            throw new NotFoundException("VirtualSensor not found!");
        }
        try {
            List<Field> fields = composite.getFields();
            composite.removeFields();
            cd.delete(composite);

            for (Field field : fields) {
                fsc.merge(field);
            }

            return true;
        } catch (Exception e) {
            throw new InternalServerErrorException("VirtuaSensorLink couldn't removed!");
        }
    }

    public synchronized boolean update(long id, CompositeVsnTo compositeVsnTo) throws MethodNotAllowedException, NotFoundException, InternalServerErrorException, BadRequestException {
        List<? extends FieldTo> fieldsTo;
        String label;
        try {
            fieldsTo = compositeVsnTo.getBoundFields();
            label = compositeVsnTo.getLabel().trim();
            //check nullpointer           
            fieldsTo.size();
        } catch (Exception e) {
            throw new BadRequestException("Params cannot be null!");
        }
        //get DAO
        CompositeDao cd;
        FieldDao fd;
        try {
            cd = factory.getPersistentCompositeDao();
            fd = factory.getFieldDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        //does composite exists?
        VirtualSensorComposite composite = cd.getById(id);
        if (composite == null) {
            throw new NotFoundException("VirtualSensor not found!");
        }
        ///update mark
        boolean isUpdated = false;

        //check field to not empty
        if (fieldsTo.isEmpty()) {
            throw new BadRequestException("Is required at least one field!");
        }

        //get database fields from fieldsto
        List<Field> newestFields = getFields(fieldsTo);

        //set label if it is a new label
        if (!composite.getLabel().equals(label)) {
            isUpdated = true;
            composite.setLabel(label);
        }

        //change fields if fields is not empty
        if (newestFields == null || newestFields.isEmpty()) {
            throw new InternalServerErrorException("Fields cannot had been loaded!");
        }

        //get current fields of composite
        List<Field> currentFields = composite.getFields();

        //operates on list adding and removing fields by UpdateFieldListHelper
        FieldListManager listManager = new FieldListManager(newestFields);
        try {
            isUpdated = composite.updateFields(listManager);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        List<Field> toRemove = listManager.getExcluded();
        List<Field> toInsert = listManager.getIncluded();

        //adding aggregates to added fields
        for (Field inserted : toInsert) {
            inserted.addAggregate(composite);
        }

        //commit changes
        if (isUpdated) {
            //persists composite        
            cd.update(composite);
            //persists removed fields, removing aggregates 
            for (Field removed : toRemove) {
                removed.removeAggregate(composite);
                fd.update(removed);
            }
        }
        return isUpdated;
    }
}
