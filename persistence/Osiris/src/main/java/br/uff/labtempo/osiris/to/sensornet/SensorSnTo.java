package br.uff.labtempo.osiris.to.sensornet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import br.uff.labtempo.osiris.to.sensornet.interfaces.ISensorSnTo;
import br.uff.labtempo.osiris.to.common.base.SensorToBase;
import br.uff.labtempo.osiris.to.common.definitions.State;
import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public class SensorSnTo extends SensorToBase implements ISensorSnTo {

    private final long lastModified;
    private final String networkId;
    private final String collectorId;

    //helper attributes
    private transient Calendar helperLastModified;

    public SensorSnTo(String id, State state, long captureTimestamp, Calendar lastModifiedDate, String networkId, String collectorId) {
        super(id, state, captureTimestamp);
        this.lastModified = lastModifiedDate.getTimeInMillis();
        this.networkId = networkId;
        this.collectorId = collectorId;

        this.helperLastModified = lastModifiedDate;
    }

    @Override
    public String getNetworkId() {
        return networkId;
    }

    @Override
    public String getCollectorId() {
        return collectorId;
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
    public void addValue(String name, ValueType valueType, String value, String unit, String symbol) {
        super.addValue(name, valueType, value, unit, symbol);
    }

    @Override
    public void addConsumable(String consumableName, int currentValue) {
        super.addConsumable(consumableName, currentValue);
    }

    @Override
    public void addInfo(Map<String, String> infos) {
        super.addInfo(infos);
    }

    @Override
    public void addInfo(String infoKeyName, String infoDescription) {
        super.addInfo(infoKeyName, infoDescription);
    }

    @Deprecated
    @Override
    public List<Map<String, String>> getValues() {
        return super.getValues();
    }

    @Deprecated
    @Override
    public Map<String, Integer> getConsumables() {
        return super.getConsumables();
    }

    @Override
    public Map<String, String> getInfo() {
        return super.getInfo();
    }
}
