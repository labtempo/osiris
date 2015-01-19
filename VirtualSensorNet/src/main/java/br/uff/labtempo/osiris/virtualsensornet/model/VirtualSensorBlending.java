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
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
/**
 * Post to create
 *
 * Put to update value
 *
 * Delete to remove BlendingVSensor from virtualsensornet
 */
public class VirtualSensorBlending extends VirtualSensor {

    private List<VirtualSensor> sources;
    private Function function;

    public VirtualSensorBlending(List<Field> fields, long interval, TimeUnit intervalTimeUnit) {
        super(VirtualSensorType.BLENDING, fields,interval,intervalTimeUnit);
    }
}
