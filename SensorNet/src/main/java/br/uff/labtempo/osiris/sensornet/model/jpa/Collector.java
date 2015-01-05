/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.model.jpa;

import br.uff.labtempo.osiris.to.collector.CollectorCoTo;
import br.uff.labtempo.osiris.sensornet.model.state.Model;
import br.uff.labtempo.osiris.sensornet.model.util.ModelUtil;
import br.uff.labtempo.osiris.to.sensornet.CollectorSnTo;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
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
 * @author Felipe
 */
@Entity
public class Collector extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cid;

    private String id;
    private long interval;

    @Enumerated(EnumType.STRING)
    private TimeUnit timeUnit;

    @ManyToOne
    private Network network;

    @OneToMany(mappedBy = "collector", cascade = CascadeType.ALL)
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
        sensor.setNetwork(null);
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

        Collector other = (Collector) obj;

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
