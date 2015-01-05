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
package br.uff.labtempo.osiris.sensornet.thirdparty.scheduler;

import br.uff.labtempo.osiris.sensornet.model.jpa.Sensor;
import br.uff.labtempo.osiris.sensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.sensornet.persistence.SchedulerDao;
import br.uff.labtempo.osiris.sensornet.persistence.jpa.DataManager;
import br.uff.labtempo.osiris.thirdparty.scheduler.core.SchedulingManager;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class SchedulerBootstrap implements AutoCloseable {

    private SchedulingManager<Sensor> manager;

    public SchedulerBootstrap(DataManager data, DaoFactory factory) throws Exception {
        try {
            SchedulerStorageSensor storage = new SchedulerStorageSensor(data);
            SchedulerCallbackSensor callback = new SchedulerCallbackSensor(factory);
            manager = new SchedulingManager<>(storage, callback);
            manager.initialize();
        } catch (Exception e) {
            close();
            throw e;
        }
    }

    @Override
    public void close() throws Exception {
        manager.close();
    }

    public SchedulerDao getScheduler() {
        return new SchedulerController(manager);
    }

}
