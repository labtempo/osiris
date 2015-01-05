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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class LinkVsnTo {

    private long id;
    private final String sensorId;
    private final String collectorId;
    private final String networkId;
    private List<Map<String, String>> fields;

    //helper attributes
    private transient List<? extends FieldTo> helperFieldToList;

    public LinkVsnTo(String sensorId, String collectorId, String networkId) {
        this.sensorId = sensorId;
        this.collectorId = collectorId;
        this.networkId = networkId;
    }

    public void createField(String name, long dataTypeId, long converterId) {
        if (fields == null) {
            fields = new ArrayList<>();
        }
        InternalFieldTo fieldTo = new InternalFieldTo(name, dataTypeId, converterId);
        fields.add(fieldTo.toMap());
    }

    public void createField(String name, long dataTypeId) {
        createField(name, dataTypeId, 0);
    }

    public long getId() {
        return id;
    }

    public String getSensorId() {
        return sensorId;
    }

    public String getCollectorId() {
        return collectorId;
    }

    public String getNetworkId() {
        return networkId;
    }

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

    private class InternalFieldTo extends FieldTo{

        InternalFieldTo(Map<String, String> map) {
            super(map);
        }

        InternalFieldTo(String name, long dataTypeId, long converterId) {
            super(name, dataTypeId, converterId);
        }

        @Override
        protected Map<String, String> toMap() {
            return super.toMap(); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
