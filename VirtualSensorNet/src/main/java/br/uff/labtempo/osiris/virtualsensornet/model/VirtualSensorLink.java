/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.virtualsensornet.model;

import br.uff.labtempo.osiris.to.virtualsensornet.VirtualSensorType;
import br.uff.labtempo.osiris.virtualsensornet.model.util.SensorCoWrapper;
import java.util.List;
import javax.persistence.Entity;

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
