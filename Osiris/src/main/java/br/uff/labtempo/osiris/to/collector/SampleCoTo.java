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
package br.uff.labtempo.osiris.to.collector;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class SampleCoTo {
    private final NetworkCoTo network;
    private final CollectorCoTo collector;
    private final SensorCoTo sensor;

    public SampleCoTo(NetworkCoTo network, CollectorCoTo collector, SensorCoTo sensor) {
        this.sensor = sensor;
        this.collector = collector;
        this.network = network;
    }

    public NetworkCoTo getNetwork() {
        return network;
    }

    public CollectorCoTo getCollector() {
        return collector;
    }

    public SensorCoTo getSensor() {
        return sensor;
    }
}
