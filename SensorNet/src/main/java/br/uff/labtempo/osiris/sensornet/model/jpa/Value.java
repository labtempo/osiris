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
package br.uff.labtempo.osiris.sensornet.model.jpa;

import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import java.io.Serializable;
import javax.persistence.Embeddable;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
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
