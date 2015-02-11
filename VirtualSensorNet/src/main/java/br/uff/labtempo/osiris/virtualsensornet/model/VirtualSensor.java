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
import br.uff.labtempo.osiris.to.virtualsensornet.VirtualSensorVsnTo;
import br.uff.labtempo.osiris.virtualsensornet.model.state.Model;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class VirtualSensor extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private VirtualSensorType virtualSensorType;

    @OneToMany(mappedBy = "virtualSensor", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Field> fields;

    private long creationTimestampInMillis;
    private int creationPrecisionInNano;
    private long acquisitionTimestampInMillis;
    private long storageTimestampInMillis;

    private long creationInterval;

    @Enumerated(EnumType.STRING)
    private TimeUnit creationIntervalTimeUnit;

    @OneToMany(mappedBy = "virtualSensor", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("timestamp DESC")
    private List<Revision> revisions;

    protected VirtualSensor() {
    }

    public VirtualSensor(VirtualSensorType type, List<Field> fields, long creationInterval, TimeUnit creationIntervalTimeUnit) {
        this.virtualSensorType = type;
        this.fields = fields;
        this.creationInterval = creationInterval;
        this.creationIntervalTimeUnit = creationIntervalTimeUnit;
        for (Field field : fields) {
            field.setVirtualSensor(this);
        }
    }

    public long getId() {
        return id;
    }

    public long getCreationTimestampInMillis() {
        return creationTimestampInMillis;
    }

    public int getCreationPrecisionInNano() {
        return creationPrecisionInNano;
    }

    public long getStorageTimestampInMillis() {
        return storageTimestampInMillis;
    }

    public long getAcquisitionTimestampInMillis() {
        return acquisitionTimestampInMillis;
    }

    public VirtualSensorType getVirtualSensorType() {
        return virtualSensorType;
    }

    public long getCreationInterval() {
        return creationInterval;
    }

    public TimeUnit getCreationIntervalTimeUnit() {
        return creationIntervalTimeUnit;
    }

    public List<Field> getFields() {
        return getConcurrentFields();
    }

    protected boolean updateInterval(long creationInterval, TimeUnit creationIntervalTimeUnit) {
        if (this.creationInterval != creationInterval || !this.creationIntervalTimeUnit.equals(creationIntervalTimeUnit)) {
            this.creationInterval = creationInterval;
            this.creationIntervalTimeUnit = creationIntervalTimeUnit;
            update();
            return true;
        }
        return false;
    }

    protected boolean addNewValues(List<Field> fs, long creationTimestampInMillis, int creationPrecisionInNano, long acquisitionTimestampInMillis) {
        boolean isUpdated = false;
        synchronized (fields) {
            for (Field field : getConcurrentFields()) {
                for (Field newField : fs) {
                    if (field.equalsInputReference(newField)) {
                        field.setValue(newField.getValue());
                        isUpdated = true;
                    }
                }
            }
        }

        if (isUpdated) {
            this.creationTimestampInMillis = creationTimestampInMillis;
            this.creationPrecisionInNano = creationPrecisionInNano;
            this.acquisitionTimestampInMillis = acquisitionTimestampInMillis;
            this.storageTimestampInMillis = getStorageTimestamp();
            update();
        }

        return isUpdated;
    }

    protected boolean addField(Field field) {
        if (fields == null) {
            fields = new ArrayList<>();
        }

        boolean isUpdated = getConcurrentFields().add(field);
        if (isUpdated) {
            update();
        }
        return isUpdated;
    }

    protected boolean removeField(Field field) {
        synchronized (fields) {
            Field oldField = getField(field);

            if (oldField == null) {
                return false;
            }

            boolean isUpdated = getConcurrentFields().remove(oldField);

            if (isUpdated) {
                update();
            }
            return isUpdated;
        }
    }

    protected boolean upgradeFields(List<Field> newFields) {
        List<Field> oldSet, newSet, intersectionOld, intersectionNew, toRemove, toAdd;

        synchronized (fields) {
            oldSet = new ArrayList<>(getConcurrentFields());
            newSet = newFields;

            intersectionOld = new ArrayList<>();
            intersectionNew = new ArrayList<>();

            for (Field oldField : oldSet) {
                for (Field newField : newSet) {
                    newField.setVirtualSensor(this);
                    if (oldField.getId() == newField.getId()) {
                        if (!oldField.equals(newField)) {
                            if (!oldField.isStored()) {
                                oldField.setDataType(newField.getDataType());
                            } else {
                                if (oldField.getDataTypeId() != newField.getDataTypeId()) {
                                    throw new RuntimeException("You cannot to change DataType of a initialized Field!");
                                }
                            }
                            oldField.setConverter(newField.getConverter());
                            oldField.setReferenceName(newField.getReferenceName());
                        }
                        intersectionOld.add(oldField);
                        intersectionNew.add(newField);
                    }
                }
            }

            newSet.removeAll(intersectionNew);

            toRemove = new ArrayList<>();
            toAdd = new ArrayList<>();

            if (intersectionOld.size() > 0) {
                oldSet.removeAll(intersectionOld);
                //remove fields
                for (Field currF : oldSet) {
                    if (currF.isStored()) {
                        throw new RuntimeException("You cannot delete a Field with already stored value!");
                    } else {
                        currF.setVirtualSensor(null);
                        toRemove.add(currF);
                    }
                }

                toAdd.addAll(newFields);
                //insert updated fields
                fields.removeAll(toRemove);
                //add new fields
                fields.addAll(toAdd);
                update();
                return true;
            }
            return false;
        }
    }

    private long getStorageTimestamp() {
        if (revisions == null) {
            revisions = new ArrayList<>();
        }
        Revision revision = new Revision(this);
        revisions.add(revision);
        return revision.getStorageTimestampInMillis();
    }

    private Field getField(Field field) {
        synchronized (fields) {
            for (Field oldField : getConcurrentFields()) {
                if (oldField.equals(field)) {
                    return oldField;
                }
            }
            return null;
        }
    }

    private List<Field> getConcurrentFields() {
        return fields;
    }

    public synchronized VirtualSensorVsnTo getTransferObject() {
        VirtualSensorVsnTo sensorVsnTo = new VirtualSensorVsnTo(id, getModelState().getState(), creationTimestampInMillis, creationPrecisionInNano, creationInterval, creationIntervalTimeUnit, acquisitionTimestampInMillis, storageTimestampInMillis, getLastModifiedDate(), virtualSensorType);

        for (Field field : getConcurrentFields()) {
            sensorVsnTo.addValue(field.getReferenceName(), field.getValueType(), field.getValue(), field.getUnit(), field.getSymbol());
        }

        return sensorVsnTo;
    }

}
