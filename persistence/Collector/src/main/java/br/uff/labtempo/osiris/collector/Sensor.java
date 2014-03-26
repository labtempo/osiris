/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.collector;

import br.uff.labtempo.osiris.util.components.ComponentInitializationException;
import br.uff.labtempo.osiris.util.components.Service;
import br.uff.labtempo.osiris.util.components.conn.Publisher;
import br.uff.labtempo.osiris.util.data.DataPacket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Felipe
 */
public class Sensor extends Service {

    private int id;
    private int delay;
    private Publisher publisher;
    private String reference;
    private Integer parent;

    public Sensor(int id, int delay, Publisher publisher, String reference, Integer parent) {
        super("Sensor " + id);
        this.id = id;
        this.delay = delay;
        this.publisher = publisher;
        this.reference = reference;
        this.parent = parent;
    }

    Sensor(int id, int delay, Publisher publisher, String reference) {
        this(id, delay, publisher, reference, null);
    }

    @Override
    protected void onCreate() throws ComponentInitializationException {
    }

    @Override
    protected void onLoop() throws ComponentInitializationException {
        while (publisher.isActive()) {
            try {
                Thread.sleep(delay);

                long time = System.currentTimeMillis();

                DataPacket packet = DataPacket.create(reference, String.valueOf(id), getParent(), time, delay, getSample());

                if (publisher.publish(packet.toString(), String.valueOf(id))) {
                    System.out.println(id + ": " + packet + " - " + getDate(time));
                }
            } catch (InterruptedException ex) {
            }
        }
    }

    private String getDate(Long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSSSSS dd-MM-yyyy z");
        Date resultdate = new Date(millis);
        return (sdf.format(resultdate));
    }

    private String getParent() {
        if (this.parent != null) {
            return String.valueOf(this.parent);
        } else {
            return null;
        }
    }

    private String getSample() {

        int var = (int) (10 * Math.random());
        int tempBase = 30 + var;
        int lightBase = 20;

        String sample = "temp:" + tempBase + ",light:" + lightBase;
        return sample;
    }
}
