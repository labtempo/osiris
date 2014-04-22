/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.labtempo.osiris.util.interfaces;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public interface Client {
    public void createVSensor(String name);
    public void bind(String vsensorId, String moteId);
    public void unbind(String vsensorId);
    public Map<String,List<String>> getFreeItems();
    public Map<String,List<String>> getBoundItems();
    public List<String> getSamples(String vsensorId);
}
