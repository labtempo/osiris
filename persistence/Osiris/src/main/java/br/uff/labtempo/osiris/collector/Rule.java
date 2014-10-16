/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.collector;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public class Rule {

    private String name;
    private Map<String, String> properties;

    public Rule(String name) {
        this.name = name;
        this.properties = new HashMap<>();
    }

    public void add(String key, String value) {
        properties.put(key, value);
    }

    public void add(String key, long value) {
        properties.put(key, String.valueOf(value));
    }

    public void add(String key, double value) {
        properties.put(key, String.valueOf(value));
    }

    public void add(String key, boolean value) {
        properties.put(key, String.valueOf(value));
    }

    public String getProperty(String key) {
        return properties.get(key);
    }

    public String getName() {
        return name;
    }

}
