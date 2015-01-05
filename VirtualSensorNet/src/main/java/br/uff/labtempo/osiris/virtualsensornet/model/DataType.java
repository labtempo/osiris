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
package br.uff.labtempo.osiris.virtualsensornet.model;

import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
@Entity
public class DataType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private long id;

    private String displayName;
    
    @Enumerated(EnumType.STRING)
    private ValueType type;
    private String unit;
    private String symbol;

    public DataType() {
    }

    public DataType(ValueType type, String unit, String symbol) {
        this.type = type;
        this.unit = unit;
        this.symbol = symbol;

        if (type == null || unit == null || symbol == null) {
            throw new RuntimeException("Arguments cannot be null!");
        }
    }

    public ValueType getValueType() {
        return type;
    }

    public String getUnit() {
        return unit;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getDisplayName() {
        return displayName;
    }

    public long getId() {
        return id;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

        DataType other = (DataType) obj;

        if (!type.equals(other.type)) {
            return false;
        }
        if (!unit.equals(other.unit)) {
            return false;
     }
//        if (!symbol.equals(other.symbol)) {
//            return false;
//        }
        return true;
    }

    @Override
    public int hashCode() {
        return displayName.hashCode();
    }

}
