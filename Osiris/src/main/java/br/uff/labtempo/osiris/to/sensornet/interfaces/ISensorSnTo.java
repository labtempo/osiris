/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.to.sensornet.interfaces;

import br.uff.labtempo.osiris.to.common.data.ConsumableTo;
import br.uff.labtempo.osiris.to.common.data.InfoTo;
import br.uff.labtempo.osiris.to.common.definitions.State;
import br.uff.labtempo.osiris.to.common.data.ValueTo;
import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public interface ISensorSnTo {

    String getId();

    long getTimestamp();

    State getState();

    Calendar getLastModified();

    String getCollectorId();

    String getNetworkId();

    void addValue(String name, ValueType valueType, String value, String unit, String symbol);

    void addConsumable(String consumableName, int currentValue);

    void addInfo(Map<String, String> infos);

    void addInfo(String infoKeyName, String infoDescription);

    List<? extends ValueTo> getValuesTo();

    List<? extends ConsumableTo> getConsumablesTo();

    List<? extends InfoTo> getInfoTo();

    Map<String, String> getInfo();

    @Deprecated
    List<Map<String, String>> getValues();

    @Deprecated
    Map<String, Integer> getConsumables();

}
