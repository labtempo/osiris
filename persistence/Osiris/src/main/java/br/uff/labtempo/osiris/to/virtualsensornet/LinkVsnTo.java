/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.to.virtualsensornet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public class LinkVsnTo {

    private long id;
    private final String sensorId;
    private final String collectorId;
    private final String networkId;
    private List<Map<String, String>> fields;

    //helper attributes
    private transient List<FieldTo> helperFieldToList;

    public LinkVsnTo(String sensorId, String collectorId, String networkId) {
        this.sensorId = sensorId;
        this.collectorId = collectorId;
        this.networkId = networkId;
    }

    public void createField(String name, long dataTypeId, long converterId) {
        if (fields == null) {
            fields = new ArrayList<>();
        }
        FieldTo fieldTo = new FieldTo(name, dataTypeId, converterId);
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

    public List<FieldTo> getFields() {
        if (helperFieldToList != null) {
            return helperFieldToList;
        }
        List<FieldTo> fieldTos = new ArrayList<>();
        for (Map<String, String> field : fields) {
            fieldTos.add(new FieldTo(field));
        }
        helperFieldToList = fieldTos;
        return fieldTos;
    }

    public class FieldTo {

        private final String name;
        private final long dataTypeId;
        private final long converterId;

        private final transient String FIELD_NAME = "fieldname";
        private final transient String FIELD_DATATYPE_ID = "datatypeid";
        private final transient String FIELD_CONVERTER_ID = "converterid";

        FieldTo(Map<String, String> map) {
            this.name = map.get(FIELD_NAME);
            this.dataTypeId = Long.valueOf(map.get(FIELD_DATATYPE_ID));
            this.converterId = Long.valueOf(map.get(FIELD_CONVERTER_ID));
        }

        public FieldTo(String name, long dataTypeId, long converterId) {
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

        Map<String, String> toMap() {
            Map<String, String> map = new HashMap<>();
            map.put(FIELD_NAME, name);
            map.put(FIELD_DATATYPE_ID, String.valueOf(dataTypeId));
            map.put(FIELD_CONVERTER_ID, String.valueOf(converterId));
            return map;
        }
    }
}
