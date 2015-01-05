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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.script.ScriptException;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
@Entity
public class Field implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @ManyToOne
    private DataType defaultDataType;

    @ManyToOne
    private Value currentValue;

    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Value> history;

    @ManyToOne
    private Converter converter;

    @ManyToOne
    private VirtualSensor virtualSensor;

    public Field(String name, DataType defaultdataType) {
        this.name = name;
        this.defaultDataType = defaultdataType;

        if (name == null || defaultdataType == null) {
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

    public String getName() {
        return name;
    }

    public String getValue() {
        return currentValue.getValue();
    }

    public ValueType getValueType() {
        DataType dataType = getDataType();
        ValueType valueType = dataType.getValueType();
        return valueType;
    }

    public String getUnit() {
        DataType dataType = getDataType();
        return dataType.getUnit();
    }

    public String getSymbol() {
        DataType dataType = getDataType();
        return dataType.getSymbol();
    }

    public DataType getDefaultDataType() {
        return defaultDataType;
    }

    public DataType getDataType() {
        if (converter != null) {
            return converter.getDataType();
        }
        return defaultDataType;
    }

    public Converter getConverter() {
        return converter;
    }

    public Calendar getLastModified() {
        return currentValue.getDate();
    }

    public List<Value> getHistory() {
        return history;
    }

    public void setDataType(DataType dataType) {
        this.defaultDataType = dataType;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    public boolean removeConverter() {
        if (converter != null) {
            converter = null;
            return true;
        }
        return false;
    }

    public void addValue(String value) {
        value = convertNewValue(value);
        DataType dataType = getDataType();
        Value newer = new Value(value, dataType, this);
        currentValue = newer;

        if (history == null) {
            history = new ArrayList<>();
        }

        history.add(newer);
    }

    public void addInstantaneousValue(String value) {
        DataType dataType = getDataType();
        Value newer = new Value(value, dataType, this);
        currentValue = newer;
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

        Field other = (Field) obj;

        if (!name.equals(other.name)) {
            return false;
        }
        if (!defaultDataType.equals(other.defaultDataType)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
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
