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
import br.uff.labtempo.osiris.util.interfaces.Teste;
import br.uff.labtempo.osiris.util.logging.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Felipe
 */
public class ClientController extends Component implements Client {

    private DataManager data;

    public ClientController(DataManager data) {
        this.data = data;
    }

    public Teste createVSensor(String name) {
        System.out.println("<<<:::createVSensor=" + name);
        VirtualSensor vsensor = new VirtualSensor(name);
        data.save(vsensor);
        Teste teste = new Teste();
        teste.setName("teste");
        return teste;
    }

    public void bind(String vsensorId, String moteId) {
        System.out.println("<<<:::bind=" + vsensorId + ":" + moteId);
        Mote mote = data.get(Mote.class, moteId);

        if (mote == null || mote.hasSensor()) {
            return;
        }

        VirtualSensor vsensor = data.get(VirtualSensor.class, Long.parseLong(vsensorId));

        if (vsensor == null) {
            return;
        }

        vsensor.setMote(mote);
        mote.setSensor(vsensor);

        data.update(vsensor);
        data.update(mote);

    }

    public void unbind(String vsensorId) {
        System.out.println("<<<:::unbind=" + vsensorId);
        VirtualSensor vsensor = data.get(VirtualSensor.class, Long.parseLong(vsensorId));

        if (vsensor == null || vsensor.isFree()) {
            return;
        }

        Mote mote = vsensor.getMote();

        mote.setSensor(null);
        vsensor.setMote(null);

        data.update(vsensor);
        data.update(mote);
    }

    public Map<String, List<String>> getFreeItems() {
        System.out.println("<<<:::getFreeItems");

        //get builder
        CriteriaBuilder cb = data.getCriteriaBuilder();

        //builder start(mote)
        CriteriaQuery<String> c = cb.createQuery(String.class);
        Root<Mote> mote = c.from(Mote.class);
        Predicate predicate = cb.isNull(mote.<Long>get("sensor"));
        c.where(predicate);
        c.select(mote.<String>get("id"));
        List<String> motes = data.<String>getQuery(c);

        c.select(mote.<String>get("sensor"));

        //builder start(sensor)
        CriteriaQuery<Long> c2 = cb.createQuery(Long.class);
        Root<VirtualSensor> sensor = c2.from(VirtualSensor.class);
        Predicate predicate2 = cb.isNull(sensor.get("mote"));
        c2.select(sensor.<Long>get("id"));
        c2.where(predicate2);
        List<String> sensors = new ArrayList<>();

        for (Long s : data.<Long>getQuery(c2)) {
            sensors.add(String.valueOf(s));
        }

        //end
        Map<String, List<String>> map = new HashMap<>();
        map.put("Mote", motes);
        map.put("Sensor", sensors);
        return map;
    }

    public Map<String, List<String>> getBoundItems() {
        System.out.println("<<<:::getBoundItems");
        Map<String, List<String>> map = new HashMap<>();

        List<String> motes = new ArrayList<>();

        for (Mote item : data.getNotNull(Mote.class, "sensor")) {
            motes.add(item.getId());
        }

        map.put("Mote", motes);

        List<String> sensors = new ArrayList<>();

        for (VirtualSensor item : data.getNotNull(VirtualSensor.class, "mote")) {
            sensors.add(String.valueOf(item.getId()));
        }

        map.put("Sensor", sensors);

        return map;
    }

    public List<String> getSamples(String vsensorId) {
        System.out.println("<<<:::getSamples=" + vsensorId);
        List<String> samples = new ArrayList<>();

        VirtualSensor vsensor = data.get(VirtualSensor.class, Long.parseLong(vsensorId));

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
