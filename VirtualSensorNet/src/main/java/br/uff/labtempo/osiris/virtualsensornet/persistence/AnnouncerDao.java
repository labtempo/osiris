/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.virtualsensornet.persistence;

import br.uff.labtempo.osiris.to.virtualsensornet.VirtualSensorVsnTo;

/**
 *
 * @author Felipe
 */
public interface AnnouncerDao {

    void broadcastIt(VirtualSensorVsnTo objTo);

    void notifyDeactivation(VirtualSensorVsnTo objTo);

    void notifyMalfunction(VirtualSensorVsnTo objTo);

    void notifyNew(VirtualSensorVsnTo objTo);

    void notifyReactivation(VirtualSensorVsnTo objTo);
}
