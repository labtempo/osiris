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
package br.uff.labtempo.osiris.to.common.base;

import br.uff.labtempo.osiris.to.common.data.ConsumableTo;
import br.uff.labtempo.osiris.to.common.definitions.State;
import br.uff.labtempo.osiris.to.common.data.ValueTo;
import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public abstract class SensorToBase extends ToBaseInfo implements ISensorToBase {

    private long captureTimestampInMillis;
    private long acquisitionTimestampInMillis;
    private int capturePrecisionInNano;

    private Map<String, Integer> consumables;
    private List<Map<String, String>> values;

    //helper attributes
    private transient List<? extends ValueTo> helperValueToList;
    private transient List<? extends ConsumableTo> helperConsumableToList;

    protected SensorToBase(String id, State state, long captureTimestampInMillis, int capturePrecisionInNano, long acquisitionTimestampInMillis) {
        super(id, state);
        this.captureTimestampInMillis = captureTimestampInMillis;
        this.capturePrecisionInNano = capturePrecisionInNano;
        this.acquisitionTimestampInMillis = acquisitionTimestampInMillis;
        this.values = new ArrayList<>();
        this.consumables = new HashMap<>();
    }

    protected void addValue(String name, String value, String unit, String symbol) {
        AbsValueTo valueTo = new AbsValueTo(name, value, unit, symbol);
        values.add(valueTo.toMap());
    }

    protected void addValue(String name, long value, String unit, String symbol) {
        AbsValueTo valueTo = new AbsValueTo(name, value, unit, symbol);
        values.add(valueTo.toMap());
    }

    protected void addValue(String name, double value, String unit, String symbol) {
        AbsValueTo valueTo = new AbsValueTo(name, value, unit, symbol);
        values.add(valueTo.toMap());
    }

    protected void addValue(String name, boolean value, String unit, String symbol) {
        AbsValueTo valueTo = new AbsValueTo(name, value, unit, symbol);
        values.add(valueTo.toMap());
    }

    protected void addValue(String name, ValueType valueType, String value, String unit, String symbol) {
        AbsValueTo valueTo = new AbsValueTo(name, valueType, value, unit, symbol);
        values.add(valueTo.toMap());
    }

    protected void addConsumable(String consumableName, int currentValue) {
        consumables.put(consumableName, currentValue);
    }

    protected Map<String, Integer> getConsumables() {
        return consumables;
    }

    protected List<Map<String, String>> getValues() {
        return values;
    }

    @Override
    public long getCaptureTimestampInMillis() {
        return captureTimestampInMillis;
    }

    @Override
    public long getAcquisitionTimestampInMillis() {
        return acquisitionTimestampInMillis;
    }

    @Override
    public BigInteger getCaptureTimestampInNano() {
        BigInteger millis = BigInteger.valueOf(captureTimestampInMillis);
        BigInteger multiplier = BigInteger.valueOf(1000000L);
        BigInteger nano = BigInteger.valueOf(capturePrecisionInNano);
        BigInteger creationTimestampInNano = millis.multiply(multiplier).add(nano);
        return creationTimestampInNano;
    }

    @Override
    public int getCapturePrecisionInNano() {
        return capturePrecisionInNano;
    }

    @Override
    public List<? extends ValueTo> getValuesTo() {
        if (helperValueToList != null) {
            return helperValueToList;
        }
        List<ValueTo> valuesTo = new ArrayList<>();
        for (Map<String, String> value : values) {
            ValueTo valueTo = new AbsValueTo(value);
            valuesTo.add(valueTo);
        }
        helperValueToList = valuesTo;
        return valuesTo;
    }

    @Override
    public List<? extends ConsumableTo> getConsumablesTo() {
        if (helperConsumableToList != null) {
            return helperConsumableToList;
        }
        List<ConsumableTo> consumablesTo = new ArrayList<>();
        for (Map.Entry<String, Integer> entrySet : consumables.entrySet()) {
            String name = entrySet.getKey();
            Integer value = entrySet.getValue();
            ConsumableTo consumableTo = new AbsConsumables(name, value);
            consumablesTo.add(consumableTo);
        }
        helperConsumableToList = consumablesTo;
        return consumablesTo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SensorToBase other = (SensorToBase) obj;
        if (this.captureTimestampInMillis != other.captureTimestampInMillis) {
            return false;
        }
        if (this.acquisitionTimestampInMillis != other.acquisitionTimestampInMillis) {
            return false;
        }
        if (this.capturePrecisionInNano != other.capturePrecisionInNano) {
            return false;
        }
        if (!Objects.equals(this.consumables, other.consumables)) {
            return false;
        }
        if (!Objects.equals(this.values, other.values)) {
            return false;
        }
        return true;
    }

    private final class AbsValueTo extends ValueTo {

        public AbsValueTo(String name, ValueType valueType, String value, String unit, String symbol) {
            super(name, valueType, value, unit, symbol);
        }

        public AbsValueTo(String name, String value, String unit, String symbol) {
            super(name, value, unit, symbol);
        }

        public AbsValueTo(String name, boolean value, String unit, String symbol) {
            super(name, value, unit, symbol);
        }

        public AbsValueTo(String name, double value, String unit, String symbol) {
            super(name, value, unit, symbol);
        }

        public AbsValueTo(String name, long value, String unit, String symbol) {
            super(name, value, unit, symbol);
        }

        public AbsValueTo(Map<String, String> map) {
            super(map);
        }

        @Override
        protected Map<String, String> toMap() {
            return super.toMap();
        }

    }

    private final class AbsConsumables extends ConsumableTo {

        public AbsConsumables(String name, int value) {
            super(name, value);
        }
    }
}
