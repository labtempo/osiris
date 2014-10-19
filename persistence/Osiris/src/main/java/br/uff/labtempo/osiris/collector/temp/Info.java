/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.collector.temp;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public class Info {

    private Map<String, String> info;

    public Info() {
        this.info = new HashMap<>();
    }

    public Map<String, String> getInfo() {
        return info;
    }

    public void add(String key, String value) {
        info.put(key, value);
    }
}
