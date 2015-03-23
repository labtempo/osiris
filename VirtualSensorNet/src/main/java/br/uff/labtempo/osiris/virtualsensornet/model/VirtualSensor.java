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
import javax.persistence.ManyToMany;
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

    private String label;

    @Enumerated(EnumType.STRING)
    private VirtualSensorType virtualSensorType;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.EAGER)
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

    public VirtualSensor(VirtualSensorType type, String label, List<Field> fields, long creationInterval, TimeUnit creationIntervalTimeUnit) {
        this.virtualSensorType = type;
        this.label = label;
        this.fields = fields;
        this.creationInterval = creationInterval;
        this.creationIntervalTimeUnit = creationIntervalTimeUnit;
    }

    public long getId() {
        return id;
    }

    public String getLabel() {
        return label;
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
        return fields;
    }

    public void setLabel(String label) {
        this.label = label;
        update();
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

    protected void setAllTimestamp(long creationTimestampInMillis, int creationPrecisionInNano, long acquisitionTimestampInMillis) {
        this.creationTimestampInMillis = creationTimestampInMillis;
        this.creationPrecisionInNano = creationPrecisionInNano;
        this.acquisitionTimestampInMillis = acquisitionTimestampInMillis;
        this.storageTimestampInMillis = getStorageTimestamp();
        update();
    }

    protected boolean addField(Field field) {
        if (fields == null) {
            fields = new ArrayList<>();
        }

        boolean isUpdated = fields.add(field);
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

            boolean isUpdated = fields.remove(oldField);

            if (isUpdated) {
                update();
            }
            return isUpdated;
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
            for (Field oldField : fields) {
                if (oldField.equals(field)) {
                    return oldField;
                }
            }
            return null;
        }
    }

    public synchronized VirtualSensorVsnTo getTransferObject() {
        VirtualSensorVsnTo sensorVsnTo = new VirtualSensorVsnTo(id, getModelState().getState(), creationTimestampInMillis, creationPrecisionInNano, creationInterval, creationIntervalTimeUnit, acquisitionTimestampInMillis, storageTimestampInMillis, getLastModifiedDate(), virtualSensorType);

        for (Field field : fields) {
            sensorVsnTo.addValue(field.getId(), field.getReferenceName(), field.getValueType(), field.getValue(), field.getUnit(), field.getSymbol());
        }

        return sensorVsnTo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final VirtualSensor other = (VirtualSensor) obj;
        if (this.id == 0) {
            return false;
        }
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
