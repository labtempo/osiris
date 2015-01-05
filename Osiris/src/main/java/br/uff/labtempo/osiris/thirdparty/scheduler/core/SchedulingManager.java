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
import br.uff.labtempo.osiris.thirdparty.scheduler.SchedulingStorage;
import br.uff.labtempo.osiris.thirdparty.scheduler.Scheduling;
import br.uff.labtempo.osiris.thirdparty.scheduler.SchedulingCallback;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class SchedulingManager<T> implements Scheduling<T>, TaskCallback {

    private final SchedulingStorage<T> storage;
    private final SchedulingCallback callback;
    private long intervalInMillis;
    private int countToSwapInterval;
    private Timer timer;

    public SchedulingManager(SchedulingStorage<T> storage, SchedulingCallback callback) {
        this.storage = storage;
        this.callback = callback;
        this.timer = new Timer("Initial Timer");
        this.intervalInMillis = TimeUnit.MILLISECONDS.convert(10, TimeUnit.MINUTES);
    }

    @Override
    public void initialize() {
        callback();
        createScheduling();
    }

    @Override
    public void close() throws Exception {
        timer.cancel();
        timer.purge();
    }

    @Override
    public void schedule(SchedulerItem<T> item) {
        SchedulerItem<T> storedItem = storage.getItemByObject(item.getObject());
        if (storedItem != null) {
            storedItem.updateTimeToNextUpdate(item.getTimeToNextUpdate());
            storage.update(storedItem);
        } else {
            storage.save(item);
        }

        long newInterval = item.getIntervalInMillis();

        //interval updating
        if (newInterval != intervalInMillis) {
            if (++countToSwapInterval > 10) {
                intervalInMillis = newInterval;
                changeSchedulingInterval();
                countToSwapInterval = 0;
            }
        } else {
            countToSwapInterval = 0;
        }
    }

    private void changeSchedulingInterval() {
        timer.cancel();
        timer = new Timer("Timer at " + intervalInMillis);
        createScheduling();
    }

    private void createScheduling() {
        timer.schedule(new Task(this), intervalInMillis, intervalInMillis);
    }

    @Override
    public void callback() {
        Calendar calendar = Calendar.getInstance();
        long currentTimeInMillis = calendar.getTimeInMillis();
        List<? extends SchedulerItem<T>> items = storage.getAllByTimeLimit(currentTimeInMillis);
        if (items != null) {
            for (SchedulerItem<T> item : items) {
                storage.delete(item);
            }
        }
        try {
            callback.callback(items);
        } catch (Exception ex) {
            Logger.getLogger(SchedulingManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
