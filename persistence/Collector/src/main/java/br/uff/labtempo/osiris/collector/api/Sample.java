/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.collector.api;

import br.uff.labtempo.osiris.collector.api.sensor.SensorApi;
import br.uff.labtempo.osiris.collector.api.sensor.Value;
import java.util.List;

/**
 *
 * @author Felipe
 */
public class Sample {

    SensorApi sensor;
    private long timestamp;
    List<Value> values;
}
