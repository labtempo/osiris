/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.virtualsensornet.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author Felipe
 */
public class Field {

    private String name;
    private String type;
    private String value;
    private String unit;
    private String symbol;
    private Calendar lastModified;

    private List<Value> history;

    public Field(String name, String type, String value, String unit, String symbol) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.unit = unit;
        this.symbol = symbol;
        this.history = new ArrayList<>();
        addValueToHistory(value);
    }

    public Field(String name) {
        this(name, null, null, null, null);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    public String getSymbol() {
        return symbol;
    }

    public Calendar getLastModified() {
        return lastModified;
    }
    

    public List<String> getHistory() {
        List<String> values = new ArrayList<>();
        for (Value v : history) {
            values.add(v.getValue());
        }
        return values;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setValue(String value) {
        this.value = value;
        addValueToHistory(value);
    }

    private void addValueToHistory(String v) {
        if (v != null) {
            Value newer = new Value(v);
            this.history.add(newer);
            this.lastModified = newer.getDate();
        }
    }

    public boolean equalsMetadata(Field obj) {

        if (obj == null) {
            return false;
        }
        if (!name.equals(obj.name)) {
            return false;
        }
        if (!type.equals(obj.type)) {
            return false;
        }
        if (!unit.equals(obj.unit)) {
            return false;
        }
        if (!symbol.equals(obj.symbol)) {
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

        if (!equalsMetadata(other)) {
            return false;
        }
        if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
