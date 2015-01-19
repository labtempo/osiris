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
package br.uff.labtempo.osiris.thirdparty.scheduler.core;
import br.uff.labtempo.osiris.thirdparty.scheduler.Scheduler;
import br.uff.labtempo.osiris.thirdparty.scheduler.SchedulerItem;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class SchedulerProducer<T>  implements Scheduler<T>{

    private final BlockingQueue<SchedulerItem<T>> queue;

    SchedulerProducer(BlockingQueue<SchedulerItem<T>> queue) {
        this.queue = queue;
    }
  
    @Override
    public void schedule(SchedulerItem<T> item) {        
        try {
            queue.put(item);
        } catch (InterruptedException ex) {
            Logger.getLogger(SchedulerProducer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
