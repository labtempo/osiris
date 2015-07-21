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

import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.StatusCode;
import br.uff.labtempo.omcp.common.exceptions.BadRequestException;
import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.omcp.common.exceptions.MethodNotAllowedException;
import br.uff.labtempo.omcp.common.exceptions.NotFoundException;
import br.uff.labtempo.omcp.common.exceptions.NotImplementedException;
import br.uff.labtempo.omcp.common.utils.ResponseBuilder;
import br.uff.labtempo.osiris.omcp.Controller;
import br.uff.labtempo.osiris.to.common.data.FieldTo;
import br.uff.labtempo.osiris.to.common.definitions.FunctionOperation;
import br.uff.labtempo.osiris.to.common.definitions.Path;
import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import br.uff.labtempo.osiris.to.function.ParameterizedRequestFn;
import br.uff.labtempo.osiris.to.function.RequestFnTo;
import br.uff.labtempo.osiris.to.function.ResponseFnTo;
import br.uff.labtempo.osiris.to.function.ValueFnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.BlendingBondVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.BlendingVsnTo;
import br.uff.labtempo.osiris.utils.scheduling.Scheduler;
import br.uff.labtempo.osiris.utils.scheduling.SchedulerItem;
import br.uff.labtempo.osiris.utils.scheduling.SchedulingCallback;
import br.uff.labtempo.osiris.virtualsensornet.controller.util.AggregatesChecker;
import br.uff.labtempo.osiris.virtualsensornet.controller.internal.AggregatesCheckerController;
import br.uff.labtempo.osiris.virtualsensornet.controller.internal.FieldController;
import br.uff.labtempo.osiris.virtualsensornet.model.BlendingBond;
import br.uff.labtempo.osiris.virtualsensornet.model.DataConverter;
import br.uff.labtempo.osiris.virtualsensornet.model.DataType;
import br.uff.labtempo.osiris.virtualsensornet.model.Field;
import br.uff.labtempo.osiris.virtualsensornet.model.Function;
import br.uff.labtempo.osiris.virtualsensornet.model.FunctionParam;
import br.uff.labtempo.osiris.virtualsensornet.model.util.FunctionType;
import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensorBlending;
import br.uff.labtempo.osiris.virtualsensornet.model.state.ModelState;
import br.uff.labtempo.osiris.virtualsensornet.controller.util.AggregatesCheckerWrapper;
import br.uff.labtempo.osiris.virtualsensornet.controller.util.AnnouncerWrapper;
import br.uff.labtempo.osiris.virtualsensornet.controller.util.OmcpClientWrapper;
import br.uff.labtempo.osiris.virtualsensornet.controller.util.SchedulerWrapper;
import br.uff.labtempo.osiris.virtualsensornet.model.util.BlendingValuesWrapper;
import br.uff.labtempo.osiris.virtualsensornet.model.util.FieldListManager;
import br.uff.labtempo.osiris.virtualsensornet.persistence.BlendingDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.ConverterDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DataTypeDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.FunctionDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.SchedulerDao;
import br.uff.labtempo.osiris.virtualsensornet.thirdparty.announcer.AnnouncerAgent;
import br.uff.labtempo.osiris.virtualsensornet.thirdparty.scheduler.ModelSchedulerItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class VirtualSensorBlendingController extends Controller implements SchedulingCallback {

    private final DaoFactory factory;
    private final AggregatesChecker checker;
    private AnnouncerAgent announcer;
    private Scheduler scheduler;
    private OmcpClient client;

    public VirtualSensorBlendingController(DaoFactory factory, AnnouncerAgent announcer, AggregatesChecker checker) {
        this.factory = factory;
        this.checker = new AggregatesCheckerWrapper(checker);
        this.announcer = new AnnouncerWrapper(announcer);
        this.scheduler = new SchedulerWrapper(null);
        this.client = new OmcpClientWrapper(null);
    }

    public VirtualSensorBlendingController(DaoFactory factory, AggregatesCheckerController checkerController) {
        this(factory, null, checkerController);
    }

    public VirtualSensorBlendingController(DaoFactory factory) {
        this(factory, null, null);
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
        if (match(request.getResource(), Path.RESOURCE_VIRTUALSENSORNET_BLENDING_ALL.toString())) {
            switch (request.getMethod()) {
                case GET:
                    List<BlendingVsnTo> all = getAll();
                    Response response = new ResponseBuilder().ok(all, contentType).build();
                    return response;
                case POST:
                    BlendingVsnTo to = request.getContent(BlendingVsnTo.class);
                    long id = create(to);
                    // /blending/{id}
                    String uri = Path.SEPARATOR.toString() + Path.NAMING_RESOURCE_BLENDING.toString() + Path.SEPARATOR + String.valueOf(id);
                    response = new ResponseBuilder().created(uri).build();
                    return response;
                default:
                    throw new MethodNotAllowedException("Action not allowed for this resource!");
            }
        } else if (match(request.getResource(), Path.RESOURCE_VIRTUALSENSORNET_BLENDING_BY_ID.toString())) {
            Map<String, String> map = extractParams(request.getResource(), Path.RESOURCE_VIRTUALSENSORNET_BLENDING_BY_ID.toString());
            String urlId = map.get(Path.ID1.toString());
            switch (request.getMethod()) {
                case GET:
                    long id = Long.valueOf(urlId);
                    BlendingVsnTo to = get(id);
                    Response response = new ResponseBuilder().ok(to, contentType).build();
                    return response;
                case POST:
                    id = Long.valueOf(urlId);
                    ResponseFnTo responseFnTo = request.getContent(ResponseFnTo.class);
                    insertValue(id, responseFnTo);
                    // /blending/{id}/{valueId}
                    //String uri = Path.SEPARATOR.toString() + Path.NAMING_RESOURCE_BLENDING.toString() + Path.SEPARATOR + String.valueOf(id);
                    response = new ResponseBuilder().ok().build();
                    return response;
                case PUT:
                    id = Long.valueOf(urlId);
                    to = request.getContent(BlendingVsnTo.class);
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

    public void insertValue(long id, ResponseFnTo responseFnTo) throws InternalServerErrorException, NotFoundException {
        BlendingDao blendingDao;
        try {
            blendingDao = factory.getPersistentBlendingDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        VirtualSensorBlending blending = blendingDao.getById(id);
        if (blending == null) {
            throw new NotFoundException("Blending not found!");
        }

        //create field values wrapper object
        List<ValueFnTo> valueFnTos = responseFnTo.getValues();
        long creationTimeInMillis = responseFnTo.getRequestTimestampInMillis();
        int creationPrecisionInNano = 0;
        long acquisitionTimestampInMillis = responseFnTo.getResponseTimestampInMillis();
        long creationInterval = blending.getCallIntervalInMillis();
        TimeUnit creationIntervalTimeUnit = TimeUnit.MILLISECONDS;
        BlendingValuesWrapper wrapper = new BlendingValuesWrapper(valueFnTos, creationTimeInMillis, creationPrecisionInNano, acquisitionTimestampInMillis, creationInterval, creationIntervalTimeUnit);

        blending.setFieldsValues(wrapper);

        blendingDao.update(blending);

        //broadcast        
        announcer.broadcastIt(blending.getTransferObject());

        //checking aggregates
        checker.check(blending);
    }

    public BlendingVsnTo get(long id) throws NotFoundException, InternalServerErrorException {
        VirtualSensorBlending blending = getById(id);
        BlendingVsnTo to = blending.getUniqueTransferObject();
        return to;
    }

    private VirtualSensorBlending getById(long id) throws NotFoundException, InternalServerErrorException {
        BlendingDao blendingDao;
        try {
            blendingDao = factory.getPersistentBlendingDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        VirtualSensorBlending blending = blendingDao.getById(id);
        if (blending == null) {
            throw new NotFoundException("Blending not found!");
        }
        return blending;
    }

    public List<BlendingVsnTo> getAll() throws NotFoundException, InternalServerErrorException {
        BlendingDao blendingDao;
        try {
            blendingDao = factory.getBlendingDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        List<VirtualSensorBlending> blendings = blendingDao.getAll();
        List<BlendingVsnTo> blendingVsnTos = new ArrayList<>();
        for (VirtualSensorBlending vs : blendings) {
            blendingVsnTos.add(vs.getUniqueTransferObject());
        }
        return blendingVsnTos;
    }

    public synchronized long create(BlendingVsnTo blendingVsnTo) throws NotFoundException, InternalServerErrorException, BadRequestException {
        String label;
        List<? extends FieldTo> fieldsTo;

        try {
            label = blendingVsnTo.getLabel().trim();
            fieldsTo = blendingVsnTo.getFields();
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

        VirtualSensorBlending blending = new VirtualSensorBlending(label, fields);

        BlendingDao blendingDao;
        try {
            blendingDao = factory.getBlendingDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        blendingDao.save(blending);
        announcer.broadcastIt(blending.getTransferObject());
        return blending.getId();
    }

    public synchronized boolean update(long id, BlendingVsnTo blendingVsnTo) throws NotFoundException, InternalServerErrorException, BadRequestException {
        String label;
        List<? extends FieldTo> fieldsTo;

        //check arguments
        try {
            label = blendingVsnTo.getLabel().trim();
            fieldsTo = blendingVsnTo.getFields();
            //check nullpointer           
            fieldsTo.size();
        } catch (Exception e) {
            throw new BadRequestException("Params cannot be null!");
        }

        //get DAO
        BlendingDao blendingDao;
        try {
            blendingDao = factory.getPersistentBlendingDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        //recovery virtual sensor link
        VirtualSensorBlending blending = blendingDao.getById(id);
        if (blending == null) {
            throw new NotFoundException("VirtualSensor not found!");
        }

        boolean isUpdated = false;

        //update properties 
        if (!blending.getLabel().equals(label)) {
            blending.setLabel(label);
            isUpdated = true;
        }

        if (fieldsTo == null || fieldsTo.isEmpty()) {
            throw new BadRequestException("Is required at least one field!");
        }

        List<Field> newFields = createFields(fieldsTo);
        if (newFields == null || newFields.isEmpty()) {
            throw new InternalServerErrorException("Fields cannot had been loaded!");
        }

        //get current fields        
        List<Field> currentFields = blending.getFields();

        //upgrade the fields by field upgrade helper
        FieldListManager listManager = new FieldListManager(newFields);
        try {
            isUpdated = blending.updateFields(listManager);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        List<Field> removedFields = listManager.getExcluded();
        List<Field> insertedFields = listManager.getIncluded();

        //bind field to virtualsensor
        for (Field inserted : insertedFields) {
            inserted.setVirtualSensor(blending);
        }

        //optional params
        long functionId;
        long callIntervalInMillis = 0;
        FunctionOperation callMode = null;
        List<BlendingBondVsnTo> requestParams = null;
        List<BlendingBondVsnTo> responseParams = null;
        try {
            functionId = blendingVsnTo.getFunctionId();

            if (functionId != 0) {
                callIntervalInMillis = blendingVsnTo.getCallIntervalInMillis();
                callMode = blendingVsnTo.getCallMode();
                requestParams = blendingVsnTo.getRequestParams();
                responseParams = blendingVsnTo.getResponseParams();

                //check nullpointer
                requestParams.size();
                responseParams.size();
                callMode.toString();
            }
        } catch (Exception e) {
            throw new BadRequestException("Params cannot be null!");
        }

        boolean hasFunction = false;
        List<Field> toUpdateFields = null;
        if (functionId != 0) {
            //get DAO
            if (blending.getFunction() == null || blending.getFunction().getId() != functionId) {
                FunctionDao functionDao;
                try {
                    functionDao = factory.getFunctionDao();
                } catch (Exception e) {
                    throw new InternalServerErrorException("Data query error!");
                }

                //recovery function
                Function function = functionDao.getById(functionId);
                if (function == null) {
                    throw new NotFoundException("Function not found!");
                }

                blending.setFunction(function);
                isUpdated = true;
            }

            if (blending.getCallIntervalInMillis() != callIntervalInMillis) {
                blending.setCallIntervalInMillis(callIntervalInMillis);
                isUpdated = true;
            }

            if (callMode != null && blending.getCallMode() != callMode) {
                blending.setCallMode(callMode);
                isUpdated = true;
            }

            //check function params
            Function function = blending.getFunction();
            if (!checkFunctionParams(requestParams, function.getRequestParams())) {
                throw new NotFoundException("Incorrect request's params for Function!");
            }
            if (!checkFunctionParams(responseParams, function.getResponseParams())) {
                throw new NotFoundException("Incorrect response's params for Function!");
            }
            //check fields
            List<BlendingBond> requestBonds = createRequestBlendingBonds(requestParams, function.getRequestParams());
            List<BlendingBond> responseBonds = createResponseBlendingBonds(responseParams, function.getResponseParams(), blending);

            toUpdateFields = blending.setRequestFields(requestBonds);
            blending.setResponseFields(responseBonds);

            hasFunction = true;
            isUpdated = true;
        } else {
            toUpdateFields = blending.setRequestFields(null);
            blending.removeFunction();
        }

        //commit changes
        if (isUpdated) {
            //commit sensor changes
            blendingDao.update(blending);
            //remove unusable fields
            FieldController fieldSubController = new FieldController(factory);
            for (Field removed : removedFields) {
                fieldSubController.delete(removed);
            }

            //update excluded bond fields
            if (toUpdateFields != null) {
                for (Field toUpdateField : toUpdateFields) {
                    fieldSubController.merge(toUpdateField);
                }
            }

            //insert to scheduler
            if (hasFunction) {
                //TODO: create update scheduler item
                insertToScheduler(blending);
            } else {
                removeFromScheduler(blending);
            }
        }
        return isUpdated;
    }

    public boolean delete(long id) throws NotFoundException, InternalServerErrorException, BadRequestException {
        BlendingDao blendingDao;
        try {
            blendingDao = factory.getPersistentBlendingDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }

        VirtualSensorBlending blending = blendingDao.getById(id);
        if (blending == null) {
            throw new NotFoundException("Selected Blending not found!");
        }

        //remove fields
        //remove initialized fields
        FieldController fieldSubController = new FieldController(factory);
        List<Field> currentFields = blending.getFields();
        List<Field> removedFields = new ArrayList<>(currentFields);
        for (Field field : removedFields) {
            fieldSubController.deleteIgnoringInitialization(currentFields, field);
            //detaches sensor from field
            field.setVirtualSensor(null);
        }

        //remove request fields in bonds
        List<Field> toUpdateFields = blending.removeRequestFields();
        //remove response fields in bonds
        blending.removeResponseFields();

        try {
            blendingDao.update(blending);

            //update dependent fields
            for (Field field : toUpdateFields) {
                fieldSubController.merge(field);
            }

            for (Field removedField : removedFields) {
                fieldSubController.delete(removedField);
            }
            blendingDao.delete(blending);
            return true;
        } catch (Exception e) {
            throw new InternalServerErrorException("Blending couldn't removed!");
        }
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

    private List<BlendingBond> createResponseBlendingBonds(List<BlendingBondVsnTo> bondVsnTos, List<FunctionParam> functionParams, VirtualSensorBlending blending) throws InternalServerErrorException, BadRequestException {
        List<BlendingBond> blendingBonds = new ArrayList<>();
        for (BlendingBondVsnTo bondVsnTo : bondVsnTos) {
            List<Field> tempFields = blending.getFields();
            Field field = null;
            for (Field tempField : tempFields) {
                long id = bondVsnTo.getFieldId();
                if (tempField.getId() == id) {
                    field = tempField;
                }
            }
            BlendingBond bb = createBlendingBonds(functionParams, bondVsnTo, field, true);
            blendingBonds.add(bb);
        }
        return blendingBonds;
    }

    private List<BlendingBond> createRequestBlendingBonds(List<BlendingBondVsnTo> bondVsnTos, List<FunctionParam> functionParams) throws InternalServerErrorException, BadRequestException {
        FieldController fieldController = new FieldController(factory);
        List<BlendingBond> blendingBonds = new ArrayList<>();
        for (BlendingBondVsnTo bondVsnTo : bondVsnTos) {
            Field field = fieldController.getById(bondVsnTo.getFieldId());
            BlendingBond bb = createBlendingBonds(functionParams, bondVsnTo, field, false);
            blendingBonds.add(bb);
        }
        return blendingBonds;
    }

    private BlendingBond createBlendingBonds(List<FunctionParam> functionParams, BlendingBondVsnTo bondVsnTo, Field field, boolean isResponse) throws BadRequestException {
        //check field exist
        if (field == null) {
            throw new BadRequestException("Declared Field for Function not found!");
        }

        //datatype switch 
        DataType fieldDataType;
        if (isResponse) {
            fieldDataType = field.getInputDataType();
        } else {
            fieldDataType = field.getDataType();
        }

        //check if the "function param type" is compatible with "field data type"
        FunctionType functionType = null;
        for (FunctionParam functionParam : functionParams) {
            ValueType typeA = functionParam.getType().getType();
            ValueType typeB = fieldDataType.getValueType();
            if (typeA == typeB) {
                functionType = functionParam.getType();
                break;
            }
        }

        //evaluation of function param type
        if (functionType == null) {
            throw new BadRequestException("Function type and Field type are not compatible!");
        }

        //evaluation of units
        if (functionType.hasUnit()) {
            if (!fieldDataType.getUnit().equalsIgnoreCase(functionType.getUnit())) {
                throw new BadRequestException("Function unit and Field unit are not compatible!");
            }
        }

        BlendingBond bb = new BlendingBond(bondVsnTo.getParamName(), field);
        return bb;
    }

    private boolean checkFunctionParams(List<BlendingBondVsnTo> bondVsnTos, List<FunctionParam> params) {
        for (BlendingBondVsnTo bondVsnTo : bondVsnTos) {
            boolean isValid = false;
            for (FunctionParam param : params) {
                if (bondVsnTo.getParamName().equalsIgnoreCase(param.getName())) {
                    isValid = true;
                }
            }
            if (!isValid) {
                return false;
            }
        }
        return true;
    }

    //scheduler callback
    @Override
    public void callback(List<? extends SchedulerItem> items) throws Exception {
        BlendingDao blendingDao;
        try {
            blendingDao = factory.getPersistentBlendingDao();
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }
        if (items != null && items.size() > 0) {
            for (SchedulerItem item : items) {

                ModelSchedulerItem msi = (ModelSchedulerItem) item;
                long sensorId = item.getObjectId();
                VirtualSensorBlending blending = blendingDao.getById(sensorId);
                msi.setTime(blending);

                ModelState oldModelState = blending.getModelState();
                boolean canRequest = blending.canRequest();
                blendingDao.update(blending);

                //notify
                switch (blending.getModelState()) {
                    case INACTIVE:
                        if (oldModelState != ModelState.INACTIVE) {
                            announcer.notifyDeactivation(blending.getTransferObject());
                            announcer.broadcastIt(blending.getTransferObject());
                        }
                        break;
                    case MALFUNCTION:
                        if (oldModelState != ModelState.MALFUNCTION) {
                            announcer.notifyMalfunction(blending.getTransferObject());
                        }
                        break;
                    case REACTIVATED:
                        if (oldModelState != ModelState.REACTIVATED) {
                            announcer.notifyReactivation(blending.getTransferObject());
                        }
                        break;
                }

                if (canRequest) {
                    callFunction(blending);
                }
            }
        }
    }

    public void setSchedulerAgent(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    private void insertToScheduler(VirtualSensorBlending blending) {
        SchedulerItem item = new ModelSchedulerItem(blending);
        scheduler.schedule(item);
    }

    private void removeFromScheduler(VirtualSensorBlending blending) {
        SchedulerDao schedulerDao = factory.getSchedulerDao();
        SchedulerItem item = schedulerDao.getItemByObjectId(blending.getId());
        if (item != null) {
            schedulerDao.delete(item);
        }
    }

    private void callFunction(VirtualSensorBlending blending) {
        FunctionOperation operation = blending.getCallMode();
        Function function = blending.getFunction();
        String address = function.getAddress();
        RequestFnTo requestFnTo = blending.getFunctionRequest();

        switch (operation) {
            case ASYNCHRONOUS:
                try {
                    //omcp://virtualsensornet/blending/{id}
                    String module = getContext().getHost();
                    String completePath = module + Path.NAMING_RESOURCE_BLENDING + Path.SEPARATOR + blending.getId();
                    requestFnTo.setResponseTo(completePath);
                    Response response = client.doPost(address, requestFnTo);
                    if (response != null) {
                        if (response.getStatusCode() != StatusCode.CREATED) {
                            throw new RuntimeException(response.getErrorMessage());
                        }
                    }
                } catch (Exception e) {
                    Logger.getLogger(VirtualSensorBlendingController.class.getName()).log(Level.WARNING, "{0}:[POST]{1}", new Object[]{e.getMessage(), address});
                }
                break;
            case SYNCHRONOUS:
                ParameterizedRequestFn requestFn = new ParameterizedRequestFn(requestFnTo);
                Response response = null;
                String url = requestFn.getRequestUri(address);
                try {
                    response = client.doGet(url);
                } catch (Exception e) {
                    Logger.getLogger(VirtualSensorBlendingController.class.getName()).log(Level.WARNING, "{0}:[GET]{1}", new Object[]{e.getMessage(), url});
                }

                if (response != null) {
                    if (response.getStatusCode() != StatusCode.OK) {
                        RuntimeException ex = new RuntimeException(response.getErrorMessage());
                        Logger.getLogger(VirtualSensorBlendingController.class.getName()).log(Level.SEVERE, null, ex);
                        break;
                    }
                    ResponseFnTo responseFnTo = response.getContent(ResponseFnTo.class);
                    try {
                        insertValue(blending.getId(), responseFnTo);
                    } catch (InternalServerErrorException | NotFoundException ex) {
                        Logger.getLogger(VirtualSensorBlendingController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            default:
                break;
        }
    }

    public void setClient(OmcpClient omcpClientBlending) {
        client = new OmcpClientWrapper(omcpClientBlending);
    }
}
