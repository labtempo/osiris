/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.model;

import br.uff.labtempo.osiris.collector.to.CollectorCoTo;
import br.uff.labtempo.osiris.sensornet.model.jpa.Collector;
import br.uff.labtempo.osiris.sensornet.model.jpa.Network;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Felipe
 */
public class CollectorWrapperTest {

    private CollectorCoTo collectorA, collectorB;

    public CollectorWrapperTest() {
        Map<String, String> info = new HashMap<>();
        info.put("chave", "valor");
        this.collectorA = new CollectorCoTo("10", info);
        this.collectorB = new CollectorCoTo("50", info);

    }

    @Test
    public void stateCorrectTransitionTest() {

        Calendar calendar;

        Collector cw = Collector.build(collectorA);
        Collector cw2 = Collector.build(collectorB);
        Collector cw3 = Collector.build(collectorA);

        calendar = cw.getLastModifiedDate();

        cw.update();

        assertNotSame(calendar, cw.getLastModifiedDate());

        calendar = cw.getLastModifiedDate();

        cw.deactivate();

        assertNotSame(calendar, cw.getLastModifiedDate());

        calendar = cw.getLastModifiedDate();

        cw.reactivate();

        assertNotSame(calendar, cw.getLastModifiedDate());

        calendar = cw.getLastModifiedDate();

        cw.update();

        assertNotSame(calendar, cw.getLastModifiedDate());

        calendar = cw.getLastModifiedDate();

        cw.deactivate();

        assertNotSame(calendar, cw.getLastModifiedDate());

        calendar = cw.getLastModifiedDate();

        cw.reactivate();

        assertNotSame(calendar, cw.getLastModifiedDate());

        calendar = cw.getLastModifiedDate();

        cw.deactivate();

        assertNotSame(calendar, cw.getLastModifiedDate());

    }

    @Test(expected = RuntimeException.class)
    public void stateWrongTransitionNewToReactivatedTest() {

        Collector cw = Collector.build(collectorA);
        
        cw.reactivate();

    }

    @Test(expected = RuntimeException.class)
    public void stateWrongTransitionReactivatedToReactivatedTest() {

        Collector cw = Collector.build(collectorA);
        cw.deactivate();
        cw.reactivate();
        cw.reactivate();

    }

    @Test(expected = RuntimeException.class)
    public void stateWrongTransitionDeactivateToUpdatedTest() {

        Collector cw = Collector.build(collectorA);
        Collector cw2 = Collector.build(collectorA);

        cw.deactivate();
        cw.update();

    }

    @Test(expected = RuntimeException.class)
    public void stateWrongTransitionDeactivateToDeactivateTest() {

        Collector cw = Collector.build(collectorA);
        
        cw.deactivate();
        cw.deactivate();

    }

}
