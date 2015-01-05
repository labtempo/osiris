/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet;

import br.uff.labtempo.osiris.omcp.Controller;
import br.uff.labtempo.omcp.server.OmcpServer;
import br.uff.labtempo.omcp.server.rabbitmq.RabbitServer;
import br.uff.labtempo.osiris.sensornet.controller.CollectorController;
import br.uff.labtempo.osiris.sensornet.controller.NetworkController;
import br.uff.labtempo.osiris.sensornet.controller.NotifyController;
import br.uff.labtempo.osiris.sensornet.controller.SensorController;
import br.uff.labtempo.osiris.sensornet.persistence.jpa.JpaDaoFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe
 */
public class Bootstrap implements AutoCloseable {

    private final String pwd;
    private final String usr;
    private final String ip;
    private final String moduleName;

    private JpaDaoFactory factory;

    private OmcpServer server;

    public Bootstrap() throws Exception {
        ip = "192.168.0.7";
        usr = "admin";
        pwd = "admin";
        moduleName = "sensornet";

        try {
            server = new RabbitServer(moduleName, ip, usr, pwd);

            factory = JpaDaoFactory.newInstance(ip, usr, pwd, moduleName);

            Controller eCtrl = new NotifyController(factory);
            Controller sCtrl = new SensorController(factory);
            Controller nCtrl = new NetworkController(factory);
            Controller cCtrl = new CollectorController(factory);

            eCtrl.setNext(sCtrl);
            sCtrl.setNext(nCtrl);
            nCtrl.setNext(cCtrl);

            server.setHandler(eCtrl);

            server.addReference("omcp://collector.messagegroup/");
        } catch (Exception ex) {
            close();
            throw ex;
        }
        //server.addReference("omcp://test.ex/warning/");
    }

    public void start() {
        server.start();
    }

    @Override
    public void close() {
        try {
            server.close();
        } catch (Exception e) {
        }
        try {
            factory.close();
        } catch (Exception e) {
        }

    }
}
