/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.to;

import java.util.Map;

/**
 *
 * @author Felipe
 */
public class CollectorSnTo {
    /** id */
    private final String id;
    
    /** sensornet data */
    private final long modified;
    private final String state;
    
    /** parental data */
    private final String network;
    
    /** children amount */
    private final int sensors;
    
    /** description, role, location and more related to collector... */
    private final Map<String,String> info;

    public CollectorSnTo(String id, long lastModifiedDate, String collectorState, String networkId, int sensors, Map<String, String> info) {
        this.id = id;
        this.modified = lastModifiedDate;
        this.state = collectorState;
        this.network = networkId;
        this.sensors = sensors;
        this.info = info;
    }

    public String getId() {
        return id;
    }

    public long getModified() {
        return modified;
    }

    public String getState() {
        return state;
    }

    public String getNetworkId() {
        return network;
    }

    public int countSensors() {
        return sensors;
    }

    public Map<String, String> getInfo() {
        return info;
    }
}
