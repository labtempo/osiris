/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.controller;

/**
 *
 * @author Felipe
 */
public enum ControllerPath {

    NETWORK_KEY(":nid"),
    COLLECTOR_KEY(":cid"),
    SENSOR_KEY(":sid"),
    NETWORK_ALL("/"),
    NETWORK_BY_ID("/" + NETWORK_KEY),
    COLLECTOR_ALL(NETWORK_BY_ID.toString() + "/collectors/"),
    COLLECTOR_BY_ID(COLLECTOR_ALL.toString() + COLLECTOR_KEY),
    SENSOR_ALL_BY_COLLECTOR(COLLECTOR_BY_ID.toString() + "/sensors/"),
    SENSOR_ALL_BY_NETWORK(NETWORK_BY_ID.toString() + "/sensors/"),
    SENSOR_BY_ID(SENSOR_ALL_BY_COLLECTOR.toString() + SENSOR_KEY),
    COLLECTOR_EXCHANGE("collector.ex");
    

    private ControllerPath(String content) {
        this.content = content;
    }

    private String content;

    @Override
    public String toString() {
        return content;
    }
}
