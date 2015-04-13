/*
 * Copyright 2015 Felipe.
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

import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensor;
import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensorComposite;
import java.util.List;

/**
 *
 * @author Felipe
 */
public interface CompositeDao extends Dao<VirtualSensorComposite> {

    void update(VirtualSensor sensor);

    VirtualSensorComposite getById(long id);

    VirtualSensorComposite get(VirtualSensorComposite sensorComposite);

    List<VirtualSensorComposite> getAll();
}
