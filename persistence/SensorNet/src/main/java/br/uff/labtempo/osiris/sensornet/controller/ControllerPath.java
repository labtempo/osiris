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

    PROTOCOL("omcp://"),
    NETWORK_KEY(":nid"),
    COLLECTOR_KEY(":cid"),
    SENSOR_KEY(":sid"),
    SEPARATOR("/"),
    NETWORK_ALL(SEPARATOR.toString()),
    NETWORK_BY_ID(SEPARATOR.toString() + NETWORK_KEY),
    COLLECTOR_UNIT(SEPARATOR.toString() + "collectors" + SEPARATOR),
    COLLECTOR_ALL(NETWORK_BY_ID.toString() + COLLECTOR_UNIT),
    COLLECTOR_BY_ID(COLLECTOR_ALL.toString() + COLLECTOR_KEY),
    SENSOR_UNIT(SEPARATOR.toString() + "sensors" + SEPARATOR),    
    SENSOR_ALL_BY_COLLECTOR(COLLECTOR_BY_ID.toString() + SENSOR_UNIT),
    SENSOR_ALL_BY_NETWORK(NETWORK_BY_ID.toString() + SENSOR_UNIT),
    SENSOR_BY_ID(SENSOR_ALL_BY_COLLECTOR.toString() + SENSOR_KEY),
    COLLECTOR_MESSAGEGROUP("collector.messagegroup"),
    UPDATE_MESSAGEGROUP("update.messagegroup"),
    NOTIFICATION_MESSAGEGROUP("notification.messagegroup");

    private ControllerPath(String content) {
        this.content = content;
    }

    private String content;

    @Override
    public String toString() {
        return content;
    }
}
