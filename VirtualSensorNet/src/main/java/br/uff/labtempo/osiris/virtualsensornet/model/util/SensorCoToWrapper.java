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
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class SensorCoToWrapper {

    private final String id;
    private final String networkId;
    private final String collectorId;
    private final long captureTimestampInMillis;
    private final int capturePrecisionInNano;
    private final long acquisitionTimestampInMillis;

    private List<Field> fields;
    private Set<String> fieldNames;

    private final long captureInterval;
    private final TimeUnit captureIntervalTimeUnit;

    public SensorCoToWrapper(SampleCoTo sample) {
        SensorCoTo sensor = sample.getSensor();
        CollectorCoTo collector = sample.getCollector();
        NetworkCoTo network = sample.getNetwork();

        this.id = sensor.getId().trim();
        this.collectorId = collector.getId().trim();
        this.networkId = network.getId().trim();

        this.captureTimestampInMillis = sensor.getCaptureTimestampInMillis();
        this.capturePrecisionInNano = sensor.getCapturePrecisionInNano();
        this.acquisitionTimestampInMillis = sensor.getAcquisitionTimestampInMillis();
        this.captureInterval = collector.getCaptureInterval();
        this.captureIntervalTimeUnit = collector.getCaptureIntervalTimeUnit();

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

    public long getCaptureTimestampInMillis() {
        return captureTimestampInMillis;
    }

    public int getCapturePrecisionInNano() {
        return capturePrecisionInNano;
    }

    public long getAcquisitionTimestampInMillis() {
        return acquisitionTimestampInMillis;
    }    
  
    public long getCaptureInterval() {
        return captureInterval;
    }

    public TimeUnit getCaptureIntervalTimeUnit() {
        return captureIntervalTimeUnit;
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
            fieldNames.add(field.getReferenceName());
        }
    }

    private Field convertSensorCoValueToField(ValueTo valueTo) {
        String name = valueTo.getName();
        ValueType type = valueTo.getType();
        String value = valueTo.getValue();
        String unit = valueTo.getUnit();
        String symbol = valueTo.getSymbol();

        DataType dataType = new DataType(name, type, unit, symbol);

        Field field = new Field(name, dataType);

        field.setValue(value);

        return field;
    }

}
