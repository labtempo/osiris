/*
 * Copyright 2015 Felipe Santos <feliperalph at hotmail.com>.
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
package br.uff.labtempo.osiris.virtualsensornet.model.interfaces;

import br.uff.labtempo.osiris.to.virtualsensornet.VirtualSensorType;
import br.uff.labtempo.osiris.to.virtualsensornet.VirtualSensorVsnTo;
import br.uff.labtempo.osiris.virtualsensornet.model.Field;
import br.uff.labtempo.osiris.virtualsensornet.model.Revision;
import br.uff.labtempo.osiris.virtualsensornet.model.util.FieldListManager;
import br.uff.labtempo.osiris.virtualsensornet.model.util.field.FieldValuesWrapper;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public interface IVirtualSensor<T> extends IModel {

    long getId();

    String getLabel();

    void setLabel(String label);

    long getCreationTimestampInMillis();

    long getAcquisitionTimestampInMillis();

    long getStorageTimestampInMillis();

    TimeUnit getCreationIntervalTimeUnit();

    int getCreationPrecisionInNano();

    long getCreationInterval();

    List<Field> getFields();

    boolean setFieldsValues(FieldValuesWrapper valuesWrapper);

    void setCreationInterval(long creationInterval, TimeUnit timeUnit);

    boolean updateFields(FieldListManager listManager);

    VirtualSensorType getVirtualSensorType();

    VirtualSensorVsnTo getTransferObject();

    T getUniqueTransferObject();
    
    Revision getLastRevision();
}
