/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.labtempo.osiris.virtualstorage;

import br.uff.labtempo.osiris.util.components.ComponentInitializationException;
import br.uff.labtempo.osiris.util.components.Module;
import br.uff.labtempo.osiris.util.components.conn.JSONRpcServer;
import br.uff.labtempo.osiris.util.interfaces.Storage;

/**
 *
 * @author Felipe
 */
public class VirtualStorage extends Module{

    public VirtualStorage() {
        super("Virtual Storage");
    }

    @Override
    protected void beforeBoot() throws ComponentInitializationException {        
        try {
            addRequire(new JSONRpcServer("storage", "localhost",new MemoryStorage(), Storage.class));
        } catch (Exception e) {
            throw new ComponentInitializationException(e);
        }        
        super.beforeBoot();
    }
    
    
    
}
