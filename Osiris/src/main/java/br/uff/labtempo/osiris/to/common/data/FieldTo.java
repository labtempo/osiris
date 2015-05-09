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
package br.uff.labtempo.osiris.to.common.data;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public abstract class FieldTo {

    private final long id;
    private final String name;
    private final long dataTypeId;
    private final long converterId;
    private final boolean initialized;
    private final long sourceId;
    private final int aggregates;
    private final int dependents;

    private final transient String FIELD_ID = "id";
    private final transient String FIELD_NAME = "name";
    private final transient String FIELD_DATATYPE_ID = "datatypeid";
    private final transient String FIELD_CONVERTER_ID = "converterid";
    private final transient String FIELD_INITIALIZED = "initialized";
    private final transient String FIELD_SOURCE_ID = "sourceid";
    private final transient String FIELD_AGGREGATES = "aggregates";
    private final transient String FIELD_DEPENDENTS = "dependents";

    protected FieldTo(Map<String, String> map) {
        this.id = Long.valueOf(map.get(FIELD_ID));
        this.name = map.get(FIELD_NAME);
        this.dataTypeId = Long.valueOf(map.get(FIELD_DATATYPE_ID));
        this.converterId = Long.valueOf(map.get(FIELD_CONVERTER_ID));
        this.initialized = Boolean.valueOf(map.get(FIELD_INITIALIZED));
        this.sourceId = Long.valueOf(map.get(FIELD_SOURCE_ID));
        this.aggregates = Integer.valueOf(map.get(FIELD_AGGREGATES));
        this.dependents = Integer.valueOf(map.get(FIELD_DEPENDENTS));
    }

    protected FieldTo(String name, long dataTypeId, long converterId) {
        this(0, name, dataTypeId, converterId, false, 0, 0, 0);
    }

    protected FieldTo(long id, String name, long dataTypeId, long converterId) {
        this(id, name, dataTypeId, converterId, false, 0, 0, 0);
    }

    protected FieldTo(long id, String name, long dataTypeId, long converterId, boolean initialized, long sourceId, int aggregates, int dependents) {
        this.id = id;
        this.name = name;
        this.dataTypeId = dataTypeId;
        this.converterId = converterId;
        this.initialized = initialized;
        this.sourceId = sourceId;
        this.aggregates = aggregates;
        this.dependents = dependents;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getDataTypeId() {
        return dataTypeId;
    }

    public long getConverterId() {
        return converterId;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public long getSourceId() {
        return sourceId;
    }

    public int getAggregates() {
        return aggregates;
    }

    public int getDependents() {
        return dependents;
    }

    protected Map<String, String> toMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put(FIELD_ID, String.valueOf(id));
        map.put(FIELD_NAME, name);
        map.put(FIELD_DATATYPE_ID, String.valueOf(dataTypeId));
        map.put(FIELD_CONVERTER_ID, String.valueOf(converterId));
        map.put(FIELD_INITIALIZED, String.valueOf(initialized));
        map.put(FIELD_SOURCE_ID, String.valueOf(sourceId));
        map.put(FIELD_AGGREGATES, String.valueOf(aggregates));
        map.put(FIELD_DEPENDENTS, String.valueOf(dependents));
        return map;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.name);
        hash = 59 * hash + (int) (this.dataTypeId ^ (this.dataTypeId >>> 32));
        hash = 59 * hash + (int) (this.converterId ^ (this.converterId >>> 32));
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
        final FieldTo other = (FieldTo) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (this.dataTypeId != other.dataTypeId) {
            return false;
        }
        if (this.converterId != other.converterId) {
            return false;
        }
        return true;
    }

}
