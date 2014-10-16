/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Felipe
 */
public class Network {

    private String id;
    private String name;
    private transient List<Sensor> sensors;

    public Network() {
        sensors = Collections.synchronizedList(new ArrayList<Sensor>());
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public String getId() {
        return id;
    }
}
