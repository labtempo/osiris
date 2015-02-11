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
package br.uff.labtempo.osiris.sensornet.model;

import br.uff.labtempo.osiris.to.collector.SensorCoTo;
import br.uff.labtempo.osiris.sensornet.model.state.Model;
import br.uff.labtempo.osiris.sensornet.model.util.ConsumableInfo;
import br.uff.labtempo.osiris.sensornet.model.util.ModelUtil;
import br.uff.labtempo.osiris.to.sensornet.SensorSnTo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
@Entity
@Cacheable
public class Sensor extends Model {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long persistenceId;
    /**
     * hardware id in wsn
     */
    private String id;

    /**
     * parental data
     */
    @ManyToOne
    private Network network;

    @ManyToOne
    private Collector collector;

    /**
     * captureTimestampInMillis of last sensing capture
     */
    private long captureTimestampInMillis;
    private int capturePrecisionInNano;
    private long acquisitionTimestampInMillis;
    private long storageTimestampInMillis;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Field> fields;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Consumable> consumables;
    
    @OneToMany(mappedBy = "sensor",cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("captureTimestampInMillis DESC")
    private List<Revision> revisions;

    protected Sensor() {
    }

    public Sensor(String id, long timestamp, int capturePrecisionInNano, long acquisitionTimestampInMillis, List<Field> fields, List<Consumable> consumables, Map<String, String> info) {
        this.id = id;
        this.captureTimestampInMillis = timestamp;
        this.capturePrecisionInNano = capturePrecisionInNano;
        this.acquisitionTimestampInMillis = acquisitionTimestampInMillis;
        this.fields = fields;
        this.consumables = consumables;
        setInfo(info);
        this.storageTimestampInMillis = getStorageTimestamp();
    }

    public String getId() {
        return id;
    }

    public long getPersistenceId() {
        return persistenceId;
    }

    public Network getNetwork() {
        return network;
    }

    public Collector getCollector() {
        return collector;
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

    public long getStorageTimestampInMillis() {
        return storageTimestampInMillis;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public List<Consumable> getConsumables() {
        return consumables;
    }

    public void setCaptureTimestampInMillis(long captureTimestampInMillis, int capturePrecisionInNano) {
        this.captureTimestampInMillis = captureTimestampInMillis;
        this.capturePrecisionInNano = capturePrecisionInNano;
    }

    public void setAcquisitionTimestampInMillis(long acquisitionTimestampInMillis) {
        this.acquisitionTimestampInMillis = acquisitionTimestampInMillis;
    }

    public void setConsumables(List<Consumable> consumables) {
        this.consumables = consumables;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void setCollector(Collector collector) {
        this.collector = collector;
    }

    public SensorSnTo getTransferObject() {
        return new ModelUtil().toTransferObject(this);
    }

    public static Sensor build(SensorCoTo to) {
        return new ModelUtil().fromTransferObject(to);
    }

    public boolean update(SensorCoTo sensorTo) {
        boolean isUpdate = new ModelUtil().updateFromTransferObject(this, sensorTo);
        if (isUpdate) {
            this.storageTimestampInMillis = getStorageTimestamp();
            update();
        }
        return isUpdate;
    }
    
    private long getStorageTimestamp() {
        if (revisions == null) {
            revisions = new ArrayList<>();
        }
        Revision revision = new Revision(this);
        revisions.add(revision);
        return revision.getStorageTimestampInMillis();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.id);
        hash = 29 * hash + Objects.hashCode(this.network);
        hash = 29 * hash + Objects.hashCode(this.collector);
        hash = 29 * hash + (int) (this.captureTimestampInMillis ^ (this.captureTimestampInMillis >>> 32));
        hash = 29 * hash + this.capturePrecisionInNano;
        hash = 29 * hash + (int) (this.acquisitionTimestampInMillis ^ (this.acquisitionTimestampInMillis >>> 32));
        hash = 29 * hash + (int) (this.storageTimestampInMillis ^ (this.storageTimestampInMillis >>> 32));
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
        final Sensor other = (Sensor) obj;
        if (!super.equals(obj)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.network, other.network)) {
            return false;
        }
        if (!Objects.equals(this.collector, other.collector)) {
            return false;
        }
        if (this.captureTimestampInMillis != other.captureTimestampInMillis) {
            return false;
        }
        if (this.capturePrecisionInNano != other.capturePrecisionInNano) {
            return false;
        }
        if (this.acquisitionTimestampInMillis != other.acquisitionTimestampInMillis) {
            return false;
        }
        if (this.storageTimestampInMillis != other.storageTimestampInMillis) {
            return false;
        }
        return true;
    }

    

    public List<ConsumableInfo> checkConsumables() {
        return new ModelUtil().checkBrokenConsumables(consumables);
    }
}
