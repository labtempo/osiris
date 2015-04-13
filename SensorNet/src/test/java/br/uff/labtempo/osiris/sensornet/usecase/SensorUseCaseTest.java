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
package br.uff.labtempo.osiris.sensornet.usecase;

import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.omcp.common.exceptions.NotFoundException;
import br.uff.labtempo.osiris.sensornet.controller.CollectorController;
import br.uff.labtempo.osiris.sensornet.controller.NetworkController;
import br.uff.labtempo.osiris.sensornet.controller.NotifyController;
import br.uff.labtempo.osiris.sensornet.controller.SensorController;
import br.uff.labtempo.osiris.sensornet.model.Sensor;
import br.uff.labtempo.osiris.sensornet.usecase.config.Bootstrap;
import br.uff.labtempo.osiris.sensornet.usecase.config.CollectorDataBuilder;
import br.uff.labtempo.osiris.to.collector.CollectorCoTo;
import br.uff.labtempo.osiris.to.collector.NetworkCoTo;
import br.uff.labtempo.osiris.to.collector.SampleCoTo;
import br.uff.labtempo.osiris.to.collector.SensorCoTo;
import br.uff.labtempo.osiris.to.common.data.ConsumableTo;
import br.uff.labtempo.osiris.to.common.data.InfoTo;
import br.uff.labtempo.osiris.to.common.data.ValueTo;
import br.uff.labtempo.osiris.to.common.definitions.LogicalOperator;
import br.uff.labtempo.osiris.to.sensornet.CollectorSnTo;
import br.uff.labtempo.osiris.to.sensornet.NetworkSnTo;
import br.uff.labtempo.osiris.to.sensornet.SensorSnTo;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class SensorUseCaseTest {

    private static Bootstrap bootstrap;
    private static SensorController controller;
    private static NotifyController notifycontroller;

    @Before
    public void getController() throws Exception {
        controller = bootstrap.getSensorController();
        notifycontroller = bootstrap.getNotifyController();
    }

    @BeforeClass
    public static void upserver() throws Exception {
        bootstrap = new Bootstrap();
    }

    @AfterClass
    public static void downserver() throws Exception {
        bootstrap.close();
    }

    @Test
    public void testCreateSensor_Valid_ShouldPass() throws NotFoundException, InternalServerErrorException {
        CollectorDataBuilder builder = new CollectorDataBuilder("network", "collector", "sensorCreate");
        SampleCoTo sampleCoTo = builder.generateSample();
        notifycontroller.analyzeSample(sampleCoTo);

        SensorSnTo cst = controller.getById("network", "collector", "sensorCreate");

        assertNotNull(cst);

        String expectedId = "sensorCreate";
        assertEquals(expectedId, cst.getId());
    }

    //geters
    @Test
    public void testGetAllSensor_Valid_ShouldPass() throws NotFoundException, InternalServerErrorException {
        List<SensorSnTo> ssts = controller.getAll("network", "collector");

        CollectorDataBuilder builder = new CollectorDataBuilder("network", "collector", "sensorGetAll");
        SampleCoTo sampleCoTo = builder.generateSample();
        notifycontroller.analyzeSample(sampleCoTo);
        List<SensorSnTo> csts1 = controller.getAll("network", "collector");
        long expectedSize = ssts.size() + 1;

        assertEquals(expectedSize, csts1.size());
    }

    @Test
    public void testGetAllSensorFromNetwork_Valid_ShouldPass() throws NotFoundException, InternalServerErrorException {
        List<SensorSnTo> ssts = controller.getAllInNetwork("network");

        CollectorDataBuilder builder = new CollectorDataBuilder("network", "collector", "sensorGetAllSensorFromNetwork");
        SampleCoTo sampleCoTo = builder.generateSample();
        notifycontroller.analyzeSample(sampleCoTo);

        long expectedSize = ssts.size() + 1;

        List<SensorSnTo> csts1 = controller.getAllInNetwork("network");

        assertEquals(expectedSize, csts1.size());
    }

    @Test
    public void testGetSensorById_Valid_ShouldPass() throws NotFoundException, InternalServerErrorException {
        CollectorDataBuilder builder = new CollectorDataBuilder("network", "collector", "sensorGetSensorById1");
        SampleCoTo sampleCoTo = builder.generateSample();
        notifycontroller.analyzeSample(sampleCoTo);
        builder = new CollectorDataBuilder("network", "collector", "sensorGetSensorById2");
        sampleCoTo = builder.generateSample();
        notifycontroller.analyzeSample(sampleCoTo);
        builder = new CollectorDataBuilder("network", "collector", "sensorGetSensorById3");
        sampleCoTo = builder.generateSample();
        notifycontroller.analyzeSample(sampleCoTo);

        SensorSnTo sst = controller.getById("network", "collector", "sensorGetSensorById1");
        assertNotNull(sst);
        String expectedId = "sensorGetSensorById1";
        assertEquals(expectedId, sst.getId());

        sst = controller.getById("network", "collector", "sensorGetSensorById2");
        assertNotNull(sst);
        expectedId = "sensorGetSensorById2";
        assertEquals(expectedId, sst.getId());

        sst = controller.getById("network", "collector", "sensorGetSensorById3");
        assertNotNull(sst);
        expectedId = "sensorGetSensorById3";
        assertEquals(expectedId, sst.getId());
    }

    //updaters
    @Test
    public void testUpdateSensor_Values_Valid_ShouldPass() throws NotFoundException, InternalServerErrorException, InterruptedException {
        CollectorDataBuilder builder = new CollectorDataBuilder("network", "collector", "sensorUpdateSensor_Values");
        SampleCoTo sampleCoTo = builder.generateSample();

        Thread.sleep(1);

        NetworkCoTo nct = sampleCoTo.getNetwork();
        CollectorCoTo cct = sampleCoTo.getCollector();
        SensorCoTo sensorTo = new SensorCoTo("sensorUpdateSensor_Values");

        sensorTo.addInfo("chave", "valor");
        sensorTo.addInfo("chave2", "valor2");

        sensorTo.addValue("temperature", 50000, "celsius", "°C");
        sensorTo.addValue("luminosity", 50000, "candela", "cd");
        sensorTo.addValue("battery", 50000, "volt", "V");

        sensorTo.addConsumable("battery", 25);
        sensorTo.addConsumableRule("low battery", "battery", LogicalOperator.GREATER_THAN, 30, "Battery has low charge, you need change it!");

        sensorTo.addConsumable("fuel", 50);
        sensorTo.addConsumableRule("low fuel", "fuel", LogicalOperator.GREATER_THAN, 70, "Low fuel message!");

        SampleCoTo sampleCoTo1 = new SampleCoTo(nct, cct, sensorTo);

        notifycontroller.analyzeSample(sampleCoTo);
        notifycontroller.analyzeSample(sampleCoTo1);

        SensorSnTo cst = controller.getById("network", "collector", "sensorUpdateSensor_Values");
        assertNotNull(cst);
        String expectedValue = "50000";

        List<? extends ValueTo> vts = cst.getValuesTo();

        for (ValueTo vt : vts) {
            assertEquals(expectedValue, vt.getValue());
        }
    }

    @Test
    public void testUpdateSensor_Consumables_Valid_ShouldPass() throws NotFoundException, InternalServerErrorException, InterruptedException {
        CollectorDataBuilder builder = new CollectorDataBuilder("network", "collector", "sensorUpdateSensor_Consumables");
        SampleCoTo sampleCoTo = builder.generateSample();

        Thread.sleep(1);

        NetworkCoTo nct = sampleCoTo.getNetwork();
        CollectorCoTo cct = sampleCoTo.getCollector();
        SensorCoTo sensorTo = new SensorCoTo("sensorUpdateSensor_Consumables");

        sensorTo.addInfo("chave", "valor");
        sensorTo.addInfo("chave2", "valor2");

        sensorTo.addValue("temperature", 50000, "celsius", "°C");
        sensorTo.addValue("luminosity", 50000, "candela", "cd");
        sensorTo.addValue("battery", 50000, "volt", "V");

        sensorTo.addConsumable("battery", 100);
        sensorTo.addConsumableRule("low battery", "battery", LogicalOperator.GREATER_THAN, 30, "Battery has low charge, you need change it!");

        sensorTo.addConsumable("fuel", 100);
        sensorTo.addConsumableRule("low fuel", "fuel", LogicalOperator.GREATER_THAN, 70, "Low fuel message!");

        SampleCoTo sampleCoTo1 = new SampleCoTo(nct, cct, sensorTo);

        notifycontroller.analyzeSample(sampleCoTo);
        notifycontroller.analyzeSample(sampleCoTo1);

        SensorSnTo cst = controller.getById("network", "collector", "sensorUpdateSensor_Consumables");
        assertNotNull(cst);
        short expectedValue = 100;

        List<? extends ConsumableTo> vts = cst.getConsumablesTo();

        for (ConsumableTo vt : vts) {
            assertEquals(expectedValue, vt.getValue());
        }
    }

//    @Test
    public void testUpdateSensor_ConsumablesRules_Valid_ShouldPass() throws NotFoundException, InternalServerErrorException, InterruptedException {
//        CollectorDataBuilder builder = new CollectorDataBuilder("network", "collector", "sensorUpdateSensor_ConsumablesRules");
//        SampleCoTo sampleCoTo = builder.generateSample();
//
//        Thread.sleep(1);
//
//        NetworkCoTo nct = sampleCoTo.getNetwork();
//        CollectorCoTo cct = sampleCoTo.getCollector();
//        SensorCoTo sensorTo = new SensorCoTo("sensorUpdateSensor_ConsumablesRules");
//
//        sensorTo.addInfo("chave", "valor");
//        sensorTo.addInfo("chave2", "valor2");
//
//        sensorTo.addValue("temperature", 50000, "celsius", "°C");
//        sensorTo.addValue("luminosity", 50000, "candela", "cd");
//        sensorTo.addValue("battery", 50000, "volt", "V");
//
//        sensorTo.addConsumable("battery", 100);
//        sensorTo.addConsumableRule("low battery", "battery", LogicalOperator.EQUAL, 1, "Battery has low charge, you need change it!");
//
//        sensorTo.addConsumable("fuel", 100);
//        sensorTo.addConsumableRule("low fuel", "fuel", LogicalOperator.EQUAL, 1, "Low fuel message!");
//
//        SampleCoTo sampleCoTo1 = new SampleCoTo(nct, cct, sensorTo);
//
//        notifycontroller.analyzeSample(sampleCoTo);
//        notifycontroller.analyzeSample(sampleCoTo1);
//
//        SensorSnTo cst = controller.getById("network", "collector", "sensorUpdateSensor_ConsumablesRules");
//        assertNotNull(cst);
//        short expectedValue = 100;
//
//        List<? extends RuleTo> vts = cst.getR();
//
//        for (ConsumableTo vt : vts) {
//            assertEquals(expectedValue, vt.getValue());
//        }
    }

    @Test
    public void testUpdateSensor_Info_Valid_ShouldPass() throws InterruptedException, NotFoundException, InternalServerErrorException {
        CollectorDataBuilder builder = new CollectorDataBuilder("network", "collector", "sensorUpdateSensor_Info");
        SampleCoTo sampleCoTo = builder.generateSample();

        Thread.sleep(1);

        NetworkCoTo nct = sampleCoTo.getNetwork();
        CollectorCoTo cct = sampleCoTo.getCollector();
        SensorCoTo sensorTo = new SensorCoTo("sensorUpdateSensor_Info");

        sensorTo.addInfo("chave", "value");
        sensorTo.addInfo("chave2", "value");

        sensorTo.addValue("temperature", 50000, "celsius", "°C");
        sensorTo.addValue("luminosity", 50000, "candela", "cd");
        sensorTo.addValue("battery", 50000, "volt", "V");

        sensorTo.addConsumable("battery", 100);
        sensorTo.addConsumableRule("low battery", "battery", LogicalOperator.EQUAL, 1, "Battery has low charge, you need change it!");

        sensorTo.addConsumable("fuel", 100);
        sensorTo.addConsumableRule("low fuel", "fuel", LogicalOperator.EQUAL, 1, "Low fuel message!");

        SampleCoTo sampleCoTo1 = new SampleCoTo(nct, cct, sensorTo);

        notifycontroller.analyzeSample(sampleCoTo);
        notifycontroller.analyzeSample(sampleCoTo1);

        SensorSnTo cst = controller.getById("network", "collector", "sensorUpdateSensor_Info");
        assertNotNull(cst);
        String expectedValue = "value";

        List<? extends InfoTo> vts = cst.getInfoTo();

        for (InfoTo vt : vts) {
            assertEquals(expectedValue, vt.getDescription());
        }
    }

    //remove
    @Test
    public void testDeleteSensor_Valid_ShouldPass() throws NotFoundException, InternalServerErrorException {
        CollectorDataBuilder builder = new CollectorDataBuilder("networkDeleteSensor", "collectorDeleteSensor", "sensorDeleteSensor");
        SampleCoTo sampleCoTo = builder.generateSample();
        notifycontroller.analyzeSample(sampleCoTo);

        SensorSnTo sst = controller.getById("networkDeleteSensor", "collectorDeleteSensor", "sensorDeleteSensor");
        assertNotNull(sst);

        String networkId = sst.getNetworkId();
        NetworkController networkController = bootstrap.getNetworkController();
        NetworkSnTo nct = networkController.getById(networkId);

        String collectorId = sst.getCollectorId();
        CollectorController collectorController = bootstrap.getCollectorController();
        CollectorSnTo cst = collectorController.getById(networkId, collectorId);

        int expectedNetworkTotalSensors = nct.getTotalSensors() - 1;
        int expectedCollectorTotalSensors = cst.getTotalSensors() - 1;

        boolean isDeleted = controller.delete(networkId, collectorId, "sensorDeleteSensor");

        Assert.assertTrue(isDeleted);
        
        nct = networkController.getById(networkId);
        cst = collectorController.getById(networkId, collectorId);

        assertEquals(expectedNetworkTotalSensors, nct.getTotalSensors());
        assertEquals(expectedCollectorTotalSensors, cst.getTotalSensors());
    }
}
