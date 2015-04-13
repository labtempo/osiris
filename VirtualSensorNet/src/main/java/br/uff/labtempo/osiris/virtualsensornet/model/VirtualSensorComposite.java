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

import br.uff.labtempo.osiris.virtualsensornet.model.interfaces.IComposite;
import br.uff.labtempo.osiris.to.virtualsensornet.CompositeVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.VirtualSensorType;
import br.uff.labtempo.osiris.virtualsensornet.model.state.ModelState;
import br.uff.labtempo.osiris.virtualsensornet.model.util.FieldListManager;
import br.uff.labtempo.osiris.virtualsensornet.model.util.field.FieldValuesWrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.persistence.Entity;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
@Entity
public class VirtualSensorComposite extends VirtualSensor<CompositeVsnTo> implements IComposite {

    public VirtualSensorComposite() {
    }

    public VirtualSensorComposite(String label, List<Field> fields, long creationInterval, TimeUnit creationIntervalTimeUnit) {
        super(VirtualSensorType.COMPOSITE, label, fields, creationInterval, creationIntervalTimeUnit);
        //add as aggregate
        for (Field field : fields) {
            field.addAggregate(this);
        }
    }

    @Override
    public boolean isAggregated() {
        return true;
    }

    @Override
    public VirtualSensor getVirtualSensor() {
        return this;
    }

    @Override
    public void removeFields() {
        List<Field> fields = getFields();
        synchronized (fields) {
            List<Field> tempFields = new ArrayList<>(getFields());
            for (Field field : tempFields) {
                field.removeAggregate(this);
                fields.remove(field);
            }
        }
    }

    @Override
    public boolean setFieldsValues(FieldValuesWrapper valuesWrapper) {
        boolean isError = false;
        boolean isWorking = false;

        Field latestUpdatedField = null;
        Field minorIntervalField = null;

        for (Field field : getFields()) {
            VirtualSensor sensor = field.getVirtualSensor();

            latestUpdatedField = compareLatestUpdatedSensor(latestUpdatedField, field);
            minorIntervalField = compareMinorUpdateIntervalSensor(minorIntervalField, field);

            //select operation state of the composite 
            if (sensor.getModelState() == ModelState.INACTIVE
                    || sensor.getModelState() == ModelState.MALFUNCTION) {
                isError = true;
            } else {
                isWorking = true;
            }
        }

        final VirtualSensor latestUpdatedSensor = latestUpdatedField.getVirtualSensor();
        final VirtualSensor minorIntervalSensor = minorIntervalField.getVirtualSensor();

        //put new time values
        if (isWorking) {
            FieldValuesWrapper<Object> wrapper = new FieldValuesWrapper<Object>() {

                @Override
                public long getCreationTimestampInMillis() {
                    return latestUpdatedSensor.getCreationTimestampInMillis();
                }

                @Override
                public long getAcquisitionTimestampInMillis() {
                    return latestUpdatedSensor.getAcquisitionTimestampInMillis();
                }

                @Override
                public TimeUnit getCreationIntervalTimeUnit() {
                    return minorIntervalSensor.getCreationIntervalTimeUnit();
                }

                @Override
                public int getCreationPrecisionInNano() {
                    return latestUpdatedSensor.getCreationPrecisionInNano();
                }

                @Override
                public long getCreationInterval() {
                    return minorIntervalSensor.getCreationInterval();
                }

                @Override
                public List<Object> getValues() {
                    return null;
                }
            };
            super.setFieldsValues(valuesWrapper);
        }

        //reset operation state of the composite to reflect new state properly
        if (isWorking && !isError) {
            super.update();
            return true;
        } else if (isWorking && isError) {
            super.malfunction();
            return true;
        } else if (!isWorking) {
            super.deactivate();
            return true;
        }
        return false;
    }

    @Override
    public boolean updateFields(FieldListManager listManager) {
        List<Field> fields = getFields();
        synchronized (fields) {
            boolean updated = listManager.doAddRemove(fields);
            if (updated) {
                update();
            }
            return updated;
        }
    }

    @Override
    public CompositeVsnTo getUniqueTransferObject() {
        CompositeVsnTo compositeVsnTo = new CompositeVsnTo(getId(), getLabel());
        List<Field> fields = getFields();
        for (Field field : fields) {
            VirtualSensor sensor = field.getVirtualSensor();
            int aggregates = field.getAggregates().size();
            compositeVsnTo.addBoundField(field.getId(), field.getReferenceName(), field.getDataTypeId(), field.getConverterId(), field.isStored(), sensor.getId(), aggregates);
        }
        return compositeVsnTo;
    }

    private static Field compareMinorUpdateIntervalSensor(Field currentField, Field newField) {
        VirtualSensor currentSensor = null;
        VirtualSensor newSensor = null;

        if (currentField == null && newField == null) {
            return null;
        }

        if (newField == null) {
            return currentField;
        }

        if (currentField != null) {
            currentSensor = currentField.getVirtualSensor();
        }
        newSensor = newField.getVirtualSensor();

        //sensor not evaluated(discarded)
        if (newSensor.getModelState() == ModelState.INACTIVE) {
            return currentField;
        }

        //initialize null
        if (currentField == null || currentSensor == null) {
            return newField;
        }

        //not equals timeUnit: select the smallest unit
        if (currentSensor.getCreationIntervalTimeUnit() != newSensor.getCreationIntervalTimeUnit()) {
            TimeUnit currentTimeUnit = currentSensor.getCreationIntervalTimeUnit();
            TimeUnit newTimeUnit = newSensor.getCreationIntervalTimeUnit();
            if (currentTimeUnit.compareTo(newTimeUnit) > 0) {
                return newField;
            }
            return currentField;
        }

        //select the smallest interval 
        if (currentSensor.getCreationInterval() < newSensor.getCreationInterval()) {
            return newField;
        }

        return currentField;
    }

    private static Field compareLatestUpdatedSensor(Field currentField, Field newField) {
        VirtualSensor currentSensor = null;
        VirtualSensor newSensor = null;

        if (currentField == null && newField == null) {
            return null;
        }

        if (newField == null) {
            return currentField;
        }

        if (currentField != null) {
            currentSensor = currentField.getVirtualSensor();
        }
        newSensor = newField.getVirtualSensor();

        //sensor not evaluated(discarded)
        if (newSensor.getModelState() == ModelState.INACTIVE) {
            return currentField;
        }

        //initialize null
        if (currentField == null || currentSensor == null) {
            return newField;
        }

        //select latest updated field
        if (currentSensor.getCreationTimestampInMillis() < newSensor.getCreationTimestampInMillis()) {
            return newField;
        }

        //if equals, select by precision 
        if (currentSensor.getCreationTimestampInMillis() == newSensor.getCreationTimestampInMillis()
                && currentSensor.getCreationPrecisionInNano() < newSensor.getCreationPrecisionInNano()) {
            return newField;
        }

        return currentField;
    }

    public static Field selectMinorUpdateIntervalSensor(List<Field> fields) {
        Field currentField = null;
        for (Field field : fields) {
            currentField = compareMinorUpdateIntervalSensor(currentField, field);
        }
        return currentField;
    }
}
