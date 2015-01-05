/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.util.components.conn.rpc;

import br.uff.labtempo.osiris.util.components.conn.Config;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class RemoteListener <T>{
    
    private RPCServer<T> server;
    private Properties prop;

    public RemoteListener(String host, String resourceName, T instance, Class<?> klass) throws Exception {
        loadProperties();
        String exchange = prop.getProperty("amqp.rpc.exchange");
        String exchangeType = prop.getProperty("amqp.rpc.exchange.type");
        String queue = prop.getProperty("amqp.rpc.routing.key")+resourceName;
       
        this.server  = new JSONRpcServer<T>(host, exchange, exchangeType, queue, queue, instance, klass);
    }

    public void start() {
        setShutdownHook();
        server.start();
    }

    private void setShutdownHook() {
        System.out.println("Press Control+C to shutdown the server!\nRunning...");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                close();
            }
        });
    }

    private void close() {
        System.out.println("Server is shutting down now...");
        try {
            server.close();
        } catch (Exception ex) {
            Logger.getLogger(RemoteListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Server is down!");
    }

    private void loadProperties() throws Exception {
        if (prop == null) {
            prop = Config.getProperties();
        }
        
    }

}
