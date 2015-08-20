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

    /*OMCP URI SCHEME*/
    PROTOCOL("omcp://"),
    SEPARATOR("/"),
    
    /*NAMING*/
    /*OMCP MODULE DOMAINS*/
    NAMING_MODULE_COLLECTOR("collector"),  
    NAMING_MODULE_SENSORNET("sensornet"),    
    NAMING_MODULE_VIRTUALSENSORNET("virtualsensornet"),
    NAMING_MODULE_SERVICE("service"),    
    NAMING_MODULE_FUNCTION("function"),
    NAMING_MODULE_EXTERNAL("external"),
    
    /*MODULE RESOURCES*/
    NAMING_RESOURCE_NETWORK(""),
    NAMING_RESOURCE_COLLECTOR("collector"),
    NAMING_RESOURCE_SENSOR("sensor"),
    NAMING_RESOURCE_VIRTUALSENSOR("vsensor"),    
    NAMING_RESOURCE_REVISIONS("revisions"),
    NAMING_RESOURCE_LINK("link"),
    NAMING_RESOURCE_COMPOSITE("composite"),
    NAMING_RESOURCE_BLENDING("blending"),
    NAMING_RESOURCE_CONVERTER("converter"),
    NAMING_RESOURCE_DATATYPE("datatype"),
    NAMING_RESOURCE_FUNCTION("function"),
    NAMING_RESOURCE_SPOOL("spool"),
    NAMING_RESOURCE_INTERFACE("interface"),
    
    /*OMCP MESSAGE GROUP DOMAIN*/
    NAMING_MESSAGEGROUP("messagegroup"),
    
    /*NATIVE MESSAGE GROUP DOMAINS*/ 
    NAMING_MESSAGEGROUP_COLLECTOR("collector.messagegroup"),    
    NAMING_MESSAGEGROUP_UPDATE("update.messagegroup"),    
    NAMING_MESSAGEGROUP_NOTIFICATION("notification.messagegroup"),
    
    /*URI QUERY-STRING PARAMS*/
    NAMING_QUERY_STRING_LIMIT("limit"),
    NAMING_QUERY_STRING_FROM_DATE("from"),
    NAMING_QUERY_STRING_TO_DATE("to"),
    
    /*CONTROLLER URI FILTERS*/
    /*KEYS*/
    ID1(":k1"),
    ID2(":k2"),
    ID3(":k3"),
    ID4(":k4"),
    ID5(":k5"),
    /*URI RESOURCE WITH WILDCARD*/
    RESOURCE_SENSORNET_NETWORK_All("/"),
    RESOURCE_SENSORNET_NETWORK_BY_ID("/:k1"),
    RESOURCE_SENSORNET_NETWORK_SENSOR_All("/:k1/sensor/"),
    RESOURCE_SENSORNET_NETWORK_SENSOR_BY_ID("/:k1/sensor/:k2"),
    RESOURCE_SENSORNET_COLLECTOR_All("/:k1/collector/"),
    RESOURCE_SENSORNET_COLLECTOR_BY_ID("/:k1/collector/:k2"),
    RESOURCE_SENSORNET_SENSOR_All("/:k1/collector/:k2/sensor/"),
    RESOURCE_SENSORNET_SENSOR_BY_ID("/:k1/collector/:k2/sensor/:k3"),
    RESOURCE_SENSORNET_SENSOR_REVISIONS_BY_ID("/:k1/collector/:k2/sensor/:k3/revisions/"),
    RESOURCE_VIRTUALSENSORNET_VIRTUALSENSOR_ALL("/vsensor/"),
    RESOURCE_VIRTUALSENSORNET_VIRTUALSENSOR_BY_ID("/vsensor/:k1"),
    RESOURCE_VIRTUALSENSORNET_VIRTUALSENSOR_REVISIONS_BY_ID("/vsensor/:k1/revisions/"),
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
    RESOURCE_VIRTUALSENSORNET_FUNCTION_ALL("/function/"),
    RESOURCE_VIRTUALSENSORNET_FUNCTION_BY_ID("/function/:k1"),
    RESOURCE_FUNCTION_REQUEST("/"),
    RESOURCE_FUNCTION_INTERFACE("/interface/"),
    RESOURCE_FUNCTION_SPOOL("/spool/"),
    RESOURCE_FUNCTION_SPOOL_ITEM("/spool/:k1"),
    
    /*OMCP NATIVE ADDRESSES*/
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
