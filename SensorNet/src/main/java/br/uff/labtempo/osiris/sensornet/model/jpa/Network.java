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

import br.uff.labtempo.osiris.to.collector.NetworkCoTo;
import br.uff.labtempo.osiris.sensornet.model.state.Model;
import br.uff.labtempo.osiris.sensornet.model.util.ModelUtil;
import br.uff.labtempo.osiris.to.sensornet.NetworkSnTo;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
@Entity
public class Network extends Model {

    @Id
    private String id;

    @OneToMany(mappedBy = "network", cascade = CascadeType.ALL)
    private Set<Collector> collectors;

    @OneToMany(mappedBy = "network", cascade = CascadeType.ALL)
    private Set<Sensor> sensors;

    protected Network() {
    }

    public Network(String id) {
        this.id = id;
        this.collectors = new HashSet<>();
        this.sensors = new HashSet<>();
    }

    public Network(String id, Map<String, String> info) {
        this(id);
        setInfo(info);
    }

    public String getId() {
        return id;
    }

    /**
     * Read only
     *
     * @return
     */
    public Collector[] getCollectors() {
        return collectors.toArray(new Collector[]{});
    }

    public void addCollector(Collector collector) {
        collectors.add(collector);
        collector.setNetwork(this);
    }

    public void removeCollector(Collector collector) {
        collectors.remove(collector);
        collector.setNetwork(null);
    }

    /**
     * Read only
     *
     * @return
     */
    public Sensor[] getSensors() {
        return sensors.toArray(new Sensor[]{});
    }

    public void addSensor(Sensor sensor) {
        sensors.add(sensor);
        sensor.setNetwork(this);
    }

    public void removeSensor(Sensor sensor) {
        sensors.remove(sensor);
        sensor.setNetwork(null);
    }

    public NetworkSnTo getTransferObject() {
        return new ModelUtil().toTransferObject(this);
    }

    public static Network build(NetworkCoTo to) {
        return new ModelUtil().fromTransferObject(to);
    }

    public boolean update(NetworkCoTo networkTo) {
        boolean isUpdate = new ModelUtil().updateFromTransferObject(this, networkTo);
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

        Network  other = (Network) obj;

        if (!id.equals(other.id)) {
            return false;
        }        
        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
