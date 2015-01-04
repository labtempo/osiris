/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.collector.api;

import java.util.Properties;

/**
 *
 * @author Felipe
 */
class NetworkApi {
    private String id;
    private NetworkApi network;
    private Properties properties;

    public NetworkApi(String id) {
        this.id = id;
        this.properties = new Properties();        
    }
    
    public void addInfo(String name, String description){
        properties.setProperty(name, description);
    }
    
    public void removeInfo(String name){
        properties.remove(name);
    }
   
}
