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

import br.uff.labtempo.osiris.virtualsensornet.model.interfaces.ILink;
import br.uff.labtempo.osiris.to.virtualsensornet.LinkVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.VirtualSensorType;
import br.uff.labtempo.osiris.virtualsensornet.model.util.FieldListManager;
import br.uff.labtempo.osiris.virtualsensornet.model.util.LinkValuesWrapper;
import br.uff.labtempo.osiris.virtualsensornet.model.util.field.FieldValuesWrapper;
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
public class VirtualSensorLink extends VirtualSensor<LinkVsnTo> implements ILink {

    private String networkId;
    private String collectorId;
    private String sensorId;

    public VirtualSensorLink() {
    }

    public VirtualSensorLink(String label, String networkId, String collectorId, String sensorId, List<Field> fields) {
        super(VirtualSensorType.LINK, label, fields, 0, TimeUnit.MILLISECONDS);
        this.networkId = networkId;
        this.collectorId = collectorId;
        this.sensorId = sensorId;
        //define as source
        for (Field field : fields) {
            field.setVirtualSensor(this);
        }
    }

    @Override
    public String getNetworkId() {
        return networkId;
    }

    @Override
    public String getCollectorId() {
        return collectorId;
    }

    @Override
    public String getSensorId() {
        return sensorId;
    }

    @Override
    public void setNetworkId(String networkId) {
        this.networkId = networkId;
        update();
    }

    @Override
    public void setCollectorId(String collectorId) {
        this.collectorId = collectorId;
        update();
    }

    @Override
    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
        update();
    }

    @Override
    public boolean setFieldsValues(FieldValuesWrapper valuesWrapper) {
        //Updating is not realized if sensorWrapper.getCreationTimestampInMillis() is lower or equal than super.getCreationTimestampInMillis()

        LinkValuesWrapper sensorWrapper = (LinkValuesWrapper) valuesWrapper;

        if (getCreationTimestampInMillis() > sensorWrapper.getCreationTimestampInMillis()) {
            return false;
        } else if (getCreationTimestampInMillis() == sensorWrapper.getCreationTimestampInMillis()
                && getCreationPrecisionInNano() >= sensorWrapper.getCreationPrecisionInNano()) {
            return false;
        }
        if (!isSource(sensorWrapper)) {
            return false;
        }

        boolean isUpdated = false;
        List<Field> sensorCoFields = sensorWrapper.getValues();
        List<Field> current = getFields();

        synchronized (current) {
            //update field values            
            if (setValuesByReferenceName(getFields(), sensorCoFields)) {
                super.setFieldsValues(sensorWrapper);
                isUpdated = true;
            }
        }
        return isUpdated;
    }

    @Override
    public boolean updateFields(FieldListManager listManager) {
        List<Field> fields = getFields();
        synchronized (fields) {
            boolean updated = listManager.doCreateModifyDelete(fields);
            if (updated) {
                update();
            }
            return updated;
        }
    }

    @Override
    public LinkVsnTo getUniqueTransferObject() {
        LinkVsnTo linkVsnTo = new LinkVsnTo(getId(), getLabel(), sensorId, collectorId, networkId);
        List<Field> fields = getFields();
        for (Field field : fields) {
            VirtualSensor sensor = field.getVirtualSensor();
            int aggregates = field.getAggregates().size();
            int dependents = field.getDependents().size();
            linkVsnTo.createField(field.getId(), field.getReferenceName(), field.getDataTypeId(), field.getConverterId(), field.isStored(), sensor.getId(), aggregates, dependents);
        }
        return linkVsnTo;
    }

    private boolean isSource(LinkValuesWrapper sensor) {
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

    private boolean setValuesByReferenceName(List<Field> current, List<Field> newest) {
        boolean isUpdated = false;
        for (Field field : current) {
            for (Field newField : newest) {
                if (field.equalsInputReference(newField)) {
                    field.setValue(newField.getValue());
                    isUpdated = true;
                }
            }
        }
        return isUpdated;
    }
}
