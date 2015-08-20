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
package br.uff.labtempo.tlauncher.data;

import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.osiris.to.collector.CollectorCoTo;
import br.uff.labtempo.osiris.to.collector.NetworkCoTo;
import br.uff.labtempo.osiris.to.collector.SampleCoTo;
import br.uff.labtempo.osiris.to.collector.SensorCoTo;
import static br.uff.labtempo.osiris.to.common.definitions.Path.*;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public abstract class DataBuilder {

    private final NetworkCoTo networkCoTo;
    private final CollectorCoTo collectorCoTo;
    private long timestamp; 

    public DataBuilder(String collectorId) {
        this.networkCoTo = getNetwork(DataBase.NETWORK_ID);
        this.collectorCoTo = getCollector(collectorId);
    }
    
    public abstract void publishSample(String sensorId, int messageId);

    public SampleCoTo getSample(String sensorId, int messageId) {
        SampleCoTo sample = new SampleCoTo(networkCoTo, collectorCoTo, getSensor(sensorId, messageId));
        return sample;
    }

    public String makeUrl() {
        String collectorId = collectorCoTo.getId();
        String url = MESSAGEGROUP_COLLECTOR.toString() + DataBase.NETWORK_ID + SEPARATOR + collectorId + SEPARATOR;
        return url;
    }

    public String makeUrl(String sensorId) {
        String url = makeUrl() + sensorId;
        return url;
    }

    private SensorCoTo getSensor(String sensorId, int messageId) {
        SensorCoTo sensorTo = new SensorCoTo(sensorId, System.currentTimeMillis(), (int)System.nanoTime());
        sensorTo.addValue(DataBase.DATA_NAME, messageId, DataBase.DATA_UNIT, DataBase.DATA_SYMBOL);
        return sensorTo;
    }

    private NetworkCoTo getNetwork(String networkId) {
        NetworkCoTo networkCoTo = new NetworkCoTo(networkId);
        return networkCoTo;
    }

    private CollectorCoTo getCollector(String collectorId) {
        CollectorCoTo collectorCoTo = new CollectorCoTo(collectorId, 10, TimeUnit.SECONDS);
        return collectorCoTo;
    }
}
