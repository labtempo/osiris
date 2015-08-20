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
package br.uff.labtempo.osiris.virtualsensornet.persistence;

import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensorLink;
import java.util.List;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public interface LinkDao extends Dao<VirtualSensorLink> {

    public VirtualSensorLink get(long id);

    public VirtualSensorLink get(VirtualSensorLink sensorLink);

    public List<VirtualSensorLink> getAll();

    public List<VirtualSensorLink> getAllByReferences(String networkId, String collectorId, String sensorId);
    
    public void commit();
}
