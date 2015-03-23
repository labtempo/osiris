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
package br.uff.labtempo.osiris.to.virtualsensornet;

import br.uff.labtempo.osiris.to.common.data.FieldTo;
import br.uff.labtempo.osiris.to.virtualsensornet.interfaces.ILinkVsnTo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class LinkVsnTo implements ILinkVsnTo {

    private long id;
    private final String sensorId;
    private final String collectorId;
    private final String networkId;
    private List<Map<String, String>> fields;

    //helper attributes
    private transient List<? extends FieldTo> helperFieldToList;

    public LinkVsnTo(long id, String sensorId, String collectorId, String networkId) {
        this.id = id;
        this.sensorId = sensorId;
        this.collectorId = collectorId;
        this.networkId = networkId;

        this.fields = new ArrayList<>();
    }

    public LinkVsnTo(String sensorId, String collectorId, String networkId) {
        this(0, sensorId, collectorId, networkId);
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
    public void createField(long id, String name, long dataTypeId, long converterId, boolean initialized, long sourceId, int aggregates) {
        InternalFieldTo fieldTo = new InternalFieldTo(id, name, dataTypeId, converterId, initialized, sourceId, aggregates);
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
    public long getId() {
        return id;
    }

    @Override
    public String getSensorId() {
        return sensorId;
    }

    @Override
    public String getCollectorId() {
        return collectorId;
    }

    @Override
    public String getNetworkId() {
        return networkId;
    }

    @Override
    public List<? extends FieldTo> getFields() {
        if (helperFieldToList != null) {
            return helperFieldToList;
        }
        List<InternalFieldTo> fieldTos = new ArrayList<>();
        for (Map<String, String> field : fields) {
            fieldTos.add(new InternalFieldTo(field));
        }
        helperFieldToList = fieldTos;
        return fieldTos;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.sensorId);
        hash = 29 * hash + Objects.hashCode(this.collectorId);
        hash = 29 * hash + Objects.hashCode(this.networkId);
        hash = 29 * hash + Objects.hashCode(this.fields);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LinkVsnTo other = (LinkVsnTo) obj;
        if (!Objects.equals(this.sensorId, other.sensorId)) {
            return false;
        }
        if (!Objects.equals(this.collectorId, other.collectorId)) {
            return false;
        }
        if (!Objects.equals(this.networkId, other.networkId)) {
            return false;
        }
        if (!Objects.equals(this.getFields(), other.getFields())) {
            return false;
        }
        return true;
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

        InternalFieldTo(long id, String name, long dataTypeId, long converterId, boolean initialized, long sourceId, int aggregates) {
            super(id, name, dataTypeId, converterId, initialized, sourceId, aggregates);
        }

        @Override
        protected Map<String, String> toMap() {
            return super.toMap(); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
