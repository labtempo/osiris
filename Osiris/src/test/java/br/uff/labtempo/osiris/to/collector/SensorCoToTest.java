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

import br.uff.labtempo.omcp.common.utils.Serializer;
import br.uff.labtempo.osiris.to.common.data.ConsumableRuleTo;
import br.uff.labtempo.osiris.to.common.data.ConsumableTo;
import br.uff.labtempo.osiris.to.common.definitions.LogicalOperator;
import br.uff.labtempo.osiris.to.common.data.ValueTo;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class SensorCoToTest {

    private final SensorCoTo sensorTo;

    public SensorCoToTest() {
        this.sensorTo = new SensorCoTo(1);

        sensorTo.addInfo("chave", "valor");
        sensorTo.addInfo("chave2", "valor2");

        sensorTo.addValue("temperature", 35.5, "celsius", "°C");
        sensorTo.addValue("luminosity", 200.0, "candela", "cd");
        sensorTo.addValue("battery", 50, "volt", "V");

        sensorTo.addConsumable("battery", 25);
        sensorTo.addConsumableRule("low battery", "battery", LogicalOperator.GREATER_THAN, 30, "Battery has low charge, you need change it!");
        
        sensorTo.addConsumable("fuel", 50);
        sensorTo.addConsumableRule("low fuel", "fuel", LogicalOperator.GREATER_THAN, 70, "Low fuel message!");
        
    }

    @Test
    public void testSerialization() {
        SensorCoTo newCoTo = serializer();
        assertEquals(sensorTo, newCoTo);
    }

    @Test
    public void testValues() {
        SensorCoTo newCoTo = serializer();
        List<? extends ValueTo> valueTos = newCoTo.getValuesTo();
        assertTrue(hasValue(valueTos, "temperature"));
        assertTrue(hasValue(valueTos, "luminosity"));
        assertTrue(hasValue(valueTos, "battery"));
    }

    @Test
    public void testConsumables() {
        SensorCoTo newCoTo = serializer();
        List<? extends ConsumableTo> consumableTos = newCoTo.getConsumablesTo();
        assertTrue(hasConsumable(consumableTos, "battery"));
        assertTrue(hasConsumable(consumableTos, "fuel"));
    }

    @Test
    public void testConsumableRules() {
        SensorCoTo newCoTo = serializer();
        List<? extends ConsumableRuleTo> consumableRuleTos = newCoTo.getConsumableRulesTo();
        assertTrue(hasConsumableRule(consumableRuleTos, "low fuel", "fuel"));
        assertTrue(hasConsumableRule(consumableRuleTos, "low battery", "battery"));
    }

    private boolean hasValue(List<? extends ValueTo> values, String name) {
        for (ValueTo value : values) {
            if (value.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasConsumable(List<? extends ConsumableTo> values, String name) {
        for (ConsumableTo value : values) {
            if (value.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasConsumableRule(List<? extends ConsumableRuleTo> rules, String ruleName, String consumableName) {
        for (ConsumableRuleTo rule : rules) {
            if (rule.getConsumableName().equals(consumableName) && rule.getName().equals(ruleName)) {
                return true;
            }
        }
        return false;
    }

    private SensorCoTo serializer() {
        Serializer serializer = new Serializer();
        String json = serializer.toJson(sensorTo);
        System.out.println(json);
        SensorCoTo newCoTo = serializer.fromJson(json, SensorCoTo.class);
        return newCoTo;
    }

}
