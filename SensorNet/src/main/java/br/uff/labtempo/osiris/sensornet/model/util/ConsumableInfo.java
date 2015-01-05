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
package br.uff.labtempo.osiris.sensornet.model.util;

import br.uff.labtempo.osiris.sensornet.model.jpa.Consumable;
import br.uff.labtempo.osiris.sensornet.model.jpa.Rule;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class ConsumableInfo {

    private Consumable consumable;
    private List<Rule> brokenRules;

    ConsumableInfo(Consumable consumable) {
        this.consumable = consumable;
    }

    public boolean isBroken() {
        if (brokenRules == null) {
            return false;
        }
        return true;
    }

    public Consumable getConsumable() {
        return consumable;
    }

    public List<Rule> getBrokenRules() {
        return brokenRules;
    }

    public void addBrokenRule(Rule rule) {
        if (brokenRules == null) {
            brokenRules = new ArrayList<>();
        }
        brokenRules.add(rule);
    }

}
