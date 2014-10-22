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
public class NetworkSnTo {
    /** id */
    private final String id;
    
    /** sensornet data */
    private final long modified;
    private final String state;    
   
    /** children amount */
    private final int collectors;
    private final int sensors;
    
    /** domain, os, model, network type and more related to network... */
    private final Map<String,String> info;

    public NetworkSnTo(String id, long lastModifiedDate, String networkState, int collectors, int sensors, Map<String, String> info) {
        this.id = id;
        this.modified = lastModifiedDate;
        this.state = networkState;
        this.collectors = collectors;
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

    public int countCollectors() {
        return collectors;
    }

    public int countSensors() {
        return sensors;
    }

    public Map<String, String> getInfo() {
        return info;
    }
    
}
