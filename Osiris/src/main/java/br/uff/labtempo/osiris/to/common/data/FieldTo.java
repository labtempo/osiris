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

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public abstract class FieldTo {

    private final String name;
    private final long dataTypeId;
    private final long converterId;

    private final transient String FIELD_NAME = "fieldname";
    private final transient String FIELD_DATATYPE_ID = "datatypeid";
    private final transient String FIELD_CONVERTER_ID = "converterid";

    protected FieldTo(Map<String, String> map) {
        this.name = map.get(FIELD_NAME);
        this.dataTypeId = Long.valueOf(map.get(FIELD_DATATYPE_ID));
        this.converterId = Long.valueOf(map.get(FIELD_CONVERTER_ID));
    }

    protected FieldTo(String name, long dataTypeId, long converterId) {
        this.name = name;
        this.dataTypeId = dataTypeId;
        this.converterId = converterId;
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

    protected Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put(FIELD_NAME, name);
        map.put(FIELD_DATATYPE_ID, String.valueOf(dataTypeId));
        map.put(FIELD_CONVERTER_ID, String.valueOf(converterId));
        return map;
    }
}
