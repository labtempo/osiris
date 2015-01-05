/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.virtualsensornet.thirdparty.announcer;

/**
 *
 * @author Felipe
 */
public enum AnnouncementView {

    VIRTUAL_SENSOR("VirtualSensor"),
    ITEM_REACTIVATED(" reactivate!"),
    ITEM_DISABLED(" disabled!"),
    ITEM_NEW(" discovered!"),
    ITEM_MALFUNCTION(" is working incorrect or partially!");

    private AnnouncementView(String content) {
        this.content = content;
    }

    private final String content;

    @Override
    public String toString() {
        return content;
    }

}
