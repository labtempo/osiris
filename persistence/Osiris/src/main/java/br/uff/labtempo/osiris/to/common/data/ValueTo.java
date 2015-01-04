/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.to.common.data;

import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public abstract class ValueTo {

    private final String name;
    private final ValueType type;
    private final String value;
    private final String unit;
    private final String symbol;

    private final transient String NAME = "name";
    private final transient String TYPE = "type";
    private final transient String VALUE = "value";
    private final transient String UNIT = "unit";
    private final transient String SYMBOL = "symbol";

    protected ValueTo(Map<String, String> map) {
        this.name = map.get(NAME);
        this.type = ValueType.getByCode(map.get(TYPE));
        this.value = map.get(VALUE);
        this.unit = map.get(UNIT);
        this.symbol = map.get(SYMBOL);
    }

    protected ValueTo(String name, ValueType type, String value, String unit, String symbol) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.unit = unit;
        this.symbol = symbol;
    }

    protected ValueTo(String name, String value, String unit, String symbol) {
        this(name, ValueType.TEXT, String.valueOf(value), unit, symbol);
    }

    protected ValueTo(String name, long value, String unit, String symbol) {
        this(name, ValueType.NUMBER, String.valueOf(value), unit, symbol);
    }

    protected ValueTo(String name, double value, String unit, String symbol) {
        this(name, ValueType.NUMBER, String.valueOf(value), unit, symbol);
    }

    protected ValueTo(String name, boolean value, String unit, String symbol) {
        this(name, ValueType.LOGIC, String.valueOf(value), unit, symbol);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getUnit() {
        return unit;
    }

    public ValueType getType() {
        return type;
    }

    protected Map<String, String> toMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put(NAME, name);
        map.put(TYPE, type.toString());
        map.put(VALUE, value);
        map.put(UNIT, unit);
        map.put(SYMBOL, symbol);
        return map;
    }

}
