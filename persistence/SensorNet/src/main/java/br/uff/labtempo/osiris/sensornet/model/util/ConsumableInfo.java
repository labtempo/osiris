/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.model.util;

import br.uff.labtempo.osiris.sensornet.model.jpa.Consumable;
import br.uff.labtempo.osiris.sensornet.model.jpa.Rule;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Felipe
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
