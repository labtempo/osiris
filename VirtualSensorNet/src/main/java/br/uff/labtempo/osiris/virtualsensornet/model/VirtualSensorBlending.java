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
package br.uff.labtempo.osiris.virtualsensornet.model;

import br.uff.labtempo.osiris.virtualsensornet.model.util.FunctionType;
import br.uff.labtempo.osiris.virtualsensornet.model.interfaces.IBlending;
import br.uff.labtempo.osiris.to.common.definitions.FunctionOperation;
import br.uff.labtempo.osiris.to.function.RequestFnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.BlendingVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.VirtualSensorType;
import br.uff.labtempo.osiris.virtualsensornet.model.state.ModelState;
import br.uff.labtempo.osiris.virtualsensornet.model.util.BlendingValuesWrapper;
import br.uff.labtempo.osiris.virtualsensornet.model.util.FieldListManager;
import br.uff.labtempo.osiris.virtualsensornet.model.util.field.FieldValuesWrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
/**
 * Post to create
 *
 * Put to update value
 *
 * Delete to remove BlendingVSensor from virtualsensornet
 */
@Entity
public class VirtualSensorBlending extends VirtualSensor<BlendingVsnTo> implements Dependent, IBlending {

    //changeable
    @ManyToOne
    private Function function;

    @Enumerated(EnumType.STRING)
    private FunctionOperation callMode;

    //updateable
    @ElementCollection
    private List<BlendingBond> requestFields;

    @ElementCollection
    private List<BlendingBond> responseFields;

    private boolean isAggregated;

    protected VirtualSensorBlending() {
    }

    public VirtualSensorBlending(String label, List<Field> fields) {
        super(VirtualSensorType.BLENDING, label, fields, 0, TimeUnit.MILLISECONDS);
        for (Field field : fields) {
            field.setVirtualSensor(this);
        }
    }

    @Override
    public Function getFunction() {
        return function;
    }

    @Override
    public FunctionOperation getCallMode() {
        return callMode;
    }

    @Override
    public long getCallIntervalInMillis() {
        return super.getCreationInterval();
    }

    @Override
    public List<BlendingBond> getRequestFields() {
        return requestFields;
    }

    @Override
    public List<BlendingBond> getResponseFields() {
        return responseFields;
    }

    //updateable
    @Override
    public List<Field> setRequestFields(List<BlendingBond> requestFields) {
        //group old fields in blending bonds 
        if (requestFields == null) {
            requestFields = new ArrayList<>();
        }

        List<Field> oldFields = null;
        if (this.requestFields != null) {
            oldFields = new ArrayList<>();
            for (BlendingBond requestField : requestFields) {
                Field field = requestField.getField();
                field.removeDependent(this);
                oldFields.add(field);
            }
        }

        //set new fields by blending bonds
        this.requestFields = requestFields;
        List<Field> newFields = new ArrayList<>();
        for (BlendingBond requestField : requestFields) {
            Field field = requestField.getField();
            VirtualSensor sensor = field.getVirtualSensor();
            if (sensor.getId() != this.getId()) {
                field.addDependent(this);
            }
            newFields.add(field);
        }
        update();

        //separate exculded fields from new fields
        if (oldFields == null) {
            return null;
        } else {
            //return excluded fields only to update
            List<Field> tempFields = new ArrayList<>(oldFields);
            for (Field tempField : tempFields) {
                for (Field newField : newFields) {
                    if (newField.getId() == tempField.getId()) {
                        oldFields.remove(tempField);
                        break;
                    }
                }
            }
            return oldFields;
        }
    }

    //updateable
    @Override
    public void setResponseFields(List<BlendingBond> responseFields) {
        this.responseFields = responseFields;
        update();
    }

    @Override
    public List<Field> removeRequestFields() {
        if (requestFields != null) {
            List<Field> fields = new ArrayList<>();
            for (BlendingBond requestField : requestFields) {
                Field field = requestField.getField();
                field.removeDependent(this);
                fields.add(field);
            }
            requestFields.clear();
            update();
            return fields;
        }
        return null;
    }

    @Override
    public void removeResponseFields() {
        if (responseFields != null) {
            responseFields.clear();
            update();
        }
    }

    //changeable
    @Override
    public void setFunction(Function function, FunctionOperation mode) {
        this.function = function;
        this.callMode = mode;
        update();
    }

    //changeable
    @Override
    public void setFunction(Function function) {
        this.function = function;
        update();
    }

    //changeable
    @Override
    public void removeFunction() {
        //TODO: desativar function remover campos bounds
        setFunction(null, null);
        setCallIntervalInMillis(0);
        setRequestFields(null);
        setResponseFields(null);
        update();
    }

    //changeable
    @Override
    public void setCallMode(FunctionOperation callMode) {
        this.callMode = callMode;
        update();
    }

    //changeable
    @Override
    public void setCallIntervalInMillis(long callIntervalInMillis) {
        setCreationInterval(callIntervalInMillis, TimeUnit.MILLISECONDS);
        update();
    }

    @Override
    public boolean isAggregated() {
        return isAggregated;
    }

    @Override
    public VirtualSensor getVirtualSensor() {
        return this;
    }

    @Override
    public boolean setFieldsValues(FieldValuesWrapper valuesWrapper) {
        BlendingValuesWrapper wrapper = (BlendingValuesWrapper) valuesWrapper;
        List<BlendingValuesWrapper.BlendingValue> valueWrappers = wrapper.getValues();
        List<Field> current = getFields();
        boolean isUpdated = false;
        synchronized (current) {
            //update field values
            //response value has only one field, not has array
            boolean isValueUpdated = false;
            for (BlendingBond responseField : responseFields) {
                String paramName = responseField.getName();
                Field field = responseField.getField();
                for (BlendingValuesWrapper.BlendingValue blendingValue : valueWrappers) {
                    if (paramName.equalsIgnoreCase(blendingValue.getName())) {
                        field.setValue(blendingValue.getValue());
                        isValueUpdated = true;
                    }
                }
            }

            if (isValueUpdated) {
                super.setFieldsValues(valuesWrapper);
                isUpdated = true;
            }
        }
        return isUpdated;
    }

    @Override
    public boolean updateFields(FieldListManager listManager) {
        List<Field> fields = getFields();
        synchronized (fields) {
            boolean updated = listManager.doCreateModifyDelete(fields);
            if (updated) {
                update();
            }
            return updated;
        }
    }

    @Override
    public BlendingVsnTo getUniqueTransferObject() {
        //TODO: adicionar function aqui
        BlendingVsnTo blendingVsnTo = new BlendingVsnTo(getId(), getLabel());

        //adding function infos
        if (function != null) {
            blendingVsnTo.setFunction(function.getId());
            blendingVsnTo.setCallMode(callMode);
            blendingVsnTo.setCallIntervalInMillis(getCallIntervalInMillis());

            //adding request fields
            for (BlendingBond requestField : requestFields) {
                Field field = requestField.getField();
                String paramName = requestField.getName();
                long paramFieldId = field.getId();
                blendingVsnTo.addRequestParam(paramFieldId, paramName);
            }
            //adding response fields
            for (BlendingBond responseField : responseFields) {
                Field field = responseField.getField();
                String paramName = responseField.getName();
                long paramFieldId = field.getId();
                blendingVsnTo.addRequestParam(paramFieldId, paramName);
            }
        }

        //adding fields
        List<Field> fields = getFields();
        for (Field field : fields) {
            VirtualSensor sensor = field.getVirtualSensor();
            int aggregates = field.getAggregates().size();
            int dependents = field.getDependents().size();
            blendingVsnTo.createField(field.getId(), field.getReferenceName(), field.getDataTypeId(), field.getConverterId(), field.isStored(), sensor.getId(), aggregates, dependents);
        }
        return blendingVsnTo;
    }

    @Override
    public boolean canRequest() {
        if (function == null || requestFields == null || responseFields == null) {
            if (getModelState() != ModelState.INACTIVE) {
                deactivate();
            }
            return false;
        }

        boolean hasError = false;
        boolean canRequest = false;
        for (BlendingBond requestField : requestFields) {
            Field field = requestField.getField();
            if (isUsableField(field)) {
                canRequest = true;
            } else {
                hasError = true;
            }
        }

        if (!canRequest && getModelState() != ModelState.INACTIVE) {
            deactivate();
        } else if (canRequest && hasError && getModelState() != ModelState.MALFUNCTION && getModelState() != ModelState.INACTIVE) {
            malfunction();
        } else if (!hasError) {
            switch (getModelState()) {
                case INACTIVE:
                    reactivate();
                    break;
                case MALFUNCTION:
                    update();
                    break;
            }
        }

        if (getModelState() != ModelState.INACTIVE) {
            return true;
        }
        return false;
    }

    private boolean isUsableField(Field field) {
        if (field.hasValue()) {
            VirtualSensor vs = field.getVirtualSensor();
            if (vs.getModelState() != ModelState.INACTIVE) {
                return true;
            }
        }
        return false;
    }

    @Override
    public RequestFnTo getFunctionRequest() {

        //grouping values by name
        RequestFnTo requestFnTo = new RequestFnTo();
        Map<String, List<String>> map = new HashMap<>();
        for (BlendingBond requestField : requestFields) {
            Field field = requestField.getField();
            if (isUsableField(field)) {
                String paramName = requestField.getName();
                String paramValue = field.getValue();
                List<String> values;
                if (map.containsKey(paramName)) {
                    values = map.get(paramName);
                } else {
                    values = new ArrayList<>();
                    map.put(paramName, values);
                }
                values.add(paramValue);
            }
        }

        //assigning the values to the function's param name
        List<FunctionParam> requestParams = function.getRequestParams();
        for (FunctionParam requestParam : requestParams) {
            String paramName = requestParam.getName();
            FunctionType functionParam = requestParam.getType();
            boolean isCollection = functionParam.isCollection();
            List<String> values = map.get(paramName);

            if (values == null) {
                continue;
            }

            if (isCollection) {
                requestFnTo.addValue(paramName, values);
            } else {
                String value = values.get(0);
                requestFnTo.addValue(paramName, value);
            }
        }
        return requestFnTo;
    }
}
