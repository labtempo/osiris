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
package br.uff.labtempo.osiris.to.sensornet;

import br.uff.labtempo.osiris.to.sensornet.interfaces.ICollectorSnTo;
import br.uff.labtempo.osiris.to.common.base.CollectorToBase;
import br.uff.labtempo.osiris.to.common.definitions.State;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class CollectorSnTo extends CollectorToBase implements ICollectorSnTo {


    private final long lastModified;
    private final String networkId;
    private final int totalSensors;

    //helper attributes
    private transient Calendar helperLastModified;

    public CollectorSnTo( String id, State state, long interval, TimeUnit timeUnit, Calendar lastModifiedDate, String networkId, int totalSensors) {
        super(id, state, interval, timeUnit);
        this.lastModified = lastModifiedDate.getTimeInMillis();
        this.networkId = networkId;
        this.totalSensors = totalSensors;

        this.helperLastModified = lastModifiedDate;
    }

    @Override
    public Calendar getLastModified() {
        if (helperLastModified == null) {
            helperLastModified = Calendar.getInstance();
            helperLastModified.setTimeInMillis(lastModified);
        }
        return helperLastModified;
    }

    @Override
    public String getNetworkId() {
        return networkId;
    }

    @Override
    public int getTotalSensors() {
        return totalSensors;
    }

    @Override
    public void addInfo(String infoKeyName, String infoDescription) {
        super.addInfo(infoKeyName, infoDescription);
    }

    @Override
    public void addInfo(Map<String, String> infos) {
        super.addInfo(infos);
    }

    @Override
    public Map<String, String> getInfo() {
        return super.getInfo();
    }
}
