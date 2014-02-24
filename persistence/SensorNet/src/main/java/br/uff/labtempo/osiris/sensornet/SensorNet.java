/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet;

import br.uff.labtempo.osiris.util.components.ComponentInitializationException;
import br.uff.labtempo.osiris.util.components.Module;
import br.uff.labtempo.osiris.util.components.conn.Publisher;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe
 */
public class SensorNet extends Module {

    private Publisher publisher;

    public SensorNet() {
        super("SensorNet");
    }

    @Override
    protected void onCreate() throws ComponentInitializationException {
        try {
            publisher = new Publisher("sensornet", "localhost");
            addRequire(publisher);
        } catch (Exception e) {
            throw new ComponentInitializationException(e);
        }
    }

    @Override
    protected void onStart() throws ComponentInitializationException {
        DataPacket packet = new DataPacket();
        while (publisher.isActive()) {
            try {
                Thread.sleep(6000);
                packet.sensorID = "teste";
                packet.timestamp = System.currentTimeMillis();
                packet.measures = "temp:" + (int) (10 * Math.random());
                if (publisher.publish(packet.toString())) {
                    System.out.println("sent: " + packet);
                }
            } catch (InterruptedException ex) {
            }
        }
    }
}
