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

import br.uff.labtempo.osiris.utils.scheduling.SchedulerItem;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
class SchedulerConsumer implements Runnable, AutoCloseable {

    private final BlockingQueue<SchedulerItem> queue;
    private final SchedulingManager manager;
    private Thread thread;

    SchedulerConsumer(BlockingQueue<SchedulerItem> queue, SchedulingManager manager) {
        this.queue = queue;
        this.manager = manager;
    }

    public void start() {
        thread = new Thread(this, "Scheduler consumer");
        thread.setDaemon(true);
        thread.setPriority(2);
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                SchedulerItem schedulerItem = queue.take();
                if (schedulerItem != null && schedulerItem.getObjectId() != -1) {
                    schedule(schedulerItem);
                } else {
                    break;
                }
            } catch (InterruptedException ex) {
                break;
            }
        }       
    }

    private void schedule(SchedulerItem item) {
        manager.schedule(item);
    }

    @Override
    public void close() throws Exception {
        SchedulerItem item = new SchedulerItem() {

            @Override
            public long getTimeToNextUpdate() {
                return -1;
            }

            @Override
            public long getObjectId() {
                return -1;
            }

            @Override
            public void updateTimeToNextUpdate(long timeToNextUpdate) {
            }

            @Override
            public long getIntervalInMillis() {
                return -1;
            }
        };
        queue.add(item);
        if (thread != null) {
            thread.join();
        }
    }

}
