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

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class SchedulerTimeAlarm implements Runnable, AutoCloseable {

    private long intervalInMillis;
    private TaskCallback callback;
    private Thread thread;
    private boolean running;

    public SchedulerTimeAlarm(long initialIntervalInMillis, TaskCallback callback) {
        this.intervalInMillis = initialIntervalInMillis;
        this.callback = callback;
    }

    public SchedulerTimeAlarm(long duration, TimeUnit unit, TaskCallback callback) {
        this(TimeUnit.MILLISECONDS.convert(duration, unit), callback);
    }

    public void start() {
        thread = new Thread(this, "Alarm");
        thread.isDaemon();
        thread.start();

    }

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                Thread.sleep(intervalInMillis);
                callback.callback();
            } catch (InterruptedException ex) {
            }
        }
    }

    public void setInterval(long duration, TimeUnit unit) {
        intervalInMillis = TimeUnit.MILLISECONDS.convert(duration, unit);
    }

    public void setIntervalInMillis(long intervalInMillis) {
        this.intervalInMillis = intervalInMillis;
    }

    public long getIntervalInMillis() {
        return intervalInMillis;
    }

    @Override
    public void close() throws Exception {
        if (thread != null) {
            running = false;
            thread.interrupt();
            thread.join();
        }
    }
}
