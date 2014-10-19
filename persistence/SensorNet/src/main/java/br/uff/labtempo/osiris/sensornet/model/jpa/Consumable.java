/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.model.jpa;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

/**
 *
 * @author Felipe
 */
@Entity
public class Consumable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private int value;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("name ASC")
    private List<Rule> rules;

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public List<Rule> getRules() {
        return rules;
    }
    
    
}
