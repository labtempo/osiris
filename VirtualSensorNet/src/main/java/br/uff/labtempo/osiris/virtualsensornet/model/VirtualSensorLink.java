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

import br.uff.labtempo.osiris.to.virtualsensornet.LinkVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.VirtualSensorType;
import br.uff.labtempo.osiris.virtualsensornet.model.util.SensorCoToWrapper;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
        super(VirtualSensorType.LINK, fields, 0, TimeUnit.MILLISECONDS);
        this.networkId = networkId;
        this.collectorId = collectorId;
        this.sensorId = sensorId;
    }

    @Override
    public boolean addField(Field field) {
        return super.addField(field);
    }

    @Override
    public boolean removeField(Field field) {
        return super.removeField(field);
    }

    @Override
    public void deactivate() {
        super.deactivate();
    }

    public boolean updateVirtualSensorDataFromCollectorData(SensorCoToWrapper sensorWrapper) {
        boolean isUpdated = false;
        if (isSource(sensorWrapper)) {
            List<Field> sensorCoFields = sensorWrapper.getFields();
            if (updateInterval(sensorWrapper.getInterval(), sensorWrapper.getIntervalTimeUnit())) {
                isUpdated = true;
            }
            if (addNewValues(sensorCoFields, sensorWrapper.getTimestampInMillis())) {
                isUpdated = true;
            }
        }
        return isUpdated;
    }

    @Override
    public synchronized boolean upgradeFields(List<Field> newFields) {        
        return super.upgradeFields(newFields);
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

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
        update();
    }

    public void setCollectorId(String collectorId) {
        this.collectorId = collectorId;
        update();
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
        update();
    }

    public boolean isSource(SensorCoToWrapper sensor) {
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
            if (!sensor.hasField(superField.getReferenceName())) {
                return false;
            }
        }

        return true;
    }

    public LinkVsnTo getLinkTransferObject() {        
        LinkVsnTo linkVsnTo = new LinkVsnTo(getId(), sensorId, collectorId, networkId);
        List<Field> fields = getFields();
        for (Field field : fields) {
            linkVsnTo.createField(field.getId(), field.getReferenceName(), field.getDataTypeId(), field.getConverterId(), field.isStored());
        }
        return linkVsnTo;
    }
}
