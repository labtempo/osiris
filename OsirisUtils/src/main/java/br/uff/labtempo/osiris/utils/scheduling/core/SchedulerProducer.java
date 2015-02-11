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
package br.uff.labtempo.osiris.utils.scheduling.core;
import br.uff.labtempo.osiris.utils.scheduling.Scheduler;
import br.uff.labtempo.osiris.utils.scheduling.SchedulerItem;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class SchedulerProducer  implements Scheduler{

    private final BlockingQueue<SchedulerItem> queue;

    SchedulerProducer(BlockingQueue<SchedulerItem> queue) {
        this.queue = queue;
    }
  
    @Override
    public void schedule(SchedulerItem item) {        
        try {
            queue.put(item);
        } catch (InterruptedException ex) {
            Logger.getLogger(SchedulerProducer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
