/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.controller;

import br.uff.labtempo.osiris.data.DataManager;
import br.uff.labtempo.osiris.model.Mote;
import br.uff.labtempo.osiris.model.Sample;
import br.uff.labtempo.osiris.model.VirtualSensor;
import br.uff.labtempo.osiris.util.components.Component;
import br.uff.labtempo.osiris.util.components.ComponentInitializationException;
import br.uff.labtempo.osiris.util.interfaces.Client;
import br.uff.labtempo.osiris.util.logging.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public class ClientController extends Component implements Client {

    private DataManager data;

    public ClientController(DataManager data) {
        this.data = data;
    }

    public void createVSensor(String name) {
        System.out.println("<<<:::createVSensor="+name);
        VirtualSensor vsensor = new VirtualSensor(name);
        data.save(vsensor);
    }

    public void bind(String vsensorId, String moteId) {
        System.out.println("<<<:::bind="+vsensorId+":"+moteId);
        Mote mote = data.get(Mote.class, moteId);

        if (mote == null) {
            return;
        }

        VirtualSensor vsensor = data.get(VirtualSensor.class, vsensorId);

        if (vsensor == null) {
            return;
        }

        vsensor.setMote(mote);

        data.update(vsensor);

    }

    public void unbind(String vsensorId) {
        System.out.println("<<<:::unbind="+vsensorId);
        VirtualSensor vsensor = data.get(VirtualSensor.class, vsensorId);

        if (vsensor == null) {
            return;
        }

        vsensor.setMote(null);

        data.update(vsensor);
    }

    public Map<String, List<String>> getFreeItems() {
        System.out.println("<<<:::getFreeItems");
        return null;
    }

    public Map<String, List<String>> getBoundItems() {
        System.out.println("<<<:::getBoundItems");
        return null;
    }

    public List<String> getSamples(String vsensorId) {
        System.out.println("<<<:::getSamples="+vsensorId);
        List<String> samples = new ArrayList<>();

        VirtualSensor vsensor = data.get(VirtualSensor.class, vsensorId);

        if (vsensor == null) {
            return samples;
        }

        for (Sample sample : vsensor.getSamples()) {
            samples.add(sample.getSample());
        }

        return samples;
    }

    @Override
    protected void onCreate() throws ComponentInitializationException {
    }

    @Override
    protected void onStop() {
        Log.D("Closing ClientController");
    }
}
