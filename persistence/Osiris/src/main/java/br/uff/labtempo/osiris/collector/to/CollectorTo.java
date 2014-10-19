/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.collector.to;

import java.util.Map;

/**
 *
 * @author Felipe
 */
public class CollectorTo {
    private final String id;
    private final Map<String, String> info;

    public CollectorTo(String id, Map<String, String> info) {
        this.id = id;
        this.info = info;
    }

    public Map<String, String> getInfo() {
        return info;
    }

    public String getId() {
        return id;
    }
}
