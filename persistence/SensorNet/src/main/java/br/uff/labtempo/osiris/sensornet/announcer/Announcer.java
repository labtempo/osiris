/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.announcer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author Felipe
 */
public class Announcer {

    private static BlockingQueue<String> queue;

    public Announcer(String ip, String usr, String pwd) {
        if (queue == null) {
            queue = new LinkedBlockingQueue<>();

            Consumer consumer = new Consumer(queue, ip, usr, pwd);

            Thread t2 = new Thread(consumer,"Announcer consumer");
            t2.setDaemon(true);
            t2.start();
        }
    }

    public Producer getProducer() {
        return new Producer(queue);
    }

}
