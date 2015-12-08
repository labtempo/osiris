/*
 * Copyright 2015 Felipe Santos <feliperalph at hotmail.com>.
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
package br.uff.labtempo.osiris.to.virtualsensornet;

import br.uff.labtempo.omcp.common.utils.DateUtil;
import br.uff.labtempo.osiris.to.common.data.FieldTo;
import br.uff.labtempo.osiris.to.common.definitions.FunctionOperation;
import br.uff.labtempo.osiris.to.virtualsensornet.interfaces.IBlendingVsnTo;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class BlendingVsnTo implements IBlendingVsnTo {
    //function id
    //function mode
    //lista de parametros(fields) com base na function
    //tempo de ativação

    private final long id;
    private String label;
    private long functionId;
    private String callMode;
    private long callIntervalInMillis;
    private List<Map<String, String>> fields;
    private List<Map<String, String>> requestParams;
    private List<Map<String, String>> responseParams;

    public BlendingVsnTo(long id, String label) {
        this.id = id;
        this.label = label;
        this.fields = new ArrayList<>();
        this.requestParams = new ArrayList<>();
        this.responseParams = new ArrayList<>();
    }

    public BlendingVsnTo(String label) {
        this(0, label);
    }

    public BlendingVsnTo() {
        this("");
        DateUtil dateUtil = new DateUtil();
        this.label = "blending-" + dateUtil.generate(Calendar.getInstance());
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public void createField(String name, long dataTypeId, long converterId) {
        InternalFieldTo fieldTo = new InternalFieldTo(name, dataTypeId, converterId);
        createField(fieldTo);
    }

    @Override
    public void createField(long id, String name, long dataTypeId, long converterId) {
        InternalFieldTo fieldTo = new InternalFieldTo(id, name, dataTypeId, converterId);
        createField(fieldTo);
    }

    @Override
    public void createField(long id, String name, long dataTypeId, long converterId, boolean initialized, long sourceId, int aggregates, int dependents) {
        InternalFieldTo fieldTo = new InternalFieldTo(id, name, dataTypeId, converterId, initialized, sourceId, aggregates, dependents);
        createField(fieldTo);
    }

    @Override
    public void createField(String name, long dataTypeId) {
        createField(name, dataTypeId, 0);
    }

    @Override
    public void createField(long id, String name, long dataTypeId) {
        createField(id, name, dataTypeId, 0);
    }

    private void createField(InternalFieldTo fieldTo) {
        fields.add(fieldTo.toMap());
    }

    @Override
    public void removeFields() {
        fields.clear();
    }

    @Override
    public List<? extends FieldTo> getFields() {
        List<InternalFieldTo> fieldTos = new ArrayList<>();
        for (Map<String, String> field : fields) {
            fieldTos.add(new InternalFieldTo(field));
        }
        return fieldTos;
    }

    //edit
    @Override
    public void setFunction(long functionId) {
        this.functionId = functionId;
    }

    @Override
    public void setFunction(FunctionVsnTo function) {
        if (function != null) {
            this.functionId = function.getId();
        }
    }

    @Override
    public long getFunctionId() {
        return functionId;
    }

    @Override
    public void setCallMode(FunctionOperation operation) {
        if (operation != null) {
            this.callMode = operation.toString();
        }
    }

    @Override
    public FunctionOperation getCallMode() {
        return FunctionOperation.getByMode(callMode);
    }

    @Override
    public void setCallIntervalInMillis(long callIntervalInMillis) {
        this.callIntervalInMillis = callIntervalInMillis;
    }

    @Override
    public long getCallIntervalInMillis() {
        return callIntervalInMillis;
    }

    @Override
    public void addResponseParam(long fieldId, String responseParamName) {
        BlendingBondVsnTo bondVsnTo = new BlendingBondVsnTo(responseParamName, fieldId);
        addParams(bondVsnTo, responseParams);
    }

    @Override
    public void addRequestParam(long fieldId, String requestParamName) {
        BlendingBondVsnTo bondVsnTo = new BlendingBondVsnTo(requestParamName, fieldId);
        addParams(bondVsnTo, requestParams);
    }

    private void addParams(BlendingBondVsnTo bondVsnTo, List<Map<String, String>> mapList) {
        mapList.add(bondVsnTo.toMap());
    }

    @Override
    public void removeResponseParam(long fieldId) {
        removeParams(fieldId, responseParams);
    }

    @Override
    public void removeRequestParam(long fieldId) {
        removeParams(fieldId, requestParams);
    }

    private void removeParams(long fieldId, List<Map<String, String>> mapList) {
        List<Map<String, String>> tempMapList = new ArrayList<>(mapList);
        for (Map<String, String> map : tempMapList) {
            BlendingBondVsnTo bondVsnTo = new BlendingBondVsnTo(map);
            if (bondVsnTo.getFieldId() == fieldId) {
                mapList.remove(map);
            }
        }
    }

    @Override
    public List<BlendingBondVsnTo> getResponseParams() {
        return getParams(responseParams);
    }

    @Override
    public List<BlendingBondVsnTo> getRequestParams() {
        return getParams(requestParams);
    }

    private List<BlendingBondVsnTo> getParams(List<Map<String, String>> mapList) {
        List<BlendingBondVsnTo> bondVsnTos = new ArrayList<>();
        for (Map<String, String> map : mapList) {
            bondVsnTos.add(new BlendingBondVsnTo(map));
        }
        return bondVsnTos;
    }

    private class InternalFieldTo extends FieldTo {

        InternalFieldTo(Map<String, String> map) {
            super(map);
        }

        InternalFieldTo(String name, long dataTypeId, long converterId) {
            super(name, dataTypeId, converterId);
        }

        InternalFieldTo(long id, String name, long dataTypeId, long converterId) {
            super(id, name, dataTypeId, converterId);
        }

        InternalFieldTo(long id, String name, long dataTypeId, long converterId, boolean initialized, long sourceId, int aggregates, int dependents) {
            super(id, name, dataTypeId, converterId, initialized, sourceId, aggregates, dependents);
        }

        @Override
        protected Map<String, String> toMap() {
            return super.toMap(); //To change body of generated methods, choose Tools | Templates.
        }
    }

}
