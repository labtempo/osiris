/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.vnodes.conn;

import br.uff.labtempo.osiris.util.components.conn.JSONRpcClient;
import br.uff.labtempo.osiris.util.components.conn.rpc.RemoteCall;
import br.uff.labtempo.osiris.util.interfaces.Storage;

/**
 *
 * @author Felipe
 */
public class RPCConnection {

    private static RPCConnection instance;
    private JSONRpcClient storage;

    private RPCConnection() {
    }

    public static RPCConnection getInstance() {
        if (instance == null) {
            instance = new RPCConnection();
        }
        return instance;
    }

    public void addStorageService(JSONRpcClient storage){
        this.storage = storage;
    }
    
    public Storage getStorage(){
        return storage.<Storage>getProxy();
    }
}
