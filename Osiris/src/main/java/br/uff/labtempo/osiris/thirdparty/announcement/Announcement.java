/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.thirdparty.announcement;

/**
 *
 * @author Felipe
 */
public interface Announcement extends AutoCloseable {

    Announcer getAnnouncer();

    void initialize();

}
