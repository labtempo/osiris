/* 
 * Copyright 2015 Felipe Santos <fralph at ic.uff.br>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.uff.labtempo.osiris.to.common.data;

import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
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
