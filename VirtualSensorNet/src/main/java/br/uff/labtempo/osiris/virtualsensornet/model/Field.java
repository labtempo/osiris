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
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.script.ScriptException;
import org.hibernate.annotations.Where;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
@Entity
public class Field implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String referenceName;
    private String currentValue;

    @ManyToOne
    private DataType dataType;

    @ManyToOne
    private DataConverter converter;

    @ManyToOne
    private VirtualSensor virtualSensor;

    private boolean isStored;

    public Field(String referenceName, DataType dataType) {
        this(0, referenceName, dataType);
    }

    public Field(long id, String referenceName, DataType dataType) {
        this.id = id;
        this.referenceName = referenceName;
        this.dataType = dataType;

        if (referenceName == null || dataType == null) {
            throw new RuntimeException("Arguments cannot be null!");
        }
    }

    public void setVirtualSensor(VirtualSensor vsensor) {
        this.virtualSensor = vsensor;
    }

    protected Field() {
    }

    public long getId() {
        return id;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public String getValue() {
        return currentValue;
    }

    public ValueType getValueType() {
        return dataType.getValueType();
    }

    public String getUnit() {
        return dataType.getUnit();
    }

    public String getSymbol() {
        return dataType.getSymbol();
    }

    public DataType getDataType() {
        return dataType;
    }

    public long getDataTypeId() {
        return dataType.getId();
    }

    public DataType getInputDataType() {
        if (converter != null) {
            return converter.getInputDataType();
        }
        return dataType;
    }

    public boolean hasConverter() {
        if (converter != null) {
            return true;
        }
        return false;
    }

    public DataConverter getConverter() {
        return converter;
    }

    public long getConverterId() {
        if (hasConverter()) {
            return converter.getId();
        } else {
            return 0;
        }
    }

    public void setStored() {
        this.isStored = true;
    }

    public void setStored(boolean value) {
        this.isStored = value;
    }

    public boolean isStored() {
        return isStored;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public void setConverter(DataConverter converter) {
        if (converter == null) {
            converter = null;
            return;
        }

        if (converter.getOutputDataType().equals(dataType)) {
            this.converter = converter;
        } else {
            throw new RuntimeException("Convert cannot be applied, because output DataType is not equal to Field's DataType!");
        }
    }

    public boolean removeConverter() {
        if (converter != null) {
            converter = null;
            return true;
        }
        return false;
    }

    public void setValue(String value) {
        this.currentValue = convertNewValue(value);
    }

    public boolean equalsValue(Field obj) {

        if (!equals(obj)) {
            return false;
        }

        Field other = (Field) obj;

        if (!currentValue.equals(other.currentValue)) {
            return false;
        }
        return true;
    }

    public boolean equalsInputReference(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        Field other = (Field) obj;

        if (!referenceName.equals(other.referenceName)) {
            return false;
        }
        if (!getInputDataType().equals(other.dataType)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.referenceName);
        hash = 41 * hash + Objects.hashCode(this.dataType);
        hash = 41 * hash + Objects.hashCode(this.converter);
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
        final Field other = (Field) obj;
        if (!Objects.equals(this.referenceName, other.referenceName)) {
            return false;
        }
        if (!Objects.equals(this.dataType, other.dataType)) {
            return false;
        }
        if (!Objects.equals(this.converter, other.converter)) {
            return false;
        }
        return true;
    }

    private String convertNewValue(String value) {
        if (converter != null) {
            try {
                return converter.convert(value);
            } catch (ScriptException ex) {
                Logger.getLogger(Field.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return value;
    }

}
