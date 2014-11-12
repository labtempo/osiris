/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.virtualsensornet.model;

import br.uff.labtempo.osiris.collector.to.SensorCoTo;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe
 */
/**
 * Post to create
 *
 * Notify to update value
 *
 * Delete to remove BlendingVSensor from virtualsensornet
 *
 */
public class SensorLink extends VSensor {

    private String networkId;
    private String collectorId;
    private String sensorId;
    private String url;

    private transient boolean isNew;

    public SensorLink(String networkId, String collectorId, String sensorId, String url, String[] fields) {
        this.networkId = networkId;
        this.collectorId = collectorId;
        this.sensorId = sensorId;
        this.url = url;
        this.isNew = true;
        createFields(fields);
    }

    public void update(SensorCoTo sensorTo) {
        if (isNew) {
            doInsert(sensorTo);
        } else {
            doUpdate(sensorTo);
        }
        Calendar date = getFields().get(getFields().size() - 1).getLastModified();
        setLastModified(date);
    }

    private void doInsert(SensorCoTo sensorTo) {
        int count = 0;
        for (Field field : getFields()) {
            for (Map<String, String> map : sensorTo.getValues()) {
                if (map != null) {
                    if (map.containsKey("name")
                            && map.containsKey("value")
                            && map.containsKey("type")
                            && map.containsKey("unit")
                            && map.containsKey("symbol")
                            && map.get("name").equals(field.getName())) {

                        field.setValue(map.get("value"));
                        field.setType(map.get("type"));
                        field.setValue(map.get("value"));
                        field.setUnit(map.get("unit"));
                        field.setSymbol(map.get("symbol"));

                        count++;
                        break;
                    }
                }
            }
        }
        if (getFields().size() == count) {
            isNew = false;
        }
    }

    private void doUpdate(SensorCoTo sensorTo) {

        List<Field> newerFields = new ArrayList<>();

        for (Map<String, String> map : sensorTo.getValues()) {
            if (map != null) {
                if (map.containsKey("name")
                        && map.containsKey("value")
                        && map.containsKey("type")
                        && map.containsKey("unit")
                        && map.containsKey("symbol")) {

                    Field field = new Field(
                            map.get("name"),
                            map.get("type"),
                            map.get("value"),
                            map.get("unit"),
                            map.get("symbol")
                    );
                    newerFields.add(field);
                }
            }
        }

        StringBuilder sb = new StringBuilder();

        for (Field newerField : newerFields) {
            for (Field field : getFields()) {
                if (field.getName().equals(newerField.getName())) {
                    field.setValue(newerField.getValue());
                    if (!field.equalsMetadata(newerField)) {
                        if (sb.length() > 0) {
                            sb.append(", ");
                        }
                        sb.append(field.getName());
                    }
                    break;
                }
            }
        }

        if (sb.length() > 0) {
            throw new RuntimeException("Sensor fields are not equals: " + sb.toString());
        }
    }

    private void createFields(String[] fields) {
        for (String fieldName : fields) {
            super.addField(new Field(fieldName));
        }
    }

}
