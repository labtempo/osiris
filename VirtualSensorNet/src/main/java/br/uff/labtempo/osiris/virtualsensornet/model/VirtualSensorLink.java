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
package br.uff.labtempo.osiris.virtualsensornet.model;

import br.uff.labtempo.osiris.to.virtualsensornet.VirtualSensorType;
import br.uff.labtempo.osiris.virtualsensornet.model.util.SensorCoWrapper;
import java.util.List;
import javax.persistence.Entity;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
/**
 * Post to create
 *
 * Notify to update value
 *
 * Delete to remove BlendingVSensor from virtualsensornet
 *
 */
@Entity
public class VirtualSensorLink extends VirtualSensor {

    private String networkId;
    private String collectorId;
    private String sensorId;

    public VirtualSensorLink() {
    }

    public VirtualSensorLink(String networkId, String collectorId, String sensorId, List<Field> fields) {
        super(VirtualSensorType.LINK, fields);
        this.networkId = networkId;
        this.collectorId = collectorId;
        this.sensorId = sensorId;
    }

    @Override
    public void addField(Field field) {
        super.addField(field);
    }

    @Override
    public boolean removeField(Field field) {
        return super.removeField(field);
    }

    @Override
    public void deactivate() {
        super.deactivate();
    }

    public void update(SensorCoWrapper sensorWrapper) {
        boolean isUpdated = false;
        if (isSource(sensorWrapper)) {
            List<Field> sensorCoFields = sensorWrapper.getFields();
            for (Field sensorCoField : sensorCoFields) {
                isUpdated = updateFields(sensorCoField.getName(), sensorCoField.getValue(), sensorCoField.getDefaultDataType());
            }
        }

        if (isUpdated) {
            setTimestamp(sensorWrapper.getTimestamp());
            update();
        }
    }

    public String getNetworkId() {
        return networkId;
    }

    public String getCollectorId() {
        return collectorId;
    }

    public String getSensorId() {
        return sensorId;
    }

    public boolean isSource(SensorCoWrapper sensor) {
        if (!sensorId.equalsIgnoreCase(sensor.getSensorId())) {
            return false;
        }
        if (!networkId.equalsIgnoreCase(sensor.getNetworkId())) {
            return false;
        }
        if (!collectorId.equalsIgnoreCase(sensor.getCollectorId())) {
            return false;
        }

        List<Field> superFields = getFields();

        for (Field superField : superFields) {
            if (!sensor.hasField(superField.getName())) {
                return false;
            }
        }

        return true;
    }
}
