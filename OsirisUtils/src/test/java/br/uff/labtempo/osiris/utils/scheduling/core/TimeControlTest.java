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
package br.uff.labtempo.osiris.utils.scheduling.core;

import br.uff.labtempo.osiris.utils.scheduling.core.TimeControl;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class TimeControlTest {

    private TimeControl timeControl;

    public TimeControlTest() {
    }

    @Before
    public void createTimeControl() {
        this.timeControl = new TimeControl(100);
    }

    @Test
    public void testChangeInterval_Downshift_ShouldPass() {
        for (int i = 0; i < 12; i++) {
            timeControl.addInterval(200);
        }
        assertEquals(200, timeControl.getIntervalInMillis());
    }

    @Test
    public void testChangeInterval_Upshift_ShouldPass() {
        timeControl.addInterval(50);
        assertEquals(50, timeControl.getIntervalInMillis());
    }

    @Test
    public void testChangeInterval_AlternatedDownshift_ShouldPass() {
        for (int i = 0; i < 12; i++) {
            timeControl.addInterval(200);
            timeControl.addInterval(190);
        }
        assertEquals(190, timeControl.getIntervalInMillis());
    }

    @Test
    public void testNotChangeInterval_ShouldPass() {
        for (int i = 0; i < 50; i++) {
            timeControl.addInterval(200);
            timeControl.addInterval(100);
            timeControl.addInterval(190);
        }
        assertEquals(100, timeControl.getIntervalInMillis());
    }

}
