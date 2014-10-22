/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.model.jpa;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Felipe
 */
@Embeddable
public class Value implements Serializable{    
    private String name;
    private String type;
    private String value;
    private String unit;
    private String symbol;

    protected Value() {
    }

    public Value(String name, String type, String value, String unit, String symbol) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.unit = unit;
        this.symbol = symbol;
    }

    public Value(String name, long value, String unit, String symbol) {
        this(name, "integer", String.valueOf(value), unit, symbol);
    }

    public Value(String name, double value, String unit, String symbol) {
        this(name, "real", String.valueOf(value), unit, symbol);
    }

    public Value(String name, String value, String unit, String symbol) {
        this(name, "text", String.valueOf(value), unit, symbol);
    }

    public Value(String name, boolean value, String unit, String symbol) {
        this(name, "boolean", String.valueOf(value), unit, symbol);
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

        Value other = (Value) obj;

        if (!name.equals(other.name)) {
            return false;
        }
        if (!type.equals(other.type)) {
            return false;
        }
        if (!value.equals(other.value)) {
            return false;
        }
        if (!unit.equals(other.unit)) {
            return false;
        }
        if (!symbol.equals(other.symbol)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    
}
