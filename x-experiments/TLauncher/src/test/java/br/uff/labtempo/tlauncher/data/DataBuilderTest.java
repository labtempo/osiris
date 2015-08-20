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
package br.uff.labtempo.tlauncher.data;

import br.uff.labtempo.omcp.client.dummy.DummyClient;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class DataBuilderTest {

    public DataBuilderTest() {
    }

    @Test
    public void testSomeMethod() {
        CollectorDataBuilder db = new CollectorDataBuilder(new DummyClient(), "latency");
        long t1 = System.nanoTime();
        for (int i = 0; i < 10000000; i++) {
            db.publishSample("1", i);
        }
        long t2 = System.nanoTime();

        System.out.println(t2 - t1);
        System.out.println(db.makeUrl("1"));
    }   
}
