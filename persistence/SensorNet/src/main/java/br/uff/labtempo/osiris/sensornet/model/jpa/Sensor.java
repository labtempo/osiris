/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.model.jpa;

import br.uff.labtempo.osiris.to.collector.SensorCoTo;
import br.uff.labtempo.osiris.sensornet.model.state.Model;
import br.uff.labtempo.osiris.sensornet.model.util.ConsumableInfo;
import br.uff.labtempo.osiris.sensornet.model.util.ModelUtil;
import br.uff.labtempo.osiris.to.sensornet.SensorSnTo;
import java.util.List;
import java.util.Map;
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
 * @author Felipe
 */
@Entity
public class Sensor extends Model<Sensor> {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
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

    /**
     * sensing values{name of field, value, value type, metric unit, metric
     * symbol}
     */
//    @ElementCollection
//    @CollectionTable(
//          name="VALUE",
//          joinColumns=@JoinColumn(name="SENSOR_ID")
//    )
//    @OrderBy("name ASC")
    @ElementCollection
    private List<Value> values;

    /**
     * watchable hardware resources
     */
//    @ElementCollection
//    @CollectionTable(
//          name="CONSUMABLE",
//          joinColumns=@JoinColumn(name="SENSOR_ID")
//    )
//    @OrderBy("name ASC")
    @OneToMany(cascade = CascadeType.ALL)
    private List<Consumable> consumables;

    protected Sensor() {
    }

    public Sensor(String id, long timestamp, List<Value> values, List<Consumable> consumables, Map<String, String> info) {
        this.id = id;
        this.timestamp = timestamp;
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

    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }

    public List<Consumable> getConsumables() {
        return consumables;
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
