/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.virtualsensornet.model;

import br.uff.labtempo.osiris.to.virtualsensornet.VirtualSensorType;
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
 * @author Felipe
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class VirtualSensor extends Model<VirtualSensor> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private VirtualSensorType type;

    @OneToMany(mappedBy = "virtualSensor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Field> fields;

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

    public VirtualSensorType getType() {
        return type;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void updateFields(String fieldName, String newValue, DataType dataType) {
        boolean isUpdated = false;

        for (Field field : fields) {
            if (field.getName().equals(fieldName) && field.getDefaultDataType().equals(dataType)) {
                field.addValue(newValue);
                isUpdated = true;
            }
        }

        if (isUpdated) {
            update();
        }
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
}
