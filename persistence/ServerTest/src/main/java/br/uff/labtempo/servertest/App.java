package br.uff.labtempo.servertest;

import java.util.logging.Level;
import java.util.logging.Logger;
import service.Service;

/**
 * Hello world!
 *
 */
public class App {

    static Server server;

    public static void main(String[] args) {
        server = new Server();
        //shutdown();
        server.start();
    }

    private static void shutdown() {
        Thread t = new Thread() {
            public void run() {
                try {
                    Thread.sleep(6000);
                    try {
                        server.close();
                    } catch (Exception ex) {
                        Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        t.setDaemon(true);
        t.start();

    }
}
