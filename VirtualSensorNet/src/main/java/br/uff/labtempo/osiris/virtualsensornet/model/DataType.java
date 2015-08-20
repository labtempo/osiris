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
import br.uff.labtempo.osiris.to.virtualsensornet.DataTypeVsnTo;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Cacheable;
import javax.persistence.Column;
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
@Cacheable
public class DataType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ValueType type;

    @Column(nullable = false)
    private String unit;

    @Column(nullable = false)
    private String symbol;

    private boolean isDeleted;

    protected DataType() {
    }

    public DataType(String displayName, ValueType type, String unit, String symbol) {
        this.type = type;
        this.unit = unit;
        this.symbol = symbol;
        this.displayName = displayName;
        if (displayName == null || type == null || unit == null || symbol == null) {
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

    public boolean isIsDeleted() {
        return isDeleted;
    }

    public void setLogicallyDeleted() {
        isDeleted = true;
    }

    public void setLogicallyDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setType(ValueType type) {
        this.type = type;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.type);
        hash = 97 * hash + Objects.hashCode(this.unit);
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
        final DataType other = (DataType) obj;
        if (this.type != other.type) {
            return false;
        }
        if (!Objects.equals(this.unit, other.unit)) {
            return false;
        }
        return true;
    }

    public DataTypeVsnTo getTransferObject(long usedBy) {
        DataTypeVsnTo to = new DataTypeVsnTo(id, displayName, type, unit, symbol);
        to.setUsedBy(usedBy);
        return to;
    }

}
