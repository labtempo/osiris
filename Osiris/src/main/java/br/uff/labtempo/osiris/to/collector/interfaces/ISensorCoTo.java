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
package br.uff.labtempo.osiris.to.collector.interfaces;

import br.uff.labtempo.osiris.to.common.base.ISensorToBase;
import br.uff.labtempo.osiris.to.common.data.ConsumableRuleTo;
import br.uff.labtempo.osiris.to.common.definitions.LogicalOperator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public interface ISensorCoTo extends ISensorToBase {

    void addValue(String name, String value, String unit, String symbol);

    void addValue(String name, long value, String unit, String symbol);

    void addValue(String name, double value, String unit, String symbol);

    void addValue(String name, boolean value, String unit, String symbol);

    void addConsumable(String consumableName, int currentValue);

    void addConsumableRule(String name, String consumableName, LogicalOperator operator, int limitValue, String message);

    void addInfo(String infoKeyName, String infoDescription);

    void addInfo(Map<String, String> infos);

    List<? extends ConsumableRuleTo> getConsumableRulesTo();

    Map<String, String> getInfo();

    @Deprecated
    Map<String, Integer> getConsumables();

    @Deprecated
    List<Map<String, String>> getRules();

    @Deprecated
    List<Map<String, String>> getValues();

}
