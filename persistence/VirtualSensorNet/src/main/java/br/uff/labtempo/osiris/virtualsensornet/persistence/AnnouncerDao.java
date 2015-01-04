/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.virtualsensornet.persistence;

import br.uff.labtempo.osiris.to.virtualsensornet.LinkVsnTo;

/**
 *
 * @author Felipe
 */
public interface AnnouncerDao {

    void broadcastIt(LinkVsnTo objTo);

    void notifyDeactivation(LinkVsnTo objTo);

    void notifyMalfunction(LinkVsnTo objTo);

    void notifyNew(LinkVsnTo objTo);

    void notifyReactivation(LinkVsnTo objTo);
}
