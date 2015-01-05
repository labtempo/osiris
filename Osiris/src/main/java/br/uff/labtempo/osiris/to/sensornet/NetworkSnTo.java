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

import br.uff.labtempo.osiris.to.sensornet.interfaces.INetworkSnTo;
import br.uff.labtempo.osiris.to.common.base.NetworkToBase;
import br.uff.labtempo.osiris.to.common.definitions.State;
import java.util.Calendar;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class NetworkSnTo extends NetworkToBase implements INetworkSnTo {

    private final long lastModified;
    private final int totalCollectors;
    private final int totalSensors;

    //helper attributes
    private transient Calendar helperLastModified;

    public NetworkSnTo(String id, State state, Calendar lastModifiedDate, int totalCollectors, int totalSensors) {
        super(id, state);
        this.lastModified = lastModifiedDate.getTimeInMillis();
        this.totalCollectors = totalCollectors;
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
    public int getTotalCollectors() {
        return totalCollectors;
    }

    @Override
    public int getTotalSensors() {
        return totalSensors;
    }

    @Override
    public void addInfo(String infoKeyName, String infoDescription) {
        super.addInfo(infoKeyName, infoDescription); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addInfo(Map<String, String> infos) {
        super.addInfo(infos); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, String> getInfo() {
        return super.getInfo(); //To change body of generated methods, choose Tools | Templates.
    }

}
