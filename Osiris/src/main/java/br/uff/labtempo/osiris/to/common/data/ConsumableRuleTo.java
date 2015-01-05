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
package br.uff.labtempo.osiris.to.common.data;

import br.uff.labtempo.osiris.to.common.definitions.LogicalOperator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
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
