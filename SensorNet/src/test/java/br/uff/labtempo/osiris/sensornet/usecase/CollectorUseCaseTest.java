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
import br.uff.labtempo.osiris.sensornet.usecase.config.Bootstrap;
import br.uff.labtempo.osiris.sensornet.usecase.config.CollectorDataBuilder;
import br.uff.labtempo.osiris.to.collector.CollectorCoTo;
import br.uff.labtempo.osiris.to.collector.NetworkCoTo;
import br.uff.labtempo.osiris.to.collector.SampleCoTo;
import br.uff.labtempo.osiris.to.collector.SensorCoTo;
import br.uff.labtempo.osiris.to.sensornet.CollectorSnTo;
import br.uff.labtempo.osiris.to.sensornet.NetworkSnTo;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static junit.framework.Assert.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class CollectorUseCaseTest {

    private static Bootstrap bootstrap;
    private static CollectorController controller;
    private static NotifyController notifycontroller;

    @Before
    public void getController() throws Exception {
        controller = bootstrap.getCollectorController();
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
    public void testCreateCollector_Valid_ShouldPass() throws NotFoundException, InternalServerErrorException {
        CollectorDataBuilder builder = new CollectorDataBuilder("network", "collectorCreate");
        SampleCoTo sampleCoTo = builder.generateSample();
        notifycontroller.analyzeSample(sampleCoTo);
        CollectorSnTo cst = controller.getById("network", "collectorCreate");
        assertNotNull(cst);
        String expectedId = "collectorCreate";
        assertEquals(expectedId, cst.getId());

        int expectedSensorCount = 1;
        assertEquals(expectedSensorCount, cst.getTotalSensors());

    }

    //geters
    @Test
    public void testGetAllCollector_Valid_ShouldPass() throws NotFoundException, InternalServerErrorException {
        List<CollectorSnTo> csts = controller.getAll("network");

        CollectorDataBuilder builder = new CollectorDataBuilder("network", "collectorGetAll");
        SampleCoTo sampleCoTo = builder.generateSample();
        notifycontroller.analyzeSample(sampleCoTo);

        long expectedSize = csts.size() + 1;

        List<CollectorSnTo> csts1 = controller.getAll("network");

        assertEquals(expectedSize, csts1.size());
    }

    @Test
    public void testGetCollectorById_Valid_ShouldPass() throws NotFoundException, InternalServerErrorException {
        CollectorDataBuilder builder = new CollectorDataBuilder("network", "collectorGet1");
        SampleCoTo sampleCoTo = builder.generateSample();
        notifycontroller.analyzeSample(sampleCoTo);
        builder = new CollectorDataBuilder("network", "collectorGet2");
        sampleCoTo = builder.generateSample();
        notifycontroller.analyzeSample(sampleCoTo);
        builder = new CollectorDataBuilder("network", "collectorGet3");
        sampleCoTo = builder.generateSample();
        notifycontroller.analyzeSample(sampleCoTo);
        
        CollectorSnTo cst = controller.getById("network", "collectorGet1");
        assertNotNull(cst);
        String expectedId = "collectorGet1";
        assertEquals(expectedId, cst.getId());

        cst = controller.getById("network", "collectorGet2");
        assertNotNull(cst);
        expectedId = "collectorGet2";
        assertEquals(expectedId, cst.getId());

        cst = controller.getById("network", "collectorGet3");
        assertNotNull(cst);
        expectedId = "collectorGet3";
        assertEquals(expectedId, cst.getId());

    }

    //updaters
    @Test
    public void testUpdateCollector_Info_Valid_ShouldPass() throws NotFoundException, InternalServerErrorException {
        CollectorDataBuilder builder = new CollectorDataBuilder("networkUpdate", "collectorUpdate");
        SampleCoTo sampleCoTo = builder.generateSample();

        NetworkCoTo nct = sampleCoTo.getNetwork();
        SensorCoTo sct = sampleCoTo.getSensor();

        CollectorCoTo collectorCoTo = new CollectorCoTo("collectorUpdate", 2, TimeUnit.MINUTES);
        collectorCoTo.addInfo("descricao", "sala do laboratorio da pos");
        collectorCoTo.addInfo("numero", "2");
        collectorCoTo.addInfo("Topologia", "barra");

        SampleCoTo sampleCoTo1 = new SampleCoTo(nct, collectorCoTo, sct);

        notifycontroller.analyzeSample(sampleCoTo);
        notifycontroller.analyzeSample(sampleCoTo1);

        CollectorSnTo cst = controller.getById("networkUpdate", "collectorUpdate");
        assertNotNull(cst);
        String expectedDescription = "sala do laboratorio da pos";
        String expectedTopology = "barra";
        assertEquals(expectedDescription, cst.getInfo().get("descricao"));
        assertEquals(expectedTopology, cst.getInfo().get("Topologia"));

    }

    @Test
    public void testUpdateCollector_UpdatingInterval_Valid_ShouldPass() throws NotFoundException, InternalServerErrorException {
        CollectorDataBuilder builder = new CollectorDataBuilder("networkUpdatingInterval", "collectorUpdatingInterval");
        SampleCoTo sampleCoTo = builder.generateSample();

        NetworkCoTo nct = sampleCoTo.getNetwork();
        SensorCoTo sct = sampleCoTo.getSensor();

        CollectorCoTo collectorCoTo = new CollectorCoTo("collectorUpdatingInterval", 50, TimeUnit.SECONDS);
        collectorCoTo.addInfo("descricao", "sala do laboratorio da pos");
        collectorCoTo.addInfo("numero", "2");
        collectorCoTo.addInfo("Topologia", "barra");

        SampleCoTo sampleCoTo1 = new SampleCoTo(nct, collectorCoTo, sct);

        notifycontroller.analyzeSample(sampleCoTo);
        notifycontroller.analyzeSample(sampleCoTo1);

        CollectorSnTo cst = controller.getById("networkUpdatingInterval", "collectorUpdatingInterval");
        assertNotNull(cst);
        long expectedInterval = 50;
        TimeUnit expectedIntervalUnit = TimeUnit.SECONDS;
        assertEquals(expectedInterval, cst.getCaptureInterval());
        assertEquals(expectedIntervalUnit, cst.getCaptureIntervalTimeUnit());
    }

    //remove
    @Test(expected = NotFoundException.class)
    public void testDeleteCollector_Valid_ShouldThrowException() throws NotFoundException, InternalServerErrorException {
        CollectorDataBuilder builder = new CollectorDataBuilder("networkDelete", "collectorDelete");
        SampleCoTo sampleCoTo = builder.generateSample();
        notifycontroller.analyzeSample(sampleCoTo);

        CollectorSnTo cst = controller.getById("networkDelete", "collectorDelete");

        assertNotNull(cst);

        String collectorId = cst.getId();
        String networkId = cst.getNetworkId();

        boolean isDeleted = controller.delete(networkId, collectorId);

        assertTrue(isDeleted);

        controller.getById("networkDelete", "collectorDelete");
    }

    @Test
    public void testDeleteCollector_AndCheckSensorToRemovedToo_ShouldPass() throws NotFoundException, InternalServerErrorException {
        CollectorDataBuilder builder = new CollectorDataBuilder("networkDelete01", "collectorDelete01");
        SampleCoTo sampleCoTo = builder.generateSample();
        notifycontroller.analyzeSample(sampleCoTo);
        CollectorSnTo cst = controller.getById("networkDelete01", "collectorDelete01");

        assertNotNull(cst);
        assertTrue(cst.getTotalSensors() > 0);

        NetworkController networkController = bootstrap.getNetworkController();
        NetworkSnTo networkSnTo = networkController.getById(cst.getNetworkId());

        long networkTotalSensors = networkSnTo.getTotalSensors();
        long collectorTotalSensors = cst.getTotalSensors();

        String collectorId = cst.getId();
        String networkId = cst.getNetworkId();

        boolean isDeleted = controller.delete(networkId, collectorId);

        assertTrue(isDeleted);

        networkSnTo = networkController.getById(networkId);

        long expectedNetworkTotalSensors = networkTotalSensors - collectorTotalSensors;

        assertEquals(expectedNetworkTotalSensors, networkSnTo.getTotalSensors());
    }
    
   
}
