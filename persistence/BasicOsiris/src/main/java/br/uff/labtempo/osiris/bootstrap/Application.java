/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.labtempo.osiris.bootstrap;

import br.uff.labtempo.osiris.controller.ClientController;
import br.uff.labtempo.osiris.controller.CollectorController;
import br.uff.labtempo.osiris.data.DataManager;
import br.uff.labtempo.osiris.controller.exceptions.NewMoteException;
import br.uff.labtempo.osiris.util.components.ComponentInitializationException;
import br.uff.labtempo.osiris.util.components.Module;
import br.uff.labtempo.osiris.util.components.conn.JSONRpcServer;
import br.uff.labtempo.osiris.util.components.conn.OnMessageListener;
import br.uff.labtempo.osiris.util.components.conn.Publisher;
import br.uff.labtempo.osiris.util.components.conn.Subscriber;
import br.uff.labtempo.osiris.util.data.DataPacket;
import br.uff.labtempo.osiris.util.interfaces.Client;

/**
 *
 * @author Felipe
 */
public class Application extends Module implements OnMessageListener{
    
    private DataManager datamanager;
    private ClientController client;
    private CollectorController collector;
    private Publisher publisher;
    private final String EVENT_DELIMITER = "::";
    
    public Application() {
        super("Osiris Framework");
    }

    @Override
    protected void onCreate() throws ComponentInitializationException {
        try {
            datamanager = new DataManager();
            collector = new CollectorController(datamanager);
            client = new ClientController(datamanager);
            publisher = new Publisher("sensornet", "localhost");
            String[] subjects = {"network.*.#"};
            Subscriber subscriber = new Subscriber(subjects, "localhost", this);
            
            //begins before - ends after
            addRequire(datamanager);            
            addRequire(publisher);
            
            //begins after - ends before
            addProvide(collector);
            addProvide(client);
            addProvide(subscriber);
            addProvide(new JSONRpcServer("osiris", "localhost", client, Client.class));

        } catch (Exception e) {
            throw new ComponentInitializationException(e);
        }
    }

    @Override
    public void onReceiveMessage(String message, String subject) {
        DataPacket packet = DataPacket.create(message);
        try {
            collector.updateMote(packet);
        } catch (NewMoteException ex) {
            publisher.publish("NODE.FOUND"+EVENT_DELIMITER+packet.toString());
        }
    }
}
