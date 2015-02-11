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
package br.uff.labtempo.osiris.to.virtualsensornet;

import br.uff.labtempo.osiris.to.virtualsensornet.interfaces.IDataTypeVsnTo;
import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import java.util.Objects;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class DataTypeVsnTo implements IDataTypeVsnTo {

    private long id;
    private String displayName;
    private String type;
    private String unit;
    private String symbol;
    private long usedBy;

    //helper attributes
    private transient ValueType helperValueType;    

    public DataTypeVsnTo(String displayName, ValueType valueType, String unit, String symbol) {
        this(0, displayName, valueType, unit, symbol);
    }

    public DataTypeVsnTo(long id, String displayName, ValueType valueType, String unit, String symbol) {
        this.id = id;
        this.displayName = displayName;
        this.type = valueType.toString();
        this.unit = unit;
        this.symbol = symbol;

        this.helperValueType = valueType;
    }

    public void setUsedBy(long usedBy) {
        this.usedBy = usedBy;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public ValueType getType() {
        if (helperValueType == null) {
            helperValueType = Enum.valueOf(ValueType.class, type.toUpperCase());
        }
        return helperValueType;
    }

    @Override
    public String getUnit() {
        return unit;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public long getUsedBy() {
        return usedBy;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.displayName);
        hash = 47 * hash + Objects.hashCode(this.type);
        hash = 47 * hash + Objects.hashCode(this.unit);
        hash = 47 * hash + Objects.hashCode(this.symbol);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DataTypeVsnTo other = (DataTypeVsnTo) obj;
        if (!Objects.equals(this.displayName, other.displayName)) {
            return false;
        }
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        if (!Objects.equals(this.unit, other.unit)) {
            return false;
        }
        if (!Objects.equals(this.symbol, other.symbol)) {
            return false;
        }
        return true;
    }

}
