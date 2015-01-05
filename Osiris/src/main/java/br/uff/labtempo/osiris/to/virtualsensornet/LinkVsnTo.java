/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.to.virtualsensornet;

import br.uff.labtempo.osiris.to.common.data.FieldTo;
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
