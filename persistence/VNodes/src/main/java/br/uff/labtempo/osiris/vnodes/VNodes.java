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
import br.uff.labtempo.osiris.util.interfaces.Storage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe
 */
public class VNodes extends Module {

    
    public VNodes(){
        super("VNodes");
    }

    @Override
    protected void beforeBoot() throws ComponentInitializationException {
        try {
            loadSystemProperties();
            String host = System.getProperty("module.resource.data.host");
            String resource = System.getProperty("module.resource.data.name");
            JSONRpcClient service = new JSONRpcClient(resource, host, Storage.class);
            RPCConnection.getInstance().addStorageService(service);
            addRequire(service);
            
        } catch (Exception e) {
            throw new ComponentInitializationException(e);
        }
        super.beforeBoot(); 
    }

    @Override
    protected void boostrap() throws ComponentInitializationException {
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

}
