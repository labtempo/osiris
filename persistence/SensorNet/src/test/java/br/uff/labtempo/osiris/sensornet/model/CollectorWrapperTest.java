/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.model;

import br.uff.labtempo.osiris.collector.temp.Collector;
import br.uff.labtempo.osiris.collector.temp.Info;
import java.util.Calendar;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Felipe
 */
public class CollectorWrapperTest {

    private Collector collectorA, collectorB;

    public CollectorWrapperTest() {
        this.collectorA = new Collector(10);
        this.collectorB = new Collector(50);
        Info info = new Info();
        info.add("chave", "valor");

        this.collectorA.setInfo(info);
        this.collectorB.setInfo(info);
    }

    @Test
    public void stateCorrectTransitionTest() {

        Calendar calendar;

        CollectorWrapper cw = new CollectorWrapper(collectorA);
        CollectorWrapper cw2 = new CollectorWrapper(collectorB);
        CollectorWrapper cw3 = new CollectorWrapper(collectorA);

        calendar = cw.getLastModifiedDate();

        cw.update(cw2);

        assertNotSame(calendar, cw.getLastModifiedDate());

        calendar = cw.getLastModifiedDate();

        cw.deactivate();

        assertNotSame(calendar, cw.getLastModifiedDate());

        calendar = cw.getLastModifiedDate();

        cw.reactivate();

        assertNotSame(calendar, cw.getLastModifiedDate());

        calendar = cw.getLastModifiedDate();

        cw.update(cw3);

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

        CollectorWrapper cw = new CollectorWrapper(collectorA);

        cw.reactivate();

    }

    @Test(expected = RuntimeException.class)
    public void stateWrongTransitionReactivatedToReactivatedTest() {

        CollectorWrapper cw = new CollectorWrapper(collectorA);
        cw.deactivate();
        cw.reactivate();
        cw.reactivate();

    }

    @Test(expected = RuntimeException.class)
    public void stateWrongTransitionDeactivateToUpdatedTest() {

        CollectorWrapper cw = new CollectorWrapper(collectorA);
        CollectorWrapper cw2 = new CollectorWrapper(collectorB);

        cw.deactivate();
        cw.update(cw2);

    }

    @Test(expected = RuntimeException.class)
    public void stateWrongTransitionDeactivateToDeactivateTest() {

        CollectorWrapper cw = new CollectorWrapper(collectorA);

        cw.deactivate();
        cw.deactivate();

    }

}
