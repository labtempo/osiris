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
import br.uff.labtempo.osiris.utils.scheduling.SchedulingStorage;
import br.uff.labtempo.osiris.utils.scheduling.Scheduling;
import br.uff.labtempo.osiris.utils.scheduling.SchedulingCallback;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class SchedulingManager implements Scheduling, TaskCallback {

    private final BlockingQueue<SchedulerItem> queue;
    private final SchedulingStorage storage;
    private final SchedulingCallback callback;
    private SchedulerConsumer scheduler;
    private SchedulerTimeAlarm alarm;
    private final TimeControl timeControl;

    public SchedulingManager(SchedulingStorage storage, SchedulingCallback callback, long intervalDuration, TimeUnit timeUnit) {
        this.queue = new LinkedBlockingQueue<>();
        this.storage = storage;
        this.callback = callback;
        long intervalInMillis = TimeUnit.MILLISECONDS.convert(intervalDuration, timeUnit);

        this.timeControl = new TimeControl(intervalInMillis);
    }

    @Override
    public void initialize() {
        if (alarm == null) {
            alarm = new SchedulerTimeAlarm(timeControl.getIntervalInMillis(), this);
            scheduler = new SchedulerConsumer(queue, this);
            scheduler.start();
            alarm.start();
        }
        callback();
    }

    @Override
    public void close() throws Exception {
        scheduler.close();
        alarm.close();
    }

    @Override
    public Scheduler getScheduler() {
        return new SchedulerProducer(queue);
    }

    @Override
    public void callback() {
        Calendar calendar = Calendar.getInstance();
        long currentTimeInMillis = calendar.getTimeInMillis();
        List<? extends SchedulerItem> items = storage.getAllByTimeLimit(currentTimeInMillis);
        if (items != null && items.size() > 0) {
            for (SchedulerItem item : items) {
                if (item.isRemovable()) {
                    storage.delete(item);
                }
            }

            try {
                callback.callback(items);                
            } catch (Exception ex) {
                Logger.getLogger(SchedulingManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            for (SchedulerItem item : items) {
                if (item.isRemovable()) {
                    storage.update(item);
                }
            }
        }
    }

    void schedule(SchedulerItem item) {
        SchedulerItem storedItem = storage.getItemByObjectId(item.getObjectId());
        if (storedItem != null) {
            storedItem.updateTimeToNextUpdate(item.getTimeToNextUpdate());
            storedItem.updateIntervalInMillis(item.getIntervalInMillis());
            storage.update(storedItem);
        } else {
            storage.save(item);
        }

        long newInterval = item.getIntervalInMillis();

        if (timeControl.addInterval(newInterval)) {
            alarm.setIntervalInMillis(timeControl.getIntervalInMillis());
        }
    }
}
