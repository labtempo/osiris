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
import br.uff.labtempo.osiris.sensornet.controller.EventController;
import br.uff.labtempo.osiris.sensornet.controller.NetworkController;
import br.uff.labtempo.osiris.sensornet.controller.SensorController;
import br.uff.labtempo.osiris.sensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.sensornet.persistence.jpa.JpaDaoFactory;
/**
 *
 * @author Felipe
 */
public class Server implements AutoCloseable{

    private final String pwd;
    private final String usr;
    private final String ip;
    
    private JpaDaoFactory factory;

    private OmcpServer server;

    public Server() throws Exception {
        ip = "192.168.0.7";
        usr = "admin";
        pwd = "admin";
        server = new RabbitServer("sensornet", ip, usr, pwd);
        
        factory = new JpaDaoFactory(ip, usr, pwd);
        
        Controller eCtrl = new EventController(factory);
        Controller sCtrl = new SensorController(factory);        
        Controller nCtrl = new NetworkController(factory);        
        Controller cCtrl = new CollectorController(factory);
        
        eCtrl.setNext(sCtrl);
        sCtrl.setNext(nCtrl);
        nCtrl.setNext(cCtrl);        
        
        server.setHandler(eCtrl);
        
        populate(factory);
        
        //server.addReference("omcp://test.ex/*/sample");
        //server.addReference("omcp://test.ex/warning/");
        
    }
    
    public void start(){
        server.start();
    }

    @Override
    public void close() throws Exception {
        server.close();
        factory.close();
    }
    public static void populate( DaoFactory factory){
        EventController controller = new EventController(factory);
        DataBuilder db = null;
        
        db = new DataBuilder("labtempo","labpos");
        for (int i = 0; i < 2; i++) {
            controller.insert(db.generateSample());
        }
        
        db = new DataBuilder("labtempo","datacenter2");
        for (int i = 0; i < 2; i++) {
            controller.insert(db.generateSample());
        }
        
        db = new DataBuilder("posgradS","lab");
        for (int i = 0; i < 6; i++) {
            controller.insert(db.generateSample());
        }
        
        
    }
}
