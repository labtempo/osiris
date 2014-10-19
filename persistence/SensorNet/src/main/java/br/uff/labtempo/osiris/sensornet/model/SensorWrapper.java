/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.model;

import br.uff.labtempo.osiris.sensornet.model.state.Model;
import br.uff.labtempo.osiris.collector.temp.Sensor;
import java.util.Collection;

/**
 *
 * @author Felipe
 */
public class SensorWrapper extends Model<SensorWrapper> {

    private Sensor sensor;

    public SensorWrapper(Sensor sensor) {
        this.sensor = sensor;
    }

    public String getId() {
        return sensor.getId();
    }

    @Override
    public void update(SensorWrapper object) {
        sensor = object.sensor;
        super.update();
    }

    @Override
    public boolean equals(Object obj) {
        return sensor.equals(obj);
    }

    @Override
    public int hashCode() {
        return sensor.hashCode();
    }

    public Sensor getContent() {
        return sensor;
    }

}
