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
package br.uff.labtempo.osiris.sensornet.model;

import br.uff.labtempo.osiris.to.collector.CollectorCoTo;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class CollectorWrapperTest {

    private CollectorCoTo collectorA, collectorB;

    public CollectorWrapperTest() {
        Map<String, String> info = new HashMap<>();
        info.put("chave", "valor");

        this.collectorA = new CollectorCoTo("10", 2, TimeUnit.MINUTES);
        this.collectorB = new CollectorCoTo("50", 2, TimeUnit.MINUTES);

        collectorA.addInfo(info);
        collectorB.addInfo(info);

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

    @Test
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
