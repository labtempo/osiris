/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.model.jpa;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Felipe
 */
@Entity
public class Value implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String type;
    private String value;
    private String unit;
    private String symbol;

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
}
