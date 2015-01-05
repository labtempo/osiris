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
package br.uff.labtempo.osiris.sensornet.model.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
@Entity
@Table(name = "SENSOR_CONSUMABLE")
public class Consumable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private int val;

    //@ElementCollection
//    @OrderBy("name ASC")
    @ElementCollection
    private List<Rule> rules;

    protected Consumable() {
    }

    public Consumable(String name, Integer value) {
        this.name = name;
        this.val = value;
        this.rules = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return val;
    }

    public List<Rule> getRules() {
        return rules;
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

        Consumable other = (Consumable) obj;

        if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    public boolean equalsAll(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        Consumable other = (Consumable) obj;

        if (!name.equals(other.name)) {
            return false;
        }

        if (val != (other.val)) {
            return false;
        }
        if (!rules.equals(other.rules)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public void addRule(Rule rule) {
        rules.add(rule);
    }

    public void setValue(int value) {
        this.val = value;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

}
