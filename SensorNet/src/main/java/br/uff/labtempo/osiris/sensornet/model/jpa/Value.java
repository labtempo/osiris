/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.model.jpa;

import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import java.io.Serializable;
import javax.persistence.Embeddable;

/**
 *
 * @author Felipe
 */
@Embeddable
public class Value implements Serializable{    
    private String name;
    private ValueType type;
    private String value;
    private String unit;
    private String symbol;

    protected Value() {
    }

    public Value(String name, ValueType type, String value, String unit, String symbol) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.unit = unit;
        this.symbol = symbol;
    }

    public Value(String name, long value, String unit, String symbol) {
        this(name, ValueType.NUMBER, String.valueOf(value), unit, symbol);
    }

    public Value(String name, double value, String unit, String symbol) {
        this(name, ValueType.NUMBER, String.valueOf(value), unit, symbol);
    }

    public Value(String name, String value, String unit, String symbol) {
        this(name, ValueType.TEXT, String.valueOf(value), unit, symbol);
    }

    public Value(String name, boolean value, String unit, String symbol) {
        this(name, ValueType.LOGIC, String.valueOf(value), unit, symbol);
    }

    public String getName() {
        return name;
    }

    public ValueType getType() {
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
