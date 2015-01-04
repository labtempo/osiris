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
public class CollectorApi {
    private long collctionInterval;
    private String id;
    private Properties properties;
    
    public CollectorApi(String id, long collctionInterval) {
        this.id = id;
        this.collctionInterval = collctionInterval;
        this.properties = new Properties();        
    }
    
    public void addInfo(String name, String description){
        properties.setProperty(name, description);
    }
    
    public void removeInfo(String name){
        properties.remove(name);
    }
            
}
