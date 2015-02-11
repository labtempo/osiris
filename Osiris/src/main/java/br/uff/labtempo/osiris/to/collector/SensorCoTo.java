/* 
 * Copyright 2015 Felipe Santos <fralph at ic.uff.br>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.uff.labtempo.osiris.to.collector;

import br.uff.labtempo.osiris.to.collector.interfaces.ISensorCoTo;
import br.uff.labtempo.osiris.to.common.base.SensorToBase;
import br.uff.labtempo.osiris.to.common.data.ConsumableRuleTo;
import br.uff.labtempo.osiris.to.common.definitions.LogicalOperator;
import br.uff.labtempo.osiris.to.common.definitions.State;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class SensorCoTo extends SensorToBase implements ISensorCoTo {

    private List<Map<String, String>> consumableRules;

    //helper attributes
    private transient List<ConsumableRuleTo> helperConsumableRuleToList;

    public SensorCoTo(String id, State state, long captureTimestampInMillis, int capturePrecisionInNano, long acquisitionTimestampInMillis) {
        super(id, state, captureTimestampInMillis, capturePrecisionInNano, acquisitionTimestampInMillis);
        this.consumableRules = new ArrayList<>();
    }

    public SensorCoTo(String id, long captureTimestampInMillis, int capturePrecisionInNano) {
        this(id, State.NEW, captureTimestampInMillis, capturePrecisionInNano, System.currentTimeMillis());
    }

    public SensorCoTo(String id, State state, long captureTimestampInMillis, int capturePrecisionInNano) {
        this(String.valueOf(id), state, captureTimestampInMillis, capturePrecisionInNano, System.currentTimeMillis());
    }

    public SensorCoTo(long id, long captureTimestampInMillis) {
        this(String.valueOf(id), captureTimestampInMillis, 0);
    }

    public SensorCoTo(String id) {
        this(id, System.currentTimeMillis(), 0);
    }

    public SensorCoTo(long id, State state) {
        this(String.valueOf(id), state, System.currentTimeMillis(),0);
    }

    public SensorCoTo(long id) {
        this(String.valueOf(id));
    }

    @Override
    public void addValue(String name, String value, String unit, String symbol) {
        super.addValue(name, value, unit, symbol);
    }

    @Override
    public void addValue(String name, long value, String unit, String symbol) {
        super.addValue(name, value, unit, symbol);
    }

    @Override
    public void addValue(String name, double value, String unit, String symbol) {
        super.addValue(name, value, unit, symbol);
    }

    @Override
    public void addValue(String name, boolean value, String unit, String symbol) {
        super.addValue(name, value, unit, symbol);
    }

    @Override
    public void addInfo(Map<String, String> infos) {
        super.addInfo(infos);
    }

    @Override
    public void addInfo(String infoKeyName, String infoDescription) {
        super.addInfo(infoKeyName, infoDescription);
    }

    @Override
    public void addConsumable(String consumableName, int currentValue) {
        super.getConsumables().put(consumableName, currentValue);
    }

    @Override
    public void addConsumableRule(String name, String consumableName, LogicalOperator operator, int limitValue, String message) {
        InternalConsumableRuleTo ruleTo = new InternalConsumableRuleTo(name, consumableName, operator, limitValue, message);
        consumableRules.add(ruleTo.toMap());
    }

    @Deprecated
    @Override
    public Map<String, Integer> getConsumables() {
        return super.getConsumables();
    }

    @Deprecated
    @Override
    public List<Map<String, String>> getRules() {
        return consumableRules;
    }

    @Deprecated
    @Override
    public List<Map<String, String>> getValues() {
        return super.getValues();
    }
    
    @Override
    public Map<String, String> getInfo() {
        return super.getInfo();
    }

    @Override
    public List<? extends ConsumableRuleTo> getConsumableRulesTo() {
        if (helperConsumableRuleToList != null) {
            return helperConsumableRuleToList;
        }
        List<ConsumableRuleTo> rulesTo = new ArrayList<>();
        for (Map<String, String> rule : consumableRules) {
            InternalConsumableRuleTo ruleTo = new InternalConsumableRuleTo(rule);
            rulesTo.add(ruleTo);
        }
        helperConsumableRuleToList = rulesTo;
        return rulesTo;
    }

    private class InternalConsumableRuleTo extends ConsumableRuleTo {

        public InternalConsumableRuleTo(Map<String, String> map) {
            super(map);
        }

        public InternalConsumableRuleTo(String name, String consumableName, LogicalOperator operator, int limitValue, String message) {
            super(name, consumableName, operator, limitValue, message);
        }

        @Override
        protected Map<String, String> toMap() {
            return super.toMap();
        }
    }
}
