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
package br.uff.labtempo.osiris.to.common.definitions;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public enum Path {

    PROTOCOL("omcp://"),
    SEPARATOR("/"),
    //reference info 
    ID1(":k1"),
    ID2(":k2"),
    ID3(":k3"),
    ID4(":k4"),
    ID5(":k5"),
    NAMING_MODULE_SENSORNET("sensornet"),    
    NAMING_MODULE_VIRTUALSENSORNET("virtualsensornet"),
    NAMING_RESOURCE_NETWORK(""),
    NAMING_RESOURCE_COLLECTOR("collector"),
    NAMING_RESOURCE_SENSOR("sensor"),
    NAMING_RESOURCE_VIRTUALSENSOR("vsensor"),    
    NAMING_RESOURCE_HISTORY("history"),
    NAMING_RESOURCE_LINK("link"),
    NAMING_RESOURCE_COMPOSITE("composite"),
    NAMING_RESOURCE_BLENDING("blending"),
    NAMING_RESOURCE_CONVERTER("converter"),
    NAMING_RESOURCE_DATATYPE("datatype"),
    NAMING_MESSAGEGROUP_COLLECTOR("collector.messagegroup"),    
    NAMING_MESSAGEGROUP_UPDATE("update.messagegroup"),    
    NAMING_MESSAGEGROUP_NOTIFICATION("notification.messagegroup"),
    NAMING_EXTRAMODULE_MESSAGEGROUP("messagegroup"),    
    NAMING_EXTRAMODULE_APPLICATION_FUNCTION("function"),
    NAMING_EXTRAMODULE_APPLICATION_MODULE("external"), 
    NAMING_EXTRAMODULE_SERVICE("service"),
    //to dyamic use(filter)
    RESOURCE_SENSORNET_NETWORK_All("/"),
    RESOURCE_SENSORNET_NETWORK_BY_ID("/:k1"),
    RESOURCE_SENSORNET_NETWORK_SENSOR_All("/:k1/sensor/"),
    RESOURCE_SENSORNET_NETWORK_SENSOR_BY_ID("/:k1/sensor/:k2"),
    RESOURCE_SENSORNET_COLLECTOR_All("/:k1/collector/"),
    RESOURCE_SENSORNET_COLLECTOR_BY_ID("/:k1/collector/:k2"),
    RESOURCE_SENSORNET_SENSOR_All("/:k1/collector/:k2/sensor/"),
    RESOURCE_SENSORNET_SENSOR_BY_ID("/:k1/collector/:k2/sensor/:k3"),
    RESOURCE_VIRTUALSENSORNET_VIRTUALSENSOR_ALL("/vsensor/"),
    RESOURCE_VIRTUALSENSORNET_VIRTUALSENSOR_BY_ID("/vsensor/:k1"),
    RESOURCE_VIRTUALSENSORNET_VIRTUALSENSOR_HISTORY_BY_ID("/vsensor/:k1/history/"),
    RESOURCE_VIRTUALSENSORNET_LINK_ALL("/link/"),
    RESOURCE_VIRTUALSENSORNET_LINK_BY_ID("/link/:k1"),
    RESOURCE_VIRTUALSENSORNET_COMPOSITE_ALL("/composite/"),
    RESOURCE_VIRTUALSENSORNET_COMPOSITE_BY_ID("/composite/:k1"),
    RESOURCE_VIRTUALSENSORNET_BLENDING_ALL("/blending/"),
    RESOURCE_VIRTUALSENSORNET_BLENDING_BY_ID("/blending/:k1"),
    RESOURCE_VIRTUALSENSORNET_CONVERTER_ALL("/converter/"),
    RESOURCE_VIRTUALSENSORNET_CONVERTER_BY_ID("/converter/:k1"),
    RESOURCE_VIRTUALSENSORNET_DATATYPE_ALL("/datatype/"),
    RESOURCE_VIRTUALSENSORNET_DATATYPE_BY_ID("/datatype/:k1"),
    //to static use(request)
    MODULE_SENSORNET("omcp://sensornet/"),
    MODULE_VIRTUALSENSORNET("omcp://virtualsensornet/"),
    MESSAGEGROUP_COLLECTOR("omcp://collector.messagegroup/"),    
    MESSAGEGROUP_COLLECTOR_ALL("omcp://collector.messagegroup/#"),
    MESSAGEGROUP_UPDATE("omcp://update.messagegroup/"),
    MESSAGEGROUP_UPDATE_ALL("omcp://update.messagegroup/#"),
    MESSAGEGROUP_NOTIFICATION("omcp://notification.messagegroup/"),
    MESSAGEGROUP_NOTIFICATION_All("omcp://notification.messagegroup/#");

    private Path(String content) {
        this.content = content;
    }

    private final String content;

    @Override
    public String toString() {
        return content;
    }
}
