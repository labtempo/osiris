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

import br.uff.labtempo.osiris.to.virtualsensornet.interfaces.IVirtualSensorVsnTo;
import br.uff.labtempo.osiris.to.common.definitions.State;
import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class VirtualSensorVsnTo implements IVirtualSensorVsnTo {

    private long id;

    private String state;

    private long creationTimestampInMillis;
    private int creationPrecisionInNano;
    private long acquisitionTimestampInMillis;
    private long storageTimestampInMillis;
    private long creationInterval;
    private String creationIntervalTimeUnit;
    private long lastModified;

    private List<Map<String, String>> values;

    private String sensorType;

    //helper attributes
    private transient State helperState;
    private transient Calendar helperLastModified;
    private transient TimeUnit helperCreationIntervalTimeUnit;
    private transient VirtualSensorType helperVirtualSensorType;
    private transient List<ValueVsnTo> helperValueToList;

    public VirtualSensorVsnTo(long id, State state, long creationTimestampInMillis, int creationPrecisionInNano, long creationInterval, TimeUnit creationIntervalTimeUnit, long acquisitionTimestampInMillis, long storageTimestampInMillis, Calendar lastModified, VirtualSensorType sensorType) {
        this.id = id;
        this.state = state.toString();
        this.creationTimestampInMillis = creationTimestampInMillis;
        this.creationPrecisionInNano = creationPrecisionInNano;
        this.creationInterval = creationInterval;
        this.creationIntervalTimeUnit = creationIntervalTimeUnit.toString();
        this.acquisitionTimestampInMillis = acquisitionTimestampInMillis;
        this.storageTimestampInMillis = storageTimestampInMillis;
        this.lastModified = lastModified.getTimeInMillis();
        this.sensorType = sensorType.toString();
        this.values = new ArrayList<>();

        this.helperState = state;
        this.helperCreationIntervalTimeUnit = creationIntervalTimeUnit;
        this.helperLastModified = lastModified;
        this.helperVirtualSensorType = sensorType;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public State getState() {
        if (helperState == null) {
            helperState = Enum.valueOf(State.class, state.toUpperCase());
        }
        return helperState;
    }

    @Override
    public long getCreationTimestampInMillis() {
        return creationTimestampInMillis;
    }

    @Override
    public long getAcquisitionTimestampInMillis() {
        return acquisitionTimestampInMillis;
    }

    @Override
    public long getStorageTimestampInMillis() {
        return storageTimestampInMillis;
    }

    @Override
    public BigInteger getCreationTimestampInNano() {
        BigInteger millis = BigInteger.valueOf(creationTimestampInMillis);
        BigInteger multiplier = BigInteger.valueOf(1000000L);
        BigInteger nano = BigInteger.valueOf(creationPrecisionInNano);
        BigInteger creationTimestampInNano = millis.multiply(multiplier).add(nano);
        return creationTimestampInNano;
    }

    @Override
    public long getCreationInterval() {
        return this.creationInterval;
    }

    @Override
    public TimeUnit getCreationIntervalTimeUnit() {
        if (helperCreationIntervalTimeUnit == null) {
            helperCreationIntervalTimeUnit = Enum.valueOf(TimeUnit.class, creationIntervalTimeUnit.toUpperCase());
        }
        return helperCreationIntervalTimeUnit;
    }

    @Override
    public int getCreationPrecisionInNano() {
        return creationPrecisionInNano;
    }

    @Override
    public Calendar getLastModified() {
        if (helperLastModified == null) {
            helperLastModified = Calendar.getInstance();
            helperLastModified.setTimeInMillis(lastModified);
        }
        return helperLastModified;
    }

    @Override
    public VirtualSensorType getSensorType() {
        if (helperVirtualSensorType == null) {
            helperVirtualSensorType = Enum.valueOf(VirtualSensorType.class, sensorType.toUpperCase());
        }
        return helperVirtualSensorType;
    }

    public void addValue(long id, String name, ValueType valueType, String value, String unit, String symbol) {
        ValueVsnTo valueTo = new ValueVsnTo(id, name, valueType, value, unit, symbol);
        values.add(valueTo.toMap());
    }

    @Override
    public List<ValueVsnTo> getValuesTo() {
        if (helperValueToList != null) {
            return helperValueToList;
        }
        List<ValueVsnTo> valuesTo = new ArrayList<>();
        for (Map<String, String> value : values) {
            ValueVsnTo valueTo = new ValueVsnTo(value);
            valuesTo.add(valueTo);
        }
        helperValueToList = valuesTo;
        return valuesTo;
    }
}
