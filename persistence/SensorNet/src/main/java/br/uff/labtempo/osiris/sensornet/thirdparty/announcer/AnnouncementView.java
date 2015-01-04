/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.thirdparty.announcer;

/**
 *
 * @author Felipe
 */
public enum AnnouncementView {

    NETWORK("Network"),
    SENSOR("Sensor"),
    COLLECTOR("Collector"),
    ITEM_REACTIVATED(" reactivate!"),
    ITEM_DISABLED(" disabled!"),
    ITEM_NEW(" discovered!"),
    ITEM_MALFUNCTION(" is working incorrect or partially!"),
    BROKEN_CONSUMABLE_RULE("Consumable broken some rules"),
    CONSUMABLES_BROKEN("Consumables broken: "),
    RULES_BROKEN("Rules broken: ");

    private AnnouncementView(String content) {
        this.content = content;
    }

    private final String content;

    @Override
    public String toString() {
        return content;
    }

}
