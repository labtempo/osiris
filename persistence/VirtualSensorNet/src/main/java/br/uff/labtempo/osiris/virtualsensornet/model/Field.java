/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.virtualsensornet.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Felipe
 */
@Entity
public class Field implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private long id;
    private String name;
    
    @ManyToOne
    private DataType defaultDataType;
    
    @ManyToOne
    private Value currentValue;
    
    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Value> history;
    
    @ManyToOne
    private Converter converter;
    
    @ManyToOne
    private VirtualSensor virtualSensor;

    public Field(String name, DataType defaultdataType) {
        this.name = name;
        this.defaultDataType = defaultdataType;

        if (name == null || defaultdataType == null) {
            throw new RuntimeException("Arguments cannot be null!");
        }
    }
    
    public void setVirtualSensor(VirtualSensor vsensor){
        this.virtualSensor = vsensor;
    }

    protected Field() {
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return currentValue.getValue();
    }

    public String getType() {
        DataType dataType = getDataType();
        return dataType.getType();
    }

    public String getUnit() {
        DataType dataType = getDataType();
        return dataType.getUnit();
    }

    public String getSymbol() {
        DataType dataType = getDataType();
        return dataType.getSymbol();
    }

    public DataType getDefaultDataType() {
        return defaultDataType;
    }

    public DataType getDataType() {
        if (converter != null) {
            return converter.getDataType();
        }
        return defaultDataType;
    }

    public Converter getConverter() {
        return converter;
    }

    public Calendar getLastModified() {
        return currentValue.getDate();
    }

    public List<Value> getHistory() {
        return history;
    }

    public void setDataType(DataType dataType) {
        this.defaultDataType = dataType;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    public boolean removeConverter() {
        if (converter != null) {
            converter = null;
            return true;
        }
        return false;
    }

    public void addValue(String value) {
        DataType dataType = getDataType();
        Value newer = new Value(value, dataType, this);
        currentValue = newer;

        if (history == null) {
            history = new ArrayList<>();
        }

        history.add(newer);
    }

    public void addInstantaneousValue(String value) {
        DataType dataType = getDataType();
        Value newer = new Value(value, dataType, this);
        currentValue = newer;
    }

    public boolean equalsValue(Field obj) {

        if (!equals(obj)) {
            return false;
        }

        Field other = (Field) obj;

        if (!currentValue.equals(other.currentValue)) {
            return false;
        }
        return true;
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

        Field other = (Field) obj;

        if (!name.equals(other.name)) {
            return false;
        }
        if (!defaultDataType.equals(other.defaultDataType)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
