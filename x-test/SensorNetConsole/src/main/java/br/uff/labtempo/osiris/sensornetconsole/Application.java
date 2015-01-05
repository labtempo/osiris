/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornetconsole;

import br.uff.labtempo.osiris.util.components.ComponentInitializationException;
import br.uff.labtempo.osiris.util.components.Module;
import br.uff.labtempo.osiris.util.components.conn.OnMessageListener;
import br.uff.labtempo.osiris.util.components.conn.Subscriber;
import br.uff.labtempo.osiris.util.data.DataPacket;

/**
 *
 * @author Felipe
 */
public class Application extends Module implements OnMessageListener {

    public Application() {
        super("SensorNetConsole");
    }

    @Override
    protected void onCreate() throws ComponentInitializationException {
        addRequire(new Subscriber(new String[]{"sensornet"}, "localhost", this));
    }

    public void onReceiveMessage(String message, String subject) {
        String[] m = message.split("::");
        DataPacket packet = DataPacket.create(m[1]);
        
        System.out.println("Event: "+m[0]+"\tSource: "+packet.getFullResourcePath());
    }

}
