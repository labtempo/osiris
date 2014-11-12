/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.virtualsensornet.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe
 */
/**
 * Post to create
 *
 * Put to update value
 *
 * Delete to remove CompositeVSensor from virtualsensornet
 */
public class Composite extends VSensor {

    private Map<VSensor, List<String>> sources;

    public Composite() {
        this.sources = new HashMap<>();
    }

    public void addSource(VSensor vsensor, String[] fields) {
        List<String> fieldNames = new ArrayList<>(Arrays.asList(fields));
        
        for (String fieldName : fieldNames) {
            for (Field field : vsensor.getFields()) {
                if(field.getName().equals(fieldName)){
                    addField(field);
                    break;
                }
            }
        }
    }
}
