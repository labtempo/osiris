package br.uff.labtempo.osiris.sensornet.to;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public class SensorSnTo {
    /** hardware id in wsn */
    private final String id;
    
    /** sensornet data */
    private final long modified;
    private final String state;
    
    /** parental data */
    private final String network;
    private final String collector;    
    
    /** timestamp of last sensing capture */
    private final long timestamp;
    
    /** sensing values{name of field, value, value type, metric unit, metric symbol} */
    private final List<Map<String,String>> values;
    
    /** watchable hardware resources */
    private final Map<String,Integer> consumables;
    
    /** parent, typeof, model, role and more... */
    private final Map<String,String> info;

    public SensorSnTo(String id, long lastModifiedDate, String sensorState, String networkId, String collectorId, long captureTimestamp, List<Map<String, String>> values, Map<String, Integer> consumables, Map<String, String> info) {
        this.id = id;
        this.modified = lastModifiedDate;
        this.state = sensorState;
        this.network = networkId;
        this.collector = collectorId;
        this.timestamp = captureTimestamp;
        this.values = values;
        this.consumables = consumables;
        this.info = info;
    }

    public String getId() {
        return id;
    }

    public long getLastModified() {
        return modified;
    }

    public String getState() {
        return state;
    }

    public String getNetworkId() {
        return network;
    }

    public String getCollectorId() {
        return collector;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public List<Map<String, String>> getValues() {
        return values;
    }

    public Map<String, Integer> getConsumables() {
        return consumables;
    }

    public Map<String, String> getInfo() {
        return info;
    }
}
