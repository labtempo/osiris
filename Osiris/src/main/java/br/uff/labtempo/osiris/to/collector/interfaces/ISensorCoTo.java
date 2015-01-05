/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.to.collector.interfaces;


import br.uff.labtempo.osiris.to.common.data.ConsumableRuleTo;
import br.uff.labtempo.osiris.to.common.data.ConsumableTo;
import br.uff.labtempo.osiris.to.common.data.InfoTo;
import br.uff.labtempo.osiris.to.common.definitions.LogicalOperator;
import br.uff.labtempo.osiris.to.common.definitions.State;
import br.uff.labtempo.osiris.to.common.data.ValueTo;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public interface ISensorCoTo {

    String getId();

    long getTimestamp();

    State getState();

    void addValue(String name, String value, String unit, String symbol);

    void addValue(String name, long value, String unit, String symbol);

    void addValue(String name, double value, String unit, String symbol);

    void addValue(String name, boolean value, String unit, String symbol);

    void addConsumable(String consumableName, int currentValue);

    void addConsumableRule(String name, String consumableName, LogicalOperator operator, int limitValue, String message);

    void addInfo(String infoKeyName, String infoDescription);

    void addInfo(Map<String, String> infos);

    List<? extends ValueTo> getValuesTo();

    List<? extends ConsumableTo> getConsumablesTo();

    List<? extends ConsumableRuleTo> getConsumableRulesTo();

    List<? extends InfoTo> getInfoTo();

    Map<String, String> getInfo();

    @Deprecated
    Map<String, Integer> getConsumables();

    @Deprecated
    List<Map<String, String>> getRules();

    @Deprecated
    List<Map<String, String>> getValues();

}
