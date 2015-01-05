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
package br.uff.labtempo.osiris.to.common.definitions;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public enum LogicalOperator {

    EQUAL("=="),
    NOT_EQUAL("!="),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    GREATER_THAN_OR_EQUAL(">="),
    LESS_THAN_OR_EQUAL("<=");

    private final String operator;

    private LogicalOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return operator;
    }
    
    public static LogicalOperator getByString(String operator) {
        for (LogicalOperator logicalOperator : values()) {
            if (logicalOperator.operator.equalsIgnoreCase(operator)) {
                return logicalOperator;
            }
        }
        throw new IllegalArgumentException("No enum constant " + LogicalOperator.class.getName());
    }

}
