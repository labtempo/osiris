/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.model.jpa;

import br.uff.labtempo.osiris.sensornet.model.state.Model;
import br.uff.labtempo.osiris.sensornet.to.SensorTo;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

/**
 *
 * @author Felipe
 */
@Entity
public class Sensor extends Model<Sensor> {

    /**
     * hardware id in wsn
     */
    @Id
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
    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy("name ASC")
    private List<Value> values;

    /**
     * watchable hardware resources
     */
    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy("name ASC")
    private List<Consumable> consumables;

    public SensorTo getTransferObject() {
        Map<String, Integer> _consumables = new HashMap<>();
        for (Consumable consumable : this.consumables) {
            _consumables.put(consumable.getName(), consumable.getValue());
        }

        List<Map<String, String>> _values = new ArrayList<>();
        for (Value value : this.values) {
            Map<String, String> map = new HashMap<>();
            map.put("name", value.getName());
            map.put("value", value.getValue());
            map.put("type", value.getType());
            map.put("unit", value.getUnit());
            map.put("symbol", value.getSymbol());
            _values.add(map);
        }

        return new SensorTo(id, getLastModifiedDate().getTimeInMillis(), getState().toString(), network.getId(), collector.getId(), timestamp, _values, _consumables, getInfo());
    }
}
