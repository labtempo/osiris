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

import br.uff.labtempo.osiris.to.virtualsensornet.VirtualSensorType;
import br.uff.labtempo.osiris.to.virtualsensornet.VirtualSensorVsnTo;
import br.uff.labtempo.osiris.virtualsensornet.model.state.Model;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class VirtualSensor extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private VirtualSensorType type;

    @OneToMany(mappedBy = "virtualSensor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Field> fields;

    private long timestamp;

    public VirtualSensor() {
    }

    public VirtualSensor(VirtualSensorType type, List<Field> fields) {
        this.type = type;
        this.fields = fields;
        for (Field field : fields) {
            field.setVirtualSensor(this);
        }
    }

    public long getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public VirtualSensorType getType() {
        return type;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    protected boolean updateFields(String fieldName, String newValue, DataType dataType) {
        boolean isUpdated = false;

        for (Field field : fields) {
            if (field.getName().equals(fieldName) && field.getDefaultDataType().equals(dataType)) {
                field.addValue(newValue);
                isUpdated = true;
            }
        }

        return isUpdated;
    }

    protected void addField(Field field) {
        if (fields == null) {
            fields = new ArrayList<>();
        }
        fields.add(field);
    }

    protected boolean removeField(Field field) {
        Field oldField = getField(field);
        if (oldField != null) {
            return fields.remove(oldField);
        }
        return false;
    }

    private Field getField(Field field) {
        for (Field oldField : fields) {
            if (oldField.equals(field)) {
                return oldField;
            }
        }
        return null;
    }

    public VirtualSensorVsnTo getTransferObject() {
        VirtualSensorVsnTo sensorVsnTo = new VirtualSensorVsnTo(id, getModelState().getState(), timestamp, getLastModifiedDate(), type);

        for (Field field : fields) {
            sensorVsnTo.addValue(field.getName(), field.getValueType(), field.getValue(), field.getUnit(), field.getSymbol());
        }

        return sensorVsnTo;
    }
}
