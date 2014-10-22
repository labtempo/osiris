/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.collector.to;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public class SensorCoTo {

    private final String id;
    private final long timestamp;
    private final Map<String, Integer> consumables;
    private final List<Map<String, String>> rules;
    private final List<Map<String, String>> values;
    private final Map<String, String> info;

    public SensorCoTo(String id, long timestamp, Map<String, Integer> consumables, List<Map<String, String>> rules, List<Map<String, String>> values, Map<String, String> info) {
        this.id = id;
        this.timestamp = timestamp;
        this.consumables = consumables;
        this.rules = rules;
        this.values = values;
        this.info = info;
    }

    public String getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Map<String, Integer> getConsumables() {
        return consumables;
    }

    public List<Map<String, String>> getRules() {
        return rules;
    }

    public List<Map<String, String>> getValues() {
        return values;
    }

    public Map<String, String> getInfo() {
        return info;
    }

}
