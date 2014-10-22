/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.model.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 *
 * @author Felipe
 */
@Entity
@Table(name = "SENSOR_CONSUMABLE")
public class Consumable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public void setRule(Rule rule) {
        rules.add(rule);
    }

}
