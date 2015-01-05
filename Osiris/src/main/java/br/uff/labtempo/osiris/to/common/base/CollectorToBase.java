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
package br.uff.labtempo.osiris.to.common.base;

import br.uff.labtempo.osiris.to.common.definitions.State;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public abstract class CollectorToBase extends ToBaseInfo {

    private final long interval;
    private final String timeUnit;

    //helper attributes
    private transient TimeUnit helperTimeUnit;

    public CollectorToBase(String id, State state, long interval, TimeUnit timeUnit) {
        super(id, state);
        this.interval = interval;
        this.timeUnit = timeUnit.toString();

        this.helperTimeUnit = timeUnit;
    }

    public long getInterval() {
        return interval;
    }

    public TimeUnit getTimeUnit() {
        if (helperTimeUnit == null) {
            helperTimeUnit = Enum.valueOf(TimeUnit.class, timeUnit);
        }
        return helperTimeUnit;
    }
}
