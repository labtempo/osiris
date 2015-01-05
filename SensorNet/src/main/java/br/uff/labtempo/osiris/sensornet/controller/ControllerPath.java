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
package br.uff.labtempo.osiris.sensornet.controller;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public enum ControllerPath {

    PROTOCOL("omcp://"),
    NETWORK_KEY(":nid"),
    COLLECTOR_KEY(":cid"),
    SENSOR_KEY(":sid"),
    SEPARATOR("/"),
    NETWORK_ALL(SEPARATOR.toString()),
    NETWORK_BY_ID(SEPARATOR.toString() + NETWORK_KEY),
    COLLECTOR_UNIT(SEPARATOR.toString() + "collectors" + SEPARATOR),
    COLLECTOR_ALL(NETWORK_BY_ID.toString() + COLLECTOR_UNIT),
    COLLECTOR_BY_ID(COLLECTOR_ALL.toString() + COLLECTOR_KEY),
    SENSOR_UNIT(SEPARATOR.toString() + "sensors" + SEPARATOR),    
    SENSOR_ALL_BY_COLLECTOR(COLLECTOR_BY_ID.toString() + SENSOR_UNIT),
    SENSOR_ALL_BY_NETWORK(NETWORK_BY_ID.toString() + SENSOR_UNIT),
    SENSOR_BY_ID(SENSOR_ALL_BY_COLLECTOR.toString() + SENSOR_KEY),
    COLLECTOR_MESSAGEGROUP("collector.messagegroup"),
    UPDATE_MESSAGEGROUP("update.messagegroup"),
    NOTIFICATION_MESSAGEGROUP("notification.messagegroup");

    private ControllerPath(String content) {
        this.content = content;
    }

    private String content;

    @Override
    public String toString() {
        return content;
    }
}
