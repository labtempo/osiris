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
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class NetworkUseCaseTest {

    private static Bootstrap bootstrap;
    private static NetworkController controller;
    private static NotifyController notifycontroller;

    @Before
    public void getController() throws Exception {
        controller = bootstrap.getNetworkController();
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
    public void testCreateNetwork_Valid_ShouldPass() throws NotFoundException, InternalServerErrorException {
        CollectorDataBuilder builder = new CollectorDataBuilder("networkCreate", "collectorCreate");
        SampleCoTo sampleCoTo = builder.generateSample();
        notifycontroller.analyzeSample(sampleCoTo);

        NetworkSnTo nst = controller.getById("networkCreate");

        assertNotNull(nst);

        String expectedId = "networkCreate";
        assertEquals(expectedId, nst.getId());

        int expectedSensorCount = 1;
        assertEquals(expectedSensorCount, nst.getTotalSensors());
    }

    //geters
    @Test
    public void testGetAllNetwork_Valid_ShouldPass() throws NotFoundException, InternalServerErrorException {
        List<NetworkSnTo> nsts = controller.getAll();

        CollectorDataBuilder builder = new CollectorDataBuilder("networkGetAll", "collectorGetAll");
        SampleCoTo sampleCoTo = builder.generateSample();
        notifycontroller.analyzeSample(sampleCoTo);

        long expectedSize = nsts.size() + 1;

        List<NetworkSnTo> csts1 = controller.getAll();

        assertEquals(expectedSize, csts1.size());
    }

    @Test
    public void testGetNetworkById_Valid_ShouldPass() throws NotFoundException, InternalServerErrorException {
        CollectorDataBuilder builder = new CollectorDataBuilder("networkGetNetworkById1", "collectorGet1");
        SampleCoTo sampleCoTo = builder.generateSample();
        notifycontroller.analyzeSample(sampleCoTo);
        builder = new CollectorDataBuilder("networkGetNetworkById2", "collectorGet2");
        sampleCoTo = builder.generateSample();
        notifycontroller.analyzeSample(sampleCoTo);
        builder = new CollectorDataBuilder("networkGetNetworkById3", "collectorGet3");
        sampleCoTo = builder.generateSample();
        notifycontroller.analyzeSample(sampleCoTo);

        NetworkSnTo cst = controller.getById("networkGetNetworkById1");
        assertNotNull(cst);
        String expectedId = "networkGetNetworkById1";
        assertEquals(expectedId, cst.getId());

        cst = controller.getById("networkGetNetworkById2");
        assertNotNull(cst);
        expectedId = "networkGetNetworkById2";
        assertEquals(expectedId, cst.getId());

        cst = controller.getById("networkGetNetworkById3");
        assertNotNull(cst);
        expectedId = "networkGetNetworkById3";
        assertEquals(expectedId, cst.getId());
    }

    //updaters 
    @Test
    public void testUpdateNetwork_info_Valid_ShouldPass() throws NotFoundException, InternalServerErrorException {
        CollectorDataBuilder builder = new CollectorDataBuilder("networkUpdateNetwork", "collectorUpdate");
        SampleCoTo sampleCoTo = builder.generateSample();

        CollectorCoTo cct = sampleCoTo.getCollector();
        SensorCoTo sct = sampleCoTo.getSensor();
        
        NetworkCoTo networkCoTo = new NetworkCoTo("networkUpdateNetwork");
        networkCoTo.addInfo("domain", "br.uff.ic.labtempo");
        networkCoTo.addInfo("type", "wireless");
        networkCoTo.addInfo("OS", "TinyOS");
        networkCoTo.addInfo("Topologia", "estrela");
        
        SampleCoTo sampleCoTo1 = new SampleCoTo(networkCoTo,cct, sct);        
        
        notifycontroller.analyzeSample(sampleCoTo);
        notifycontroller.analyzeSample(sampleCoTo1);
        
        NetworkSnTo nst = controller.getById("networkUpdateNetwork");
        assertNotNull(nst);
        String expectedDomain = "br.uff.ic.labtempo";
        String expectedTopology = "estrela";
        assertEquals(expectedDomain, nst.getInfo().get("domain"));
        assertEquals(expectedTopology, nst.getInfo().get("Topologia"));
    }

    //remove
    @Test(expected = NotFoundException.class)
    public void testDeleteNetwork_Valid_ShouldPass() throws NotFoundException, InternalServerErrorException {
        CollectorDataBuilder builder = new CollectorDataBuilder("networkDeleteNetwork", "collectorDelete");
        SampleCoTo sampleCoTo = builder.generateSample();
        notifycontroller.analyzeSample(sampleCoTo);

        NetworkSnTo nst = controller.getById("networkDeleteNetwork");

        assertNotNull(nst);

        String name = nst.getId();
        
        boolean isDeleted = controller.delete(name);
        
        assertTrue(isDeleted);
        
        controller.getById("DeleteNetwork");
    }
    
    @Test
    public void testDeleteNetwork_CheckAll_ShouldPass() throws NotFoundException, InternalServerErrorException {
        List<NetworkSnTo> networkSnTos = controller.getAll();
        long totalNetworksOriginal = networkSnTos.size();
        
        CollectorDataBuilder builder = new CollectorDataBuilder("networkCheckAll", "collectorDelete");
        SampleCoTo sampleCoTo = builder.generateSample();
        notifycontroller.analyzeSample(sampleCoTo);        
         
        networkSnTos = controller.getAll();
        long totalNetworksInserted = networkSnTos.size(); 

        NetworkSnTo cst = controller.getById("networkCheckAll");

        assertNotNull(cst);
        
        boolean isDeleted = controller.delete("networkCheckAll");
        
        assertTrue(isDeleted);
        
        networkSnTos = controller.getAll();
        long expectedNetworkTotalSensors = totalNetworksInserted - 1;
        
        assertEquals(totalNetworksOriginal,networkSnTos.size());
        
        assertEquals(expectedNetworkTotalSensors, networkSnTos.size());
    }
}
