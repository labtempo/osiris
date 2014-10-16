/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.collector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public final class Sensor {

    private String id;
    /**
     * percent
     */
    private int battery;
    private long timestamp;
    private List<Value> values;
    private Map<String, String> info;
    private List<Rule> rules;

    public Sensor(String id, int battery, long timestamp) {
        this.id = id;
        this.battery = battery;
        this.timestamp = timestamp;
        this.values = new ArrayList<>();
    }

    public Sensor(long id, int battery, long timestamp) {
        this(String.valueOf(id), battery, timestamp);
    }

    public Sensor(String id, long timestamp) {
        this(id, 100, timestamp);
    }

    public Sensor(long id, long timestamp) {
        this(String.valueOf(id), 100, timestamp);
    }

    public void addValue(String name, String value, String unit, String symbol) {
        values.add(new Value(name, value, unit, symbol));
    }

    public void addValue(String name, long value, String unit, String symbol) {
        values.add(new Value(name, value, unit, symbol));
    }

    public void addValue(String name, double value, String unit, String symbol) {
        values.add(new Value(name, value, unit, symbol));
    }

    public void addValue(String name, boolean value, String unit, String symbol) {
        values.add(new Value(name, value, unit, symbol));
    }

    public String getId() {
        return id;
    }

    public int getBattery() {
        return battery;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public List<Value> getValues() {
        return values;
    }

    public Map<String, String> getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info.getInfo();
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

        Sensor other = (Sensor) obj;

        if (!id.equals(other.id)) {
            return false;
        }
        if (timestamp != other.timestamp) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public Rule getRule(String name) {
        for (Rule rule : rules) {
            if (rule.getName().equals(name)) {
                return rule;
            }
        }
        return null;
    }

    public void addRule(Rule rule) {
        if (rules == null) {
            rules = new ArrayList<>();
        }
        rules.add(rule);
    }

    public class Value {

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
}
