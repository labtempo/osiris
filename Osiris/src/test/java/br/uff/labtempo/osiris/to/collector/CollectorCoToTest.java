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
package br.uff.labtempo.osiris.to.collector;

import br.uff.labtempo.omcp.common.utils.Serializer;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class CollectorCoToTest {

    private final CollectorCoTo collectorTo;

    public CollectorCoToTest() {
        this.collectorTo = new CollectorCoTo("test", 1, TimeUnit.DAYS);
        collectorTo.addInfo("key", "value");
    }

    @Test
    public void testSomeMethod() {
        CollectorCoTo newCoTo = serializer();
        assertEquals(collectorTo, newCoTo);
    }

    private CollectorCoTo serializer() {
        Serializer serializer = new Serializer();
        String json = serializer.toJson(collectorTo);
        System.out.println(json);
        CollectorCoTo newCoTo = serializer.fromJson(json, CollectorCoTo.class);
        return newCoTo;
    }
}
