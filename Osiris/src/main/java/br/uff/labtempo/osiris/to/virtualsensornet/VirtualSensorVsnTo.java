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

import br.uff.labtempo.osiris.to.common.data.ValueTo;
import br.uff.labtempo.osiris.to.common.definitions.State;
import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
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
