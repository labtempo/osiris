/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet;

import br.uff.labtempo.osiris.util.components.ComponentInitializationException;
import br.uff.labtempo.osiris.util.components.Module;
import br.uff.labtempo.osiris.util.components.conn.JSONRpcServer;
import br.uff.labtempo.osiris.util.components.conn.OnMessageListener;
import br.uff.labtempo.osiris.util.components.conn.Publisher;
import br.uff.labtempo.osiris.util.components.conn.Subscriber;
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
public class SensorNet extends Module implements OnMessageListener, Network {

    private Map<String, String> nodes;
    private Publisher publisher;

    public SensorNet() {
        super("SensorNet");
        nodes = new HashMap<String, String>();
    }

    @Override
    protected void onCreate() throws ComponentInitializationException {
        try {
            publisher = new Publisher("sensornet", "localhost");
            String[] subjects = {"network.*.#"};
            Subscriber subscriber = new Subscriber(subjects, "localhost", this);
            
            addRequire(publisher);
            addRequire(subscriber);
            
            addProvide(new JSONRpcServer("sensornet", "localhost", this, Network.class));

        } catch (Exception e) {
            throw new ComponentInitializationException(e);
        }
    }

    public void onReceiveMessage(String message, String subject) {
        if (!nodes.containsKey(subject)) {
            System.out.println("NOVO NÓ!!");
        }
        nodes.put(subject, message);
        System.out.println(subject + " : " + message);
    }

    public boolean getNode(String nodeId) {
        Log.D(nodeId);
        if (nodes.containsKey(nodeId)) {
            return true;
        }
        return false;
    }
}
