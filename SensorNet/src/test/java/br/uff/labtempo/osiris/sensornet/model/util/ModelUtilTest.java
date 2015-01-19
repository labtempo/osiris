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
package br.uff.labtempo.osiris.sensornet.model.util;

import br.uff.labtempo.osiris.sensornet.model.jpa.Collector;
import br.uff.labtempo.osiris.sensornet.model.jpa.Consumable;
import br.uff.labtempo.osiris.sensornet.model.jpa.Network;
import br.uff.labtempo.osiris.sensornet.model.jpa.Sensor;
import br.uff.labtempo.osiris.sensornet.model.state.ModelState;
import br.uff.labtempo.osiris.to.sensornet.CollectorSnTo;
import br.uff.labtempo.osiris.to.sensornet.NetworkSnTo;
import br.uff.labtempo.osiris.to.sensornet.SensorSnTo;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class ModelUtilTest {

    ToBuilder builder;
    private ModelUtil util;

    public ModelUtilTest() {
        builder = new ToBuilder();
        util = new ModelUtil();
    }

    @Test
    public void updateFromTransferObject() {
        Collector co = util.fromTransferObject(builder.getCollector());
        Network ne = util.fromTransferObject(builder.getNetwork());
        Sensor se = util.fromTransferObject(builder.getSensor());

        ne.addCollector(co);
        ne.addSensor(se);

        co.setNetwork(ne);
        co.addSensor(se);

        se.setCollector(co);
        se.setNetwork(ne);

        CollectorSnTo coto = util.toTransferObject(co);
        NetworkSnTo neto = util.toTransferObject(ne);
        SensorSnTo seto = util.toTransferObject(se);

        assertEquals(ModelState.NEW, co.getModelState());
        assertEquals(ModelState.NEW, ne.getModelState());
        assertEquals(ModelState.NEW, se.getModelState());

        assertEquals(ModelState.NEW.getState(), coto.getState());
        assertEquals(ModelState.NEW.getState(), neto.getState());
        assertEquals(ModelState.NEW.getState(), seto.getState());

        builder = new ToBuilder();
        
        //this sleep is necessary because the TO object is catching same time of the first object, so it is breaking the test!
        try {
            Thread.sleep(1);
        } catch (InterruptedException ex) {
            Logger.getLogger(ModelUtilTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        co.update(builder.getCollector());
        ne.update(builder.getNetwork());
        se.update(builder.getSensor());

        assertEquals(ModelState.UPDATED, co.getModelState());
        assertEquals(ModelState.UPDATED, ne.getModelState());
        assertEquals(ModelState.UPDATED, se.getModelState());
    }

    @Test
    public void testConvertion() {
        Collector co = util.fromTransferObject(builder.getCollector());
        Network ne = util.fromTransferObject(builder.getNetwork());
        Sensor se = util.fromTransferObject(builder.getSensor());

        ne.addCollector(co);
        co.addSensor(se);

        CollectorSnTo cto = co.getTransferObject();
        NetworkSnTo nto = ne.getTransferObject();
        SensorSnTo sto = se.getTransferObject();

        assertEquals(co.getInfo(), cto.getInfo());
        assertEquals(co.getNetwork().getId(), cto.getNetworkId());
        assertEquals(co.getSensors().length, cto.getTotalSensors());
        assertEquals(co.getId(), cto.getId());
        assertEquals(co.getModelState().getState(), cto.getState());
        assertEquals(co.getLastModifiedDate(), cto.getLastModified());

        assertEquals(ne.getInfo(), nto.getInfo());
        assertEquals(ne.getCollectors().length, nto.getTotalCollectors());
        assertEquals(ne.getSensors().length, nto.getTotalSensors());
        assertEquals(ne.getId(), nto.getId());
        assertEquals(ne.getModelState().getState(), nto.getState());
        assertEquals(ne.getLastModifiedDate(), nto.getLastModified());

        assertEquals(se.getInfo(), sto.getInfo());
        assertEquals(se.getCollector().getId(), sto.getCollectorId());
        assertEquals(se.getNetwork().getId(), sto.getNetworkId());
        assertEquals(se.getId(), sto.getId());
        assertEquals(se.getModelState().getState(), sto.getState());
        assertEquals(se.getLastModifiedDate(), sto.getLastModified());
        assertEquals(se.getTimestamp(), sto.getTimestamp());

        //consumables
        for (Consumable consumable : se.getConsumables()) {
            assertTrue(sto.getConsumables().containsKey(consumable.getName()));
            assertEquals((Integer) consumable.getValue(), sto.getConsumables().get(consumable.getName()));
        }

    }

}
