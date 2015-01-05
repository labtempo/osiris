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

import br.uff.labtempo.osiris.to.common.definitions.State;
import br.uff.labtempo.osiris.to.common.definitions.ValueType;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class DataTypeVsnTo {

    private long id;
    private final String displayName;
    private final String type;
    private final String unit;
    private final String symbol;
    
    //helper attributes
    private transient ValueType helperValueType;

    public DataTypeVsnTo(String displayName, ValueType valueType, String unit, String symbol) {
        this.displayName = displayName;
        this.type = valueType.toString();
        this.unit = unit;
        this.symbol = symbol;
        
        this.helperValueType = valueType;
    }

    public long getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ValueType getType() {
        if (helperValueType == null) {
            helperValueType = Enum.valueOf(ValueType.class, type);
        }
        return helperValueType;
    }

    public String getUnit() {
        return unit;
    }

    public String getSymbol() {
        return symbol;
    }
    
    
}
