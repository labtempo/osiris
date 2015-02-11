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
package br.uff.labtempo.osiris.utils.announcement;

import br.uff.labtempo.osiris.utils.announcement.core.AnnouncementManager;
import br.uff.labtempo.osiris.utils.announcement.core.AnnouncementProducer;
import br.uff.labtempo.omcp.client.dummy.DummyClient;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class AnnouncementManagerTest {

    public AnnouncementManagerTest() {
    }

    @Test
    public void testSomeMethod() throws InterruptedException, Exception {
        AnnouncementManager manager = new AnnouncementManager(new DummyClient());
        manager.initialize();

        Thread p1, p2, p3;

        p1 = new Producer(manager.getAnnouncer());
        p2 = new Producer(manager.getAnnouncer());
        p3 = new Producer(manager.getAnnouncer());
        
        p1.start();
        p2.start();
        p3.start();
        
        
        p1.join(); 
        p2.join();
        p3.join(); 
        
        
       manager.close();
        
        

    }

    class Producer extends Thread {

        private final AnnouncementProducer announcer;
        private final Random random;

        public Producer(AnnouncementProducer announcer) {
            this.announcer = announcer;
            this.random = new Random();
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    int time = random.nextInt(20) * 10;
                    announcer.announce("time - "+time, "omcp://update.messagegroup/test");
                    Thread.sleep(time);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AnnouncementManagerTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
