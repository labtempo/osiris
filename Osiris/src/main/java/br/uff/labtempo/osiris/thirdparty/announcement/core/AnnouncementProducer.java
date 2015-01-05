/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.thirdparty.announcement.core;
import br.uff.labtempo.osiris.thirdparty.announcement.Announcer;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe
 */
public class AnnouncementProducer  implements Announcer{

    private final BlockingQueue<AnnouncementItem> queue;

    AnnouncementProducer(BlockingQueue<AnnouncementItem> queue) {
        this.queue = queue;
    }

  
    @Override
    public void announce(Object obj, String address) {        
        try {
            queue.put(new AnnouncementItem(obj, address));
        } catch (InterruptedException ex) {
            Logger.getLogger(AnnouncementProducer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
