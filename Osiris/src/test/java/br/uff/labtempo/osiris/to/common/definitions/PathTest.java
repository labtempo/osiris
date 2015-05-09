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

import static br.uff.labtempo.osiris.to.common.definitions.Path.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class PathTest {

    public PathTest() {
    }

    @Test
    public void testModuleSensorNet_Equal_ShouldPass() {
        String expected = PROTOCOL.toString() + NAMING_MODULE_SENSORNET + SEPARATOR;
        assertEquals(expected, MODULE_SENSORNET.toString());
    }

    @Test
    public void testModuleVirtualSensorNet_Equal_ShouldPass() {
        String expected = PROTOCOL.toString() + NAMING_MODULE_VIRTUALSENSORNET + SEPARATOR;
        assertEquals(expected, MODULE_VIRTUALSENSORNET.toString());
    }

    @Test
    public void testMessageGroupCollector_Equal_ShouldPass() {
        String expected = PROTOCOL.toString() + NAMING_MESSAGEGROUP_COLLECTOR + SEPARATOR;
        assertEquals(expected, MESSAGEGROUP_COLLECTOR.toString());
    }

    @Test
    public void testMessageGroupUpdate_Equal_ShouldPass() {
        String expected = PROTOCOL.toString() + NAMING_MESSAGEGROUP_UPDATE + SEPARATOR;
        assertEquals(expected, MESSAGEGROUP_UPDATE.toString());
    }

    @Test
    public void testMessageGroupNotification_Equal_ShouldPass() {
        String expected = PROTOCOL.toString() + NAMING_MESSAGEGROUP_NOTIFICATION + SEPARATOR;
        assertEquals(expected, MESSAGEGROUP_NOTIFICATION.toString());
    }

    @Test
    public void testResourceSensorNetNetwortAll_Equal_ShouldPass() {
        String expected = SEPARATOR.toString() + NAMING_RESOURCE_NETWORK;
        assertEquals(expected, RESOURCE_SENSORNET_NETWORK_All.toString());
    }

    @Test
    public void testResourceSensorNetNetwortById_Equal_ShouldPass() {
        String expected = SEPARATOR.toString() + NAMING_RESOURCE_NETWORK + ID1;
        assertEquals(expected, RESOURCE_SENSORNET_NETWORK_BY_ID.toString());
    }

    @Test
    public void testResourceSensorNetNetwortSensorAll_Equal_ShouldPass() {
        String expected = SEPARATOR.toString() + NAMING_RESOURCE_NETWORK + ID1 + SEPARATOR + NAMING_RESOURCE_SENSOR + SEPARATOR;
        assertEquals(expected, RESOURCE_SENSORNET_NETWORK_SENSOR_All.toString());
    }

    @Test
    public void testResourceSensorNetNetwortSensorById_Equal_ShouldPass() {
        String expected = SEPARATOR.toString() + NAMING_RESOURCE_NETWORK + ID1 + SEPARATOR + NAMING_RESOURCE_SENSOR + SEPARATOR + ID2;
        assertEquals(expected, RESOURCE_SENSORNET_NETWORK_SENSOR_BY_ID.toString());
    }

    @Test
    public void testResourceSensorNetCollectorAll_Equal_ShouldPass() {
        String expected = SEPARATOR.toString() + NAMING_RESOURCE_NETWORK + ID1 + SEPARATOR + NAMING_RESOURCE_COLLECTOR + SEPARATOR;
        assertEquals(expected, RESOURCE_SENSORNET_COLLECTOR_All.toString());
    }

    @Test
    public void testResourceSensorNetCollectorById_Equal_ShouldPass() {
        String expected = SEPARATOR.toString() + NAMING_RESOURCE_NETWORK + ID1 + SEPARATOR + NAMING_RESOURCE_COLLECTOR + SEPARATOR + ID2;
        assertEquals(expected, RESOURCE_SENSORNET_COLLECTOR_BY_ID.toString());
    }

    @Test
    public void testResourceSensorNetSensorAll_Equal_ShouldPass() {
        String expected = SEPARATOR.toString() + NAMING_RESOURCE_NETWORK + ID1 + SEPARATOR + NAMING_RESOURCE_COLLECTOR + SEPARATOR + ID2 + SEPARATOR + NAMING_RESOURCE_SENSOR + SEPARATOR;
        assertEquals(expected, RESOURCE_SENSORNET_SENSOR_All.toString());
    }

    @Test
    public void testResourceSensorNetSensorById_Equal_ShouldPass() {
        String expected = SEPARATOR.toString() + NAMING_RESOURCE_NETWORK + ID1 + SEPARATOR + NAMING_RESOURCE_COLLECTOR + SEPARATOR + ID2 + SEPARATOR + NAMING_RESOURCE_SENSOR + SEPARATOR + ID3;
        assertEquals(expected, RESOURCE_SENSORNET_SENSOR_BY_ID.toString());
    }

    @Test
    public void testResourceVirtualSensorNetVirtualSensorAll_Equal_ShouldPass() {
        String expected = SEPARATOR.toString() + NAMING_RESOURCE_VIRTUALSENSOR + SEPARATOR;
        assertEquals(expected, RESOURCE_VIRTUALSENSORNET_VIRTUALSENSOR_ALL.toString());
    }

    @Test
    public void testResourceVirtualSensorNetVirtualSensorById_Equal_ShouldPass() {
        String expected = SEPARATOR.toString() + NAMING_RESOURCE_VIRTUALSENSOR + SEPARATOR + ID1;
        assertEquals(expected, RESOURCE_VIRTUALSENSORNET_VIRTUALSENSOR_BY_ID.toString());
    }

    @Test
    public void testResourceVirtualSensorNetVirtualSensorByIdHistoryAll_Equal_ShouldPass() {
        String expected = SEPARATOR.toString() + NAMING_RESOURCE_VIRTUALSENSOR + SEPARATOR + ID1 + SEPARATOR + NAMING_RESOURCE_REVISIONS + SEPARATOR;
        assertEquals(expected, RESOURCE_VIRTUALSENSORNET_VIRTUALSENSOR_REVISIONS_BY_ID.toString());
    }

    @Test
    public void testResourceVirtualSensorNetLinkALl_Equal_ShouldPass() {
        String expected = SEPARATOR.toString() + NAMING_RESOURCE_LINK + SEPARATOR;
        assertEquals(expected, RESOURCE_VIRTUALSENSORNET_LINK_ALL.toString());
    }

    @Test
    public void testResourceVirtualSensorNetLinkById_Equal_ShouldPass() {
        String expected = SEPARATOR.toString() + NAMING_RESOURCE_LINK + SEPARATOR + ID1;
        assertEquals(expected, RESOURCE_VIRTUALSENSORNET_LINK_BY_ID.toString());
    }

    @Test
    public void testResourceVirtualSensorNetCompositeAll_Equal_ShouldPass() {
        String expected = SEPARATOR.toString() + NAMING_RESOURCE_COMPOSITE + SEPARATOR;
        assertEquals(expected, RESOURCE_VIRTUALSENSORNET_COMPOSITE_ALL.toString());
    }

    @Test
    public void testResourceVirtualSensorNetCompositeById_Equal_ShouldPass() {
        String expected = SEPARATOR.toString() + NAMING_RESOURCE_COMPOSITE + SEPARATOR + ID1;
        assertEquals(expected, RESOURCE_VIRTUALSENSORNET_COMPOSITE_BY_ID.toString());
    }

    @Test
    public void testResourceVirtualSensorNetBlendingAll_Equal_ShouldPass() {
        String expected = SEPARATOR.toString() + NAMING_RESOURCE_BLENDING + SEPARATOR;
        assertEquals(expected, RESOURCE_VIRTUALSENSORNET_BLENDING_ALL.toString());
    }

    @Test
    public void testResourceVirtualSensorNetBlendingById_Equal_ShouldPass() {
        String expected = SEPARATOR.toString() + NAMING_RESOURCE_BLENDING + SEPARATOR + ID1;
        assertEquals(expected, RESOURCE_VIRTUALSENSORNET_BLENDING_BY_ID.toString());
    }

    @Test
    public void testResourceVirtualSensorNetConverterAll_Equal_ShouldPass() {
        String expected = SEPARATOR.toString() + NAMING_RESOURCE_CONVERTER + SEPARATOR;
        assertEquals(expected, RESOURCE_VIRTUALSENSORNET_CONVERTER_ALL.toString());
    }

    @Test
    public void testResourceVirtualSensorNetConverterBlendingById_Equal_ShouldPass() {
        String expected = SEPARATOR.toString() + NAMING_RESOURCE_CONVERTER + SEPARATOR + ID1;
        assertEquals(expected, RESOURCE_VIRTUALSENSORNET_CONVERTER_BY_ID.toString());
    }

    @Test
    public void testResourceVirtualSensorNetDataTypeAll_Equal_ShouldPass() {
        String expected = SEPARATOR.toString() + NAMING_RESOURCE_DATATYPE + SEPARATOR;
        assertEquals(expected, RESOURCE_VIRTUALSENSORNET_DATATYPE_ALL.toString());
    }

    @Test
    public void testResourceVirtualSensorNetDatatypeById_Equal_ShouldPass() {
        String expected = SEPARATOR.toString() + NAMING_RESOURCE_DATATYPE + SEPARATOR + ID1;
        assertEquals(expected, RESOURCE_VIRTUALSENSORNET_DATATYPE_BY_ID.toString());
    }
    
    @Test
    public void testNamingMessageGroupCollector_Contains_ShouldPass() {
        assertTrue(NAMING_MESSAGEGROUP_COLLECTOR.toString().contains(NAMING_EXTRAMODULE_MESSAGEGROUP.toString()));
    }
    @Test
    public void testNamingMessageGroupNotification_Contains_ShouldPass() {
        assertTrue(NAMING_MESSAGEGROUP_NOTIFICATION.toString().contains(NAMING_EXTRAMODULE_MESSAGEGROUP.toString()));
    }
    @Test
    public void testNamingMessageGroupUpdate_Contains_ShouldPass() {
        assertTrue(NAMING_MESSAGEGROUP_UPDATE.toString().contains(NAMING_EXTRAMODULE_MESSAGEGROUP.toString()));
    }

}
