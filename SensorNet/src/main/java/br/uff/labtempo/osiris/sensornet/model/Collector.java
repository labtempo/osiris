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

import br.uff.labtempo.osiris.to.collector.CollectorCoTo;
import br.uff.labtempo.osiris.sensornet.model.state.Model;
import br.uff.labtempo.osiris.sensornet.model.util.ModelUtil;
import br.uff.labtempo.osiris.to.sensornet.CollectorSnTo;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Cacheable
public class Collector extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long persistenceId;

    private String id;
    private long interval;

    @Enumerated(EnumType.STRING)
    private TimeUnit timeUnit;

    @ManyToOne
    private Network network;

    @OneToMany(mappedBy = "collector", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Sensor> sensors;

    protected Collector() {
    }

    public Collector(String id, long interval, TimeUnit timeUnit) {
        this.id = id;
        this.interval = interval;
        this.timeUnit = timeUnit;
        this.sensors = new HashSet<>();
    }

    public Collector(String id, long interval, TimeUnit timeUnit, Map<String, String> info) {
        this(id, interval, timeUnit);
        setInfo(info);
    }

    public long getPersistenceId() {
        return persistenceId;
    }

    public String getId() {
        return id;
    }

    public long getInterval() {
        return interval;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public Network getNetwork() {
        return network;
    }

    /**
     * Read only
     *
     * @return
     */
    public Sensor getSensorsByName(String sensorId) {
        for (Sensor sensor : sensors) {
            if (sensor.getId().equals(sensorId)) {
                return sensor;
            }
        }
        return null;
    }

    public Sensor[] getSensors() {
        return sensors.toArray(new Sensor[]{});
    }

    public void addSensor(Sensor sensor) {
        sensors.add(sensor);
        sensor.setCollector(this);
        sensor.setNetwork(network);
    }

    public void removeSensor(Sensor sensor) {
        sensors.remove(sensor);
        sensor.setCollector(null);
        network.removeSensor(sensor);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setInterval(long interval, TimeUnit timeUnit) {
        this.interval = interval;
        this.timeUnit = timeUnit;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public CollectorSnTo getTransferObject() {
        return new ModelUtil().toTransferObject(this);
    }

    public static Collector build(CollectorCoTo to) {
        return new ModelUtil().fromTransferObject(to);
    }

    public boolean update(CollectorCoTo collectorTo) {
        boolean isUpdate = new ModelUtil().updateFromTransferObject(this, collectorTo);
        if (isUpdate) {
            update();
        }
        return isUpdate;
    }

//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//
//        Collector other = (Collector) obj;
//
//        if (!name.equals(other.name)) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        return name.hashCode();
//    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (int) (this.persistenceId ^ (this.persistenceId >>> 32));
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
        final Collector other = (Collector) obj;
        if (this.persistenceId != other.persistenceId) {
            return false;
        }
        return true;
    }
}
