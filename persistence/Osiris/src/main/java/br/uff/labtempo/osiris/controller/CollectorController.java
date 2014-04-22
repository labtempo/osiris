/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.controller;

import br.uff.labtempo.osiris.controller.exceptions.NewMoteException;
import br.uff.labtempo.osiris.data.DataManager;
import br.uff.labtempo.osiris.model.Mote;
import br.uff.labtempo.osiris.model.Sample;
import br.uff.labtempo.osiris.model.VirtualSensor;
import br.uff.labtempo.osiris.util.components.Component;
import br.uff.labtempo.osiris.util.components.ComponentInitializationException;
import br.uff.labtempo.osiris.util.data.DataPacket;
import br.uff.labtempo.osiris.util.logging.Log;
import java.util.Date;

/**
 *
 * @author Felipe
 */
public class CollectorController extends Component {

    private DataManager data;

    public CollectorController(DataManager data) {
        this.data = data;
    }

    public void updateMote(DataPacket packet) throws NewMoteException {
        Mote mote = data.get(Mote.class, packet.getId());

        if (mote != null) {
            mote.setLastSample(packet.getData());
            mote.setLastModified(packet.getTimestamp());
            data.update(mote);

            if (mote.hasSensor()) {
                VirtualSensor vsensor = mote.getSensor();
                Sample sample = new Sample(vsensor, mote, packet.getData(), new Date(packet.getTimestamp()));
                data.save(sample);
            }

        } else {
            mote = new Mote(packet.getId(), packet.getData());
            data.save(mote);
            throw new NewMoteException("New mote was found");
        }
    }

    @Override
    protected void onCreate() throws ComponentInitializationException {
    }

    @Override
    protected void onStop() {
        Log.D("Closing CollectorController");
    }
}
