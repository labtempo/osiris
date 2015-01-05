/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.virtualsensornet.model.util;

import br.uff.labtempo.osiris.to.collector.CollectorCoTo;
import br.uff.labtempo.osiris.to.collector.NetworkCoTo;
import br.uff.labtempo.osiris.to.collector.SampleCoTo;
import br.uff.labtempo.osiris.to.collector.SensorCoTo;
import br.uff.labtempo.osiris.to.common.data.ValueTo;
import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import br.uff.labtempo.osiris.virtualsensornet.model.DataType;
import br.uff.labtempo.osiris.virtualsensornet.model.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Felipe
 */
public class SensorCoWrapper {

    private final String id;
    private final String networkId;
    private final String collectorId;
    private final long timestamp;

    private List<Field> fields;
    private Set<String> fieldNames;
    

    public SensorCoWrapper(SampleCoTo sample) {
        SensorCoTo sensor = sample.getSensor();
        CollectorCoTo collector = sample.getCollector();
        NetworkCoTo network = sample.getNetwork();

        this.id = sensor.getId();
        this.collectorId = collector.getId();
        this.networkId = network.getId();
        
        this.timestamp = sensor.getTimestamp();

        defineFields(sensor);
    }

    public String getSensorId() {
        return id;
    }

    public String getNetworkId() {
        return networkId;
    }

    public String getCollectorId() {
        return collectorId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public List<Field> getFields() {
        return fields;
    }

    public boolean hasField(String fieldName) {
        return fieldNames.contains(fieldName);
    }

    private void defineFields(SensorCoTo sensor) {
        fields = new ArrayList<>();
        fieldNames = new HashSet<>();
        for (ValueTo valueTo : sensor.getValuesTo()) {
            Field field = convertSensorCoValueToField(valueTo);
            fields.add(field);
            fieldNames.add(field.getName());
        }
    }

    private Field convertSensorCoValueToField(ValueTo valueTo) {
        String name = valueTo.getName();
        ValueType type = valueTo.getType();
        String value = valueTo.getValue();
        String unit = valueTo.getUnit();
        String symbol = valueTo.getSymbol();

        DataType dataType = new DataType(type, unit, symbol);

        Field field = new Field(name, dataType);

        field.addInstantaneousValue(value);

        return field;
    }

}
