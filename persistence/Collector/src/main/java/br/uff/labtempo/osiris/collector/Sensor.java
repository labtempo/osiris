/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.collector;

import br.uff.labtempo.osiris.util.components.ComponentInitializationException;
import br.uff.labtempo.osiris.util.components.Service;
import br.uff.labtempo.osiris.util.components.conn.Publisher;

/**
 *
 * @author Felipe
 */
public class Sensor extends Service {

    private int id;
    private int time;
    private Publisher publisher;

    public Sensor(int id, int time, Publisher publisher) {
        super("Sensor " + id);
        this.id = id;
        this.time = time;
        this.publisher = publisher;
    }

    @Override
    protected void onCreate() throws ComponentInitializationException {
    }

    @Override
    protected void onLoop() throws ComponentInitializationException {
        while (publisher.isActive()) {
            try {
                Thread.sleep(time);
                String message = id+";" + System.currentTimeMillis() + ";" + (int) (10 * Math.random());
                if (publisher.publish(message,String.valueOf(id))) {
                    System.out.println(id+": " + message);
                }
            } catch (InterruptedException ex) {
            }
        }
    }
}
