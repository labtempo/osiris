/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.virtualsensornet.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public abstract class VSensor {

    private long id;
    private List<Field> fields;
    private Calendar lastModified;

    public VSensor() {
        this.lastModified = Calendar.getInstance();
    }

    protected void setValue(String fieldName, String value) {
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                field.setValue(value);
                break;
            }
        }
    }

    protected void setValue(Map<String, String> values) {
        for (Map.Entry<String, String> entrySet : values.entrySet()) {
            String fieldName = entrySet.getKey();
            String value = entrySet.getValue();
            for (Field field : fields) {
                if (field.getName().equals(fieldName)) {
                    field.setValue(value);
                    break;
                }
            }
        }
    }

    public long getId() {
        return id;
    }

    protected List<Field> getFields() {
        return fields;
    }

    protected void addField(Field field) {
        if (fields == null) {
            fields = new ArrayList<>();
        }
        fields.add(field);
    }

    protected void setLastModified(Calendar date) {
        this.lastModified = date;
    }

    public Calendar getLastModified() {
        return lastModified;
    }
    
}
