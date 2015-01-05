/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.to.virtualsensornet;

import br.uff.labtempo.osiris.to.common.data.ValueTo;
import br.uff.labtempo.osiris.to.common.definitions.State;
import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public class VirtualSensorVsnTo {

    private long id;

    private String state;

    private long timestamp;
    
    private long lastModified;

    private List<Map<String, String>> values;

    private String sensorType;

    //helper attributes
    private transient State helperState;
    private transient Calendar helperLastModified;
    private transient VirtualSensorType helperVirtualSensorType;
    private transient List<? extends ValueTo> helperValueToList;

    public VirtualSensorVsnTo(long id, State state, long captureTimestamp, Calendar lastModified, VirtualSensorType sensorType) {
        this.id = id;
        this.state = state.toString();
        this.timestamp = captureTimestamp;
        this.lastModified = lastModified.getTimeInMillis();
        this.sensorType = sensorType.toString();
        this.values = new ArrayList<>();

        this.helperState = state;
        this.helperLastModified = lastModified;
        this.helperVirtualSensorType = sensorType;
    }

    public long getId() {
        return id;
    }

    public State getState() {
        if (helperState == null) {
            helperState = Enum.valueOf(State.class, state);
        }
        return helperState;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Calendar getLastModified() {
        if (helperLastModified == null) {
            helperLastModified = Calendar.getInstance();
            helperLastModified.setTimeInMillis(lastModified);
        }
        return helperLastModified;
    }

    public VirtualSensorType getSensorType() {
        if (helperVirtualSensorType == null) {
            helperVirtualSensorType = Enum.valueOf(VirtualSensorType.class, sensorType);
        }
        return helperVirtualSensorType;
    }

    public void addValue(String name, ValueType valueType, String value, String unit, String symbol) {
        InternalValueTo valueTo = new InternalValueTo(name, valueType, value, unit, symbol);
        values.add(valueTo.toMap());
    }

    public List<? extends ValueTo> getValuesTo() {
        if (helperValueToList != null) {
            return helperValueToList;
        }
        List<InternalValueTo> valuesTo = new ArrayList<>();
        for (Map<String, String> value : values) {
            InternalValueTo valueTo = new InternalValueTo(value);
            valuesTo.add(valueTo);
        }
        helperValueToList = valuesTo;
        return valuesTo;
    }

    private class InternalValueTo extends ValueTo {

        public InternalValueTo(String name, ValueType valueType, String value, String unit, String symbol) {
            super(name, valueType, value, unit, symbol);
        }

        InternalValueTo(Map<String, String> map) {
            super(map);
        }

        @Override
        public Map<String, String> toMap() {
            return super.toMap();
        }
    }
}
