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
package br.uff.labtempo.osiris.virtualsensornet.controller;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public enum ControllerPath {

    //URL ITEMS
//URL ITEMS
    PROTOCOL("omcp://"),
    SEPARATOR("/"),
    //KEYS OF CONTROLLER FILTER
    NETWORK_KEY(":nid"),
    COLLECTOR_KEY(":cid"),
    SENSOR_KEY(":sid"),
    VIRTUAL_KEY(":vsid"),
    FIELD_KEY(":fid"),
    //SENSORNET
    MODULE_SENSORNET(SEPARATOR.toString() + "sensornet"),
    //NETWORK
    NETWORK_ALL(SEPARATOR.toString()),
    NETWORK_BY_ID(SEPARATOR.toString() + NETWORK_KEY),
    //COLLECTOR
    COLLECTOR_UNIT(SEPARATOR.toString() + "collectors" + SEPARATOR),
    COLLECTOR_ALL(NETWORK_BY_ID.toString() + COLLECTOR_UNIT),
    COLLECTOR_BY_ID(COLLECTOR_ALL.toString() + COLLECTOR_KEY),
    //SENSOR
    SENSOR_UNIT(SEPARATOR.toString() + "sensors" + SEPARATOR),
    SENSOR_ALL_BY_COLLECTOR(COLLECTOR_BY_ID.toString() + SENSOR_UNIT),
    SENSOR_ALL_BY_NETWORK(NETWORK_BY_ID.toString() + SENSOR_UNIT),
    SENSOR_BY_ID(SENSOR_ALL_BY_COLLECTOR.toString() + SENSOR_KEY),
    //VIRTUAL SENSOR
    // omcp://virtualsensornet/vsensor/{id}
    VIRTUAL_SENSOR_ALL(SEPARATOR.toString() + "vsensor" + SEPARATOR),
    VIRTUAL_SENSOR_BY_ID(VIRTUAL_SENSOR_ALL.toString() + VIRTUAL_KEY),
    // omcp://virtualsensornet/vsensor/{id}/{fieldId}
    VIRTUAL_SENSOR_BY_ID_FIELD(VIRTUAL_SENSOR_ALL.toString() + VIRTUAL_KEY + SEPARATOR + FIELD_KEY),
    //LINK - VIRTUAL SENSOR
    // omcp://virtualsensornet/vslink/{id}
    VS_LINK_ALL(SEPARATOR.toString() + "vslink" + SEPARATOR),
    VS_LINK_BY_ID(VS_LINK_ALL.toString() + VIRTUAL_KEY),
    //COMPOSITE - VIRTUAL SENSOR
    // omcp://virtualsensornet/vscomposite/{id}
    VS_COMPOSITE_ALL(SEPARATOR.toString() + "vscomposite" + SEPARATOR),
    VS_COMPOSITE_BY_ID(VS_COMPOSITE_ALL.toString() + VIRTUAL_KEY),
    //BLENDING - VIRTUAL SENSOR
    // omcp://virtualsensornet/vsblending/{id}
    VS_BLENDING_ALL(SEPARATOR.toString() + "vsblending" + SEPARATOR),
    VS_BLENDING_BY_ID(VS_BLENDING_ALL.toString() + VIRTUAL_KEY),
    //DATATYPE - EXTRA
    // omcp://virtualsensornet/dtype/{id}
    DATATYPE_ALL(SEPARATOR.toString() + "dtype" + SEPARATOR),
    DATATYPE_BY_ID(DATATYPE_ALL.toString() + VIRTUAL_KEY),
    //DATA CONVERTER - EXTRA
    // omcp://virtualsensornet/dconverter/{id}
    DATA_CONVERTER_ALL(SEPARATOR.toString() + "dconverter" + SEPARATOR),
    DATA_CONVERTER_BY_ID(DATATYPE_ALL.toString() + VIRTUAL_KEY),
    //MESSAGE GROUPS
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
