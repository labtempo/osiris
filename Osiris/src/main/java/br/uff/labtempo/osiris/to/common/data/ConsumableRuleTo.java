/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.to.common.data;

import br.uff.labtempo.osiris.to.common.definitions.LogicalOperator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public abstract class ConsumableRuleTo {

        private final String name;
        private final String consumableName;
        private final LogicalOperator operator;
        private final int limitValue;
        private final String message;

        private final transient String NAME = "name";
        private final transient String OPERATOR = "operator";
        private final transient String CONSUMABLE_NAME = "consumableName";
        private final transient String LIMIT_VALUE = "limitValue";
        private final transient String MESSAGE = "message";

        protected ConsumableRuleTo(Map<String, String> map) {
            this.name = map.get(NAME);
            this.consumableName = map.get(CONSUMABLE_NAME);
            this.operator = LogicalOperator.getByString(map.get(OPERATOR));
            this.limitValue = Integer.valueOf(map.get(LIMIT_VALUE));
            this.message = map.get(MESSAGE);
        }

        protected ConsumableRuleTo(String name, String consumableName, LogicalOperator operator, int limitValue, String message) {
            this.name = name;
            this.consumableName = consumableName;
            this.operator = operator;
            this.limitValue = limitValue;
            this.message = message;
        }

        public String getName() {
            return name;
        }

        public String getConsumableName() {
            return consumableName;
        }

        public LogicalOperator getOperator() {
            return operator;
        }

        public int getLimitValue() {
            return limitValue;
        }

        public String getMessage() {
            return message;
        }

        protected Map<String, String> toMap() {
            Map<String, String> map = new LinkedHashMap<>();
            map.put(NAME, name);
            map.put(CONSUMABLE_NAME, consumableName);
            map.put(OPERATOR, operator.toString());
            map.put(LIMIT_VALUE, String.valueOf(limitValue));
            map.put(MESSAGE, message);
            return map;
        }
    }
