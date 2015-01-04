/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import br.uff.labtempo.omcp.service.OmcpService;
import br.uff.labtempo.omcp.service.rabbitmq.RabbitPersistentService;
import br.uff.labtempo.omcp.service.rabbitmq.RabbitService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe
 */
public class Service {

    static OmcpService service;

    public static void main(String[] args) throws Exception {
        service = new RabbitService("192.168.0.7", "admin", "admin");
        //service.addReference("omcp://notification.messagegroup/#");
        service.addReference("omcp://update.messagegroup/#");

        //shutdown();
        service.start();

    }

    private static void shutdown() {
        Thread t = new Thread() {
            public void run() {
                try {
                    Thread.sleep(6000);
                    try {
                        service.close();
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
