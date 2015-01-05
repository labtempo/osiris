/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.thirdparty.announcement;

import br.uff.labtempo.osiris.thirdparty.announcement.core.AnnouncementManager;
import br.uff.labtempo.osiris.thirdparty.announcement.core.AnnouncementProducer;
import br.uff.labtempo.omcp.client.dummy.DummyClient;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;

/**
 *
 * @author Felipe
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
