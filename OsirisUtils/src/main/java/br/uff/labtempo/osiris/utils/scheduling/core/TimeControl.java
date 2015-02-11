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

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class TimeControl {

    private long intervalInMillis;
    private long viceIntervalInMillis;
    private short countToDownshift;

    public TimeControl(long intervalInMillis) {
        this.intervalInMillis = intervalInMillis;
    }

    public boolean addInterval(long newIntervalInMillis) {
        //interval updating
        if (newIntervalInMillis != intervalInMillis) {
            if (intervalInMillis > newIntervalInMillis) {
                upshiftInterval(newIntervalInMillis);
                return true;
            } else if (newIntervalInMillis > intervalInMillis) {
                if (newIntervalInMillis == viceIntervalInMillis) {
                    countToDownshift++;
                    if (countToDownshift > 10) {
                        downshiftInterval();
                        return true;
                    }
                } else if (newIntervalInMillis > intervalInMillis) {
                    if (newIntervalInMillis < viceIntervalInMillis || viceIntervalInMillis == 0) {
                        viceIntervalInMillis = newIntervalInMillis;
                        countToDownshift = 0;
                    }
                }
            }
        } else {
            countToDownshift = 0;
        }
        return false;
    }

    private synchronized void upshiftInterval(long newInterval) {
        viceIntervalInMillis = intervalInMillis;
        intervalInMillis = newInterval;
        countToDownshift = 0;
    }

    private synchronized void downshiftInterval() {
        intervalInMillis = viceIntervalInMillis;
        viceIntervalInMillis = 0;
        countToDownshift = 0;
    }

    public long getIntervalInMillis() {
        return intervalInMillis;
    }
}
