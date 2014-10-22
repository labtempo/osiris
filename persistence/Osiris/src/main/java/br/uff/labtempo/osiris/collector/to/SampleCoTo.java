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
public class SampleCoTo {
    private final NetworkCoTo network;
    private final CollectorCoTo collector;
    private final SensorCoTo sensor;

    public SampleCoTo(NetworkCoTo network, CollectorCoTo collector, SensorCoTo sensor) {
        this.sensor = sensor;
        this.collector = collector;
        this.network = network;
    }

    public NetworkCoTo getNetwork() {
        return network;
    }

    public CollectorCoTo getCollector() {
        return collector;
    }

    public SensorCoTo getSensor() {
        return sensor;
    }
}
