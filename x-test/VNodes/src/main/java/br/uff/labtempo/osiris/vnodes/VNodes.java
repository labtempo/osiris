/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.vnodes;

import br.uff.labtempo.osiris.vnodes.conn.RPCConnection;
import br.uff.labtempo.osiris.vnodes.vbox.VBox;
import br.uff.labtempo.osiris.util.components.ComponentInitializationException;
import br.uff.labtempo.osiris.util.components.Module;
import br.uff.labtempo.osiris.util.components.conn.JSONRpcClient;
import br.uff.labtempo.osiris.util.components.conn.JSONRpcServer;
import br.uff.labtempo.osiris.util.components.conn.OnMessageListener;
import br.uff.labtempo.osiris.util.components.conn.Subscriber;
import br.uff.labtempo.osiris.util.interfaces.Network;
import br.uff.labtempo.osiris.util.interfaces.Storage;
import br.uff.labtempo.osiris.util.interfaces.VSensor;
import br.uff.labtempo.osiris.vnodes.conn.SensorNet;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class VNodes extends Module implements OnMessageListener {
    
    public VNodes() {
        super("VNodes");
    }
    
    @Override
    protected void onCreate() throws ComponentInitializationException {
        try {
            loadSystemProperties();
            String host = System.getProperty("module.resource.data.host");
            String resource = System.getProperty("module.resource.data.name");
            JSONRpcClient storage = new JSONRpcClient(resource, host, Storage.class);
            JSONRpcClient sensornet = new JSONRpcClient("sensornet", host, Network.class);
            RPCConnection.getInstance().addStorageService(storage);
            RPCConnection.getInstance().addNetworkService(sensornet);
            String[] subjects = {"network.*.#"};
            Subscriber subscriber = new Subscriber(subjects, "localhost", this);
            addRequire(storage);
            addRequire(sensornet);
            addRequire(subscriber);
            addProvide(new JSONRpcServer("vnodes", "localhost", VBox.getInstance(), VSensor.class));
            
        } catch (Exception e) {
            throw new ComponentInitializationException(e);
        }
    }
    
    @Override
    protected void onStart() throws ComponentInitializationException {
        try {
            VBox.getInstance().loadDataFromStorage();
        } catch (Exception ex) {
            try {
                VBox.getInstance().createNewStorageDataSchema();
            } catch (Exception e) {
                throw new ComponentInitializationException(e);
            }
        }
    }    
    
    private void loadSystemProperties() throws IOException {
        Properties prop = new Properties();
        InputStream input = VNodes.class.getClassLoader().getResourceAsStream("module/files/config.xml");
        prop.loadFromXML(input);
        input.close();
        prop.putAll(System.getProperties());
        System.setProperties(prop);
    }
    
    @Override
    public void onReceiveMessage(String message, String subject) {
        VBox.getInstance().newMeasure(message);
    }
    
}
