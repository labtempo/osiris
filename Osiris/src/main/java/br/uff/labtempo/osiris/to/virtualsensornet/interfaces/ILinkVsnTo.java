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

import br.uff.labtempo.osiris.to.common.data.FieldTo;
import java.util.List;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public interface ILinkVsnTo {

    long getId();

    String getLabel();

    String getSensorId();

    String getNetworkId();

    String getCollectorId();

    List<? extends FieldTo> getFields();

    void createField(String name, long dataTypeId, long converterId);

    void createField(long id, String name, long dataTypeId, long converterId);

    void createField(long id, String name, long dataTypeId, long converterId, boolean initialized, long sourceId, int aggregates, int dependents);

    void createField(String name, long dataTypeId);

    void createField(long id, String name, long dataTypeId);

    //edit
    void setLabel(String label);

    void setSensorId(String sensorId);

    void setNetworkId(String NetworkId);

    void setCollectorId(String collectorId);

    void removeFields();
}
