/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.to.sensornet.interfaces;

import br.uff.labtempo.osiris.to.common.data.InfoTo;
import br.uff.labtempo.osiris.to.common.definitions.State;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public interface INetworkSnTo {

    String getId();

    State getState();

    Calendar getLastModified();

    int getTotalCollectors();

    int getTotalSensors();
    
    void addInfo(Map<String, String> infos);

    void addInfo(String infoKeyName, String infoDescription);

    List<? extends InfoTo> getInfoTo();

    Map<String, String> getInfo();
    
    

}
