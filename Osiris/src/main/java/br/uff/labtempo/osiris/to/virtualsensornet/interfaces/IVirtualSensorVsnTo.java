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
package br.uff.labtempo.osiris.to.virtualsensornet.interfaces;

import br.uff.labtempo.osiris.to.common.data.ValueTo;
import br.uff.labtempo.osiris.to.common.definitions.State;
import br.uff.labtempo.osiris.to.virtualsensornet.VirtualSensorType;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public interface IVirtualSensorVsnTo {

    long getId();
    
    String getLabel();

    long getCreationTimestampInMillis();

    long getAcquisitionTimestampInMillis();

    long getStorageTimestampInMillis();

    BigInteger getCreationTimestampInNano();

    int getCreationPrecisionInNano();

    long getCreationInterval();

    TimeUnit getCreationIntervalTimeUnit();

    State getState();

    Calendar getLastModified();

    VirtualSensorType getSensorType();

    List<? extends ValueTo> getValuesTo();

}
