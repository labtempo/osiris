/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.monitor.model;

import java.util.Calendar;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public class Sensor {

    private final String model;
    private final String id;
    private final Calendar date;
    private final Map<String, String> values;

    public Sensor(String id, String model, Calendar date, Map<String,String> values) {
        this.id = id;
        this.model = model;
        this.date = date;
        
        this.values = values;
       
    }

    /**
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the date
     */
    public Calendar getDate() {
        return date;
    }

    /**
     * @return the values
     */
    public Map<String, String> getValues() {
        return values;
    }

    
}
