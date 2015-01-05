/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.remoteconsole;

import br.uff.labtempo.osiris.util.components.ComponentInitializationException;
import br.uff.labtempo.osiris.util.components.Module;
import br.uff.labtempo.osiris.util.components.conn.JSONRpcClient;
import br.uff.labtempo.osiris.util.interfaces.Client;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class Application extends Module {
    private JSONRpcClient client;
    private Console console;
    
    public Application() {
        super("Osiris Console");
    }

    @Override
    protected void onCreate() throws ComponentInitializationException {
        this.client = new JSONRpcClient("osiris", "localhost", Client.class);
        this.console = new Console();
        addRequire(client);
        addProvide(console);
        
    }

    @Override
    protected void onStart() throws ComponentInitializationException {
        super.onStart();
        console.setClient(client.<Client>getProxy());
        
        
    }
    
    
}
