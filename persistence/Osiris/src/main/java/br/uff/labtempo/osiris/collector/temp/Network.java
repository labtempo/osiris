/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.collector.temp;

import java.util.Map;

/**
 *
 * @author Felipe
 */
public class Network {

    private String id;
    private Map<String, String> info;

    public Network(String id) {
        this.id = id;
    }

    public Map<String, String> getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info.getInfo();
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)//mesma instancia  
        {
            return true;
        }
        if (obj == null)//objeto null sempre igual a false  
        {
            return false;
        }
        if (getClass() != obj.getClass())//são de de classes diferentes  
        {
            return false;
        }

        Network other = (Network) obj;

        if (getId().equals(other.getId())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {        
        return id.hashCode();
    }
    
    public boolean equalsInfo(Map<String, String> info){
        return info.equals(this.info);
    }
}
