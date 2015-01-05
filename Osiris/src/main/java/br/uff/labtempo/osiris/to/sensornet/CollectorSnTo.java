/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.to.sensornet;

import br.uff.labtempo.osiris.to.sensornet.interfaces.ICollectorSnTo;
import br.uff.labtempo.osiris.to.common.base.CollectorToBase;
import br.uff.labtempo.osiris.to.common.definitions.State;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Felipe
 */
public class CollectorSnTo extends CollectorToBase implements ICollectorSnTo {

    private final long lastModified;
    private final String networkId;
    private final int totalSensors;
    
    //helper attributes
    private transient Calendar helperLastModified;

    public CollectorSnTo(String id, State state, long interval, TimeUnit timeUnit, Calendar lastModifiedDate, String networkId, int totalSensors) {
        super(id, state, interval, timeUnit);
        this.lastModified = lastModifiedDate.getTimeInMillis();
        this.networkId = networkId;
        this.totalSensors = totalSensors;
        
        this.helperLastModified = lastModifiedDate;
    }

    @Override
    public Calendar getLastModified() {
        if (helperLastModified == null) {
            helperLastModified = Calendar.getInstance();
            helperLastModified.setTimeInMillis(lastModified);
        }
        return helperLastModified;
    }

    @Override
    public String getNetworkId() {
        return networkId;
    }

    @Override
    public int getTotalSensors() {
        return totalSensors;
    }

    @Override
    public void addInfo(String infoKeyName, String infoDescription) {
        super.addInfo(infoKeyName, infoDescription);
    }

    @Override
    public void addInfo(Map<String, String> infos) {
        super.addInfo(infos);
    }

    @Override
    public Map<String, String> getInfo() {
        return super.getInfo();
    }

}
