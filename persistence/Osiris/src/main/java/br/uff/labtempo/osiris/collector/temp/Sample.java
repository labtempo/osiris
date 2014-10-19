/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.collector.temp;

/**
 *
 * @author Felipe
 */
public final class Sample {

    private Network network;
    private Collector collector;
    private Sensor sensor;

    public Sample(Network network, Collector collector, Sensor sensor) {
        this.sensor = sensor;
        this.collector = collector;
        this.network = network;
    }

    public Network getNetwork() {
        return network;
    }

    public Collector getCollector() {
        return collector;
    }

    public Sensor getSensor() {
        return sensor;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        Sample other = (Sample) obj;

        if (!network.equals(other.network)) {
            return false;
        }
        
        if (!collector.equals(other.collector)) {
            return false;
        }
        
         if (!sensor.equals(other.sensor)) {
            return false;
        }
        return true;

    }

}
