/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.to.common.data;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Felipe
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
