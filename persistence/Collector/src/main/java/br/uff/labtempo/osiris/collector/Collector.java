/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.collector;

import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.omcp.client.OmcpClientBuilder;

/**
 *
 * @author Felipe
 */
public class Collector implements AutoCloseable{

    public void execute() throws Exception{
        try (OmcpClient connection = new OmcpClientBuilder().host("192.168.0.7").user("admin", "admin").source("virtual-lab").build()) {
            populate(connection);
        }
    }
    
    public void populate(OmcpClient client) throws Exception {
        DataBuilder db = null;

        db = new DataBuilder("labtempo", "labpos");
        for (int i = 0; i < 2; i++) {            
            client.doNofity(getUri(),db.generateSample());
        }

        db = new DataBuilder("labtempo", "datacenter1");
        for (int i = 0; i < 2; i++) {
            client.doNofity(getUri(),db.generateSample());
        }
        db = new DataBuilder("labtempo", "datacenter2");
        for (int i = 0; i < 2; i++) {
            client.doNofity(getUri(),db.generateSample());
        }

        db = new DataBuilder("posgradS", "lab1");
        for (int i = 0; i < 6; i++) {
            client.doNofity(getUri(),db.generateSample());
        }

        db = new DataBuilder("posgradS", "lab2");
        for (int i = 0; i < 6; i++) {
            client.doNofity(getUri(),db.generateSample());
        }

    }
    
    @Override
    public void close() throws Exception {
        
    }
    
    private String getUri(){
        String uri = "omcp://collector.messagegroup/";
        return uri;
    }
    

}
