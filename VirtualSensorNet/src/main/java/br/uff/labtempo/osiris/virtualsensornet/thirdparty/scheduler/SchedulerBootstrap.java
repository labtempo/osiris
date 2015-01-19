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
package br.uff.labtempo.osiris.virtualsensornet.thirdparty.scheduler;

import br.uff.labtempo.osiris.thirdparty.scheduler.Scheduler;
import br.uff.labtempo.osiris.thirdparty.scheduler.SchedulingCallback;
import br.uff.labtempo.osiris.thirdparty.scheduler.SchedulingStorage;
import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensor;
import br.uff.labtempo.osiris.thirdparty.scheduler.core.SchedulingManager;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class SchedulerBootstrap implements AutoCloseable {

    private SchedulingManager<VirtualSensor> manager;

    public SchedulerBootstrap(SchedulingStorage<VirtualSensor> schedulerDao, SchedulingCallback<VirtualSensor> callback) throws Exception {
        manager = new SchedulingManager<>(schedulerDao, callback, 10, TimeUnit.SECONDS);
    }

    public void start() {
        try {
            manager.initialize();
        } catch (Exception e) {
            try {
                close();
            } catch (Exception ex) {                
            }
            throw e;
        }
    }

    @Override
    public void close() throws Exception {
        manager.close();
    }

    public Scheduler<VirtualSensor> getScheduler() {
        return  manager.getScheduler();
    }

}
