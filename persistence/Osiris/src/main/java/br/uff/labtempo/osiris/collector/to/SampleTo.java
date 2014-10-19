/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.collector.to;

/**
 *
 * @author Felipe
 */
public class SampleTo {
    private final NetworkTo network;
    private final CollectorTo collector;
    private final SensorTo sensor;

    public SampleTo(NetworkTo network, CollectorTo collector, SensorTo sensor) {
        this.sensor = sensor;
        this.collector = collector;
        this.network = network;
    }

    public NetworkTo getNetwork() {
        return network;
    }

    public CollectorTo getCollector() {
        return collector;
    }

    public SensorTo getSensor() {
        return sensor;
    }
}
