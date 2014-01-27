/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.remotestorage;

import br.uff.labtempo.osiris.remotestorage.amqp.RPCServer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe
 */
public class Server {

    CallRouter router;
    RPCServer connection;

    public Server(Storage storage) {
        this.router = new CallRouter(storage);
    }

    public void start() {
        setShutdownHook();
        this.connection = new RPCServer();
        this.connection.setOnCallListener(router); 
        this.connection.start();
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
            connection.close();
        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Server is down!");
    }

}
