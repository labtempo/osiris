/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.server;

/**
 *
 * @author Felipe
 */
public class Server {

    public static void main(String[] args) throws Exception {
        OmcpServer server = new OmcpServer("sensornet","192.168.0.7","admin","admin");
        server.start();
    }
}
