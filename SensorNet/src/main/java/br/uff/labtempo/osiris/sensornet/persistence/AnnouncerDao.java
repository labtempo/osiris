/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.persistence;

import br.uff.labtempo.osiris.sensornet.model.util.ConsumableInfo;
import br.uff.labtempo.osiris.to.sensornet.CollectorSnTo;
import br.uff.labtempo.osiris.to.sensornet.NetworkSnTo;
import br.uff.labtempo.osiris.to.sensornet.SensorSnTo;
import java.util.List;

/**
 *
 * @author Felipe
 */
public interface AnnouncerDao {

    void broadcastIt(SensorSnTo objTo);

    void broadcastIt(CollectorSnTo objTo);

    void broadcastIt(NetworkSnTo objTo);

    void notifyBrokenConsumableRule(List<ConsumableInfo> highlightedConsumables, SensorSnTo objTo);

    void notifyDeactivation(NetworkSnTo objTo);

    void notifyDeactivation(CollectorSnTo objTo);

    void notifyDeactivation(SensorSnTo objTo);

    void notifyMalfunction(SensorSnTo objTo);

    void notifyMalfunction(CollectorSnTo objTo);

    void notifyMalfunction(NetworkSnTo objTo);

    void notifyNew(NetworkSnTo objTo);

    void notifyNew(CollectorSnTo objTo);

    void notifyNew(SensorSnTo objTo);

    void notifyReactivation(NetworkSnTo objTo);

    void notifyReactivation(CollectorSnTo objTo);

    void notifyReactivation(SensorSnTo objTo);
}
