/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.servertest;

import br.uff.labtempo.omcp.server.Controller;
import br.uff.labtempo.omcp.server.OmcpServer;
import br.uff.labtempo.omcp.server.rabbitmq.RabbitServer;

/**
 *
 * @author Felipe
 */
public class Server implements AutoCloseable{

    private final String pwd;
    private final String usr;
    private final String ip;

    private OmcpServer server;

    public Server() {
        ip = "192.168.0.7";
        usr = "admin";
        pwd = "admin";
        server = new RabbitServer("teste", ip, usr, pwd);
        
        Controller root = new RootController();
        Controller sensor = new VirtualSensorController();
        
        root.setNext(sensor);        
        server.setHandler(root);
        
        server.addReference("omcp://test.ex/*/sample");
        server.addReference("omcp://test.ex/warning/");
        
    }
    
    public void start(){
        server.start();
    }

    @Override
    public void close() throws Exception {
        server.close();
    }

}
