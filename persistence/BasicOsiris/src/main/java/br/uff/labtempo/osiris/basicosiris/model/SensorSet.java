/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.labtempo.osiris.basicosiris.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Felipe
 */
public class SensorSet {    
    List<VirtualSensor> sensors;

    public SensorSet() {
        sensors = new ArrayList<>();
    }
    
    void create(){
        VirtualSensor sensor = new VirtualSensor();
        sensors.add(sensor);
    }
    
    
}
