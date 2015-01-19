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
package br.uff.labtempo.osiris.sensornet.model.jpa;

import br.uff.labtempo.osiris.to.collector.SensorCoTo;
import br.uff.labtempo.osiris.sensornet.model.state.Model;
import br.uff.labtempo.osiris.sensornet.model.util.ConsumableInfo;
import br.uff.labtempo.osiris.sensornet.model.util.ModelUtil;
import br.uff.labtempo.osiris.to.sensornet.SensorSnTo;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
@Entity
public class Sensor extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long sid;
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
     * timestamp of last sensing capture
     */
    private long timestamp;

    private TimeUnit timestampUnit;

    private long timeOfCollectionInMillis;

    @ElementCollection
    private List<Value> values;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Consumable> consumables;

    protected Sensor() {
    }

    public Sensor(String id, long timestamp, TimeUnit timestampTimeUnit, long timeOfCollectionInMillis, List<Value> values, List<Consumable> consumables, Map<String, String> info) {
        this.id = id;
        this.timestamp = timestamp;
        this.timestampUnit = timestampTimeUnit;
        this.timeOfCollectionInMillis = timeOfCollectionInMillis;
        this.values = values;
        this.consumables = consumables;
        setInfo(info);
    }

    public String getId() {
        return id;
    }

    public long getSid() {
        return sid;
    }

    public Network getNetwork() {
        return network;
    }

    public Collector getCollector() {
        return collector;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public TimeUnit getTimestampUnit() {
        return timestampUnit;
    }

    public long getTimeOfCollectionInMillis() {
        return timeOfCollectionInMillis;
    }

    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }

    public List<Consumable> getConsumables() {
        return consumables;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setTimestampUnit(TimeUnit timestampUnit) {
        this.timestampUnit = timestampUnit;
    }

    public void setTimeOfCollectionInMillis(long timeOfCollectionInMillis) {
        this.timeOfCollectionInMillis = timeOfCollectionInMillis;
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
            update();
        }
        return isUpdate;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        Sensor other = (Sensor) obj;

        if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public List<ConsumableInfo> checkConsumables() {
        return new ModelUtil().checkBrokenConsumables(consumables);
    }
}
