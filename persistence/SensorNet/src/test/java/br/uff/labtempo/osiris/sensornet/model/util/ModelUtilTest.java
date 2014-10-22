/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.model.util;

import br.uff.labtempo.osiris.sensornet.model.jpa.Collector;
import br.uff.labtempo.osiris.sensornet.model.jpa.Consumable;
import br.uff.labtempo.osiris.sensornet.model.jpa.Network;
import br.uff.labtempo.osiris.sensornet.model.jpa.Sensor;
import br.uff.labtempo.osiris.sensornet.model.state.ModelState;
import br.uff.labtempo.osiris.sensornet.to.CollectorSnTo;
import br.uff.labtempo.osiris.sensornet.to.NetworkSnTo;
import br.uff.labtempo.osiris.sensornet.to.SensorSnTo;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Felipe
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

        assertEquals(co.getState(), ModelState.NEW);
        assertEquals(ne.getState(), ModelState.NEW);
        assertEquals(se.getState(), ModelState.NEW);

        assertEquals(coto.getState(), ModelState.NEW.toString());
        assertEquals(neto.getState(), ModelState.NEW.toString());
        assertEquals(seto.getState(), ModelState.NEW.toString());

        builder = new ToBuilder();

        co.update(builder.getCollector());
        ne.update(builder.getNetwork());
        se.update(builder.getSensor());

        assertEquals(co.getState(), ModelState.UPDATED);
        assertEquals(ne.getState(), ModelState.UPDATED);
        assertEquals(se.getState(), ModelState.UPDATED);
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
        assertEquals(co.getSensors().length, cto.countSensors());
        assertEquals(co.getId(), cto.getId());
        assertEquals(co.getState().toString(), cto.getState());

        assertEquals(ne.getInfo(), nto.getInfo());
        assertEquals(ne.getCollectors().length, nto.countCollectors());
        assertEquals(ne.getSensors().length, nto.countSensors());
        assertEquals(ne.getId(), nto.getId());
        assertEquals(ne.getState().toString(), nto.getState());

        assertEquals(se.getInfo(), sto.getInfo());
        assertEquals(se.getCollector().getId(), sto.getCollectorId());
        assertEquals(se.getNetwork().getId(), sto.getNetworkId());
        assertEquals(se.getId(), sto.getId());
        assertEquals(se.getState().toString(), sto.getState());
        assertEquals(se.getLastModifiedDate().getTimeInMillis(), sto.getLastModified());        
        assertEquals(se.getTimestamp(), sto.getTimestamp());
        
        //consumables
        for (Consumable consumable : se.getConsumables()) {
            assertTrue(sto.getConsumables().containsKey(consumable.getName()));
            assertEquals((Integer)consumable.getValue(), sto.getConsumables().get(consumable.getName()));
        }
        
    }

}
