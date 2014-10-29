/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.announcer;

import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.omcp.client.rabbitmq.RabbitClient;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author Felipe
 */
public class Consumer implements Runnable {

    private final BlockingQueue<String> queue;
    private OmcpClient client;

    public Consumer(BlockingQueue<String> q, String ip, String usr, String pwd) {
        this.queue = q;
        //client = new RabbitClient(usr, usr, pwd);
    }

    @Override
    public void run() {
        Thread.currentThread().setPriority(2);
        while (true) {
            try {
                System.out.println("***ANNOUNCE*** " + queue.take());
            } catch (InterruptedException ex) {
            }
        }

    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        //client.close();        
    }

}
