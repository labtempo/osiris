/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet;

import br.uff.labtempo.osiris.collector.to.CollectorCoTo;
import br.uff.labtempo.osiris.collector.to.NetworkCoTo;
import br.uff.labtempo.osiris.collector.to.SampleCoTo;
import br.uff.labtempo.osiris.collector.to.SensorCoTo;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author Felipe
 */
public class DataBuilder {

    private long index = 1;
    private final String networkId;
    private final String collectorId;
    private final int value;

    DataBuilder(String networkId, String collectorId) {
        Random gerador = new Random();

        this.value = gerador.nextInt(3);
        this.networkId = networkId;
        this.collectorId = collectorId;
    }

    public SampleCoTo generateSample() {
        SampleCoTo sample = new SampleCoTo(getNetwork(), getCollector(), getSensor());
        return sample;
    }

    SensorCoTo getSensor() {
        Map<String, String> info = new HashMap<>();
        info.put("chave", "valor");
        info.put("chave2", "valor2");

        List<Map<String, String>> _values = new ArrayList<>();

        Map<String, String> map = new HashMap<>();
        map.put("name", "temperature");
        map.put("value", String.valueOf(35.5 + value));
        map.put("type", "real");
        map.put("unit", "celsius");
        map.put("symbol", "°C");
        _values.add(map);
        map = new HashMap<>();
        map.put("name", "luminosity");
        map.put("value", String.valueOf(200.0 + value));
        map.put("type", "real");
        map.put("unit", "candela");
        map.put("symbol", "cd");
        _values.add(map);
        map = new HashMap<>();
        map.put("name", "battery");
        map.put("value", String.valueOf(50 + value));
        map.put("type", "integer");
        map.put("unit", "volt");
        map.put("symbol", "V");
        _values.add(map);

        Map<String, Integer> consumables = new HashMap<>();
        consumables.put("battery", 25 + value);
        consumables.put("fuel", 50 + value);

        List<Map<String, String>> _rules = new ArrayList<>();

        map = new HashMap<>();
        map.put("name", "low battery");
        map.put("operator", "<");
        map.put("consumable", "battery");
        map.put("value", "30");
        map.put("message", "Battery has low charge, you need change it!");
        _rules.add(map);
        map = new HashMap<>();
        map.put("name", "low fuel");
        map.put("operator", "<");
        map.put("consumable", "fuel");
        map.put("value", "70");
        map.put("message", "Low fuel message!");
        _rules.add(map);

        return new SensorCoTo("" + index++, Calendar.getInstance().getTimeInMillis(), consumables, _rules, _values, info);

    }

    NetworkCoTo getNetwork() {
        Map<String, String> info = new HashMap<>();
        info.put("domain", "br.uff.ic");
        info.put("type", "wireless");
        info.put("OS", "TinyOS");
        info.put("Topologia", "barra");
        return new NetworkCoTo(networkId, info);
    }

    CollectorCoTo getCollector() {
        Map<String, String> info = new HashMap<>();
        info.put("descricao", "sala do laboratorio");
        info.put("numero", "2");
        info.put("Topologia", "estrela");
        return new CollectorCoTo(collectorId, info);
    }
}
