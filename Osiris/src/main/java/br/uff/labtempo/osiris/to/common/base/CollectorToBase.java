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

    private final long captureInterval;
    private final String captureIntervaltimeUnit;

    //helper attributes
    private transient TimeUnit helperCaptureIntervalTimeUnit;

    public CollectorToBase(String id, State state, long captureInterval, TimeUnit captureIntervaltimeUnit) {
        super(id, state);
        this.captureInterval = captureInterval;
        this.captureIntervaltimeUnit = captureIntervaltimeUnit.toString();

        this.helperCaptureIntervalTimeUnit = captureIntervaltimeUnit;
    }

    public long getCaptureInterval() {
        return captureInterval;
    }

    public TimeUnit getCaptureIntervalTimeUnit() {
        if (helperCaptureIntervalTimeUnit == null) {
            helperCaptureIntervalTimeUnit = Enum.valueOf(TimeUnit.class, captureIntervaltimeUnit.toUpperCase());
        }
        return helperCaptureIntervalTimeUnit;
    }
}
