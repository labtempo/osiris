/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.announcer;

import br.uff.labtempo.osiris.sensornet.persistence.AnnouncerDao;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe
 */
public class Producer implements AnnouncerDao {

    private final BlockingQueue<String> queue;

    public Producer(BlockingQueue<String> q) {
        this.queue = q;
    }

    @Override
    public void save(String o) {
        try {
            queue.put(o);
        } catch (InterruptedException ex) {
            Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
