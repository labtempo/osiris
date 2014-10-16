/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet;

import java.util.Map;

/**
 *
 * @author Felipe
 */
public class Sensor {

    private String id;
    private String name;
    private Map<String, String> values;
    private double power;

    public String getId() {
        return id;
    }
}
