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

import br.uff.labtempo.osiris.thirdparty.scheduler.SchedulerItem;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
class SchedulerConsumer<T> implements Runnable, AutoCloseable {

    private final BlockingQueue<SchedulerItem<T>> queue;
    private final SchedulingManager<T> manager;
    private Thread thread;

    SchedulerConsumer(BlockingQueue<SchedulerItem<T>> queue, SchedulingManager<T> manager) {
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
                SchedulerItem<T> schedulerItem = queue.take();
                if (schedulerItem != null && schedulerItem.getObject() != null) {
                    schedule(schedulerItem);
                } else {
                    break;
                }
            } catch (InterruptedException ex) {
                break;
            }
        }       
    }

    private void schedule(SchedulerItem<T> item) {
        manager.schedule(item);
    }

    @Override
    public void close() throws Exception {
        SchedulerItem<T> item = new SchedulerItem<T>() {

            @Override
            public long getTimeToNextUpdate() {
                return 0;
            }

            @Override
            public T getObject() {
                return null;
            }

            @Override
            public void updateTimeToNextUpdate(long timeToNextUpdate) {
            }

            @Override
            public long getIntervalInMillis() {
                return 0;
            }
        };
        queue.add(item);
        if (thread != null) {
            thread.join();
        }
    }

}
