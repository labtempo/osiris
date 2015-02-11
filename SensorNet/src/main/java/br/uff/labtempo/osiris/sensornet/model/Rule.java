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
package br.uff.labtempo.osiris.sensornet.model;

import br.uff.labtempo.osiris.to.common.definitions.LogicalOperator;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
@Embeddable
@Cacheable
public class Rule implements Serializable {

    private String name;
    @Enumerated(EnumType.STRING)
    private LogicalOperator operator;
    private int value;
    private String message;

    protected Rule() {
    }

    public Rule(String name, LogicalOperator operator, int value, String message) {
        this.name = name;
        this.operator = operator;
        this.value = value;
        this.message = message;
    }

    public Rule(String name, LogicalOperator operator, String value, String message) {
        this(name, operator, Integer.parseInt(value), message);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        Rule other = (Rule) obj;

        if (!name.equals(other.name)) {
            return false;
        }
        if (!operator.equals(other.operator)) {
            return false;
        }
        if (value != (other.value)) {
            return false;
        }
        if (!message.equals(other.message)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public String getName() {
        return name;
    }

    public LogicalOperator getOperator() {
        return operator;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
    
    
}
