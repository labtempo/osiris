/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.labtempo.osiris.remoteconsole;

import br.uff.labtempo.osiris.remoteconsole.storageapi.JSONRpcClient;
import br.uff.labtempo.osiris.remoteconsole.storageapi.RPCClient;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe
 */
public class Client {

    private RPCClient connection;

    public Client() throws Exception {
        connection = new JSONRpcClient<Storage>(Storage.class, "localhost");
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception ignore) {
            }
        }
    }
    
    public Storage getStorageInstance() throws Exception{
        return (Storage) connection.build();
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }
}
