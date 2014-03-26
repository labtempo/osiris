/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet;

import br.uff.labtempo.osiris.sensornet.core.SensorNet;
import br.uff.labtempo.osiris.util.components.ComponentInitializationException;
import br.uff.labtempo.osiris.util.components.Module;
import br.uff.labtempo.osiris.util.components.conn.JSONRpcServer;
import br.uff.labtempo.osiris.util.components.conn.OnMessageListener;
import br.uff.labtempo.osiris.util.components.conn.Publisher;
import br.uff.labtempo.osiris.util.components.conn.Subscriber;
import br.uff.labtempo.osiris.util.data.DataPacket;
import br.uff.labtempo.osiris.util.interfaces.Network;
import br.uff.labtempo.osiris.util.logging.Log;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe
 */
public class Application extends Module implements OnMessageListener {

    
    private SensorNet sensornet;
    private Publisher publisher;

    public Application() {
        super("SensorNet");
        sensornet = new SensorNet();
    }

    @Override
    protected void onCreate() throws ComponentInitializationException {
        try {
            //publisher = new Publisher("sensornet", "localhost");
            String[] subjects = {"network.*.#"};
            Subscriber subscriber = new Subscriber(subjects, "localhost", this);
            
            //addRequire(publisher);
            addRequire(subscriber);
            
            //addProvide(new JSONRpcServer("sensornet", "localhost", this, Network.class));

        } catch (Exception e) {
            throw new ComponentInitializationException(e);
        }
    }

    public void onReceiveMessage(String message, String subject) {
        sensornet.newSensorEvent(DataPacket.create(message));
    }
}
