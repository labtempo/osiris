/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.thirdparty.announcement.core;

/**
 *
 * @author Felipe
 */
class AnnouncementItem {

    private final Object object;
    private final String uri;

    AnnouncementItem(Object object, String uri) {
        this.object = object;
        this.uri = uri;
    }

    public Object getObject() {
        return object;
    }

    public String getUri() {
        return uri;
    }
}
