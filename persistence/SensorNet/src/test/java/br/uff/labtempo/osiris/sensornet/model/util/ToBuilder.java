/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.model.util;

import br.uff.labtempo.osiris.collector.to.CollectorCoTo;
import br.uff.labtempo.osiris.collector.to.NetworkCoTo;
import br.uff.labtempo.osiris.collector.to.SensorCoTo;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public class ToBuilder {

    private SecureRandom random = new SecureRandom();
    private int id = 1;
    private String token;

    public ToBuilder() {
        token = new BigInteger(130, random).toString(32);
    }

    SensorCoTo getSensor() {

        Map<String, String> info = new LinkedHashMap<>();
        info.put("chave", "valor");
        info.put("chave2", "valor2");
        info.put("SecNumber", token);

        Map<String, Integer> consumables = new LinkedHashMap<>();
        consumables.put("battery", 68);
        consumables.put("fuel", 89);

        List<Map<String, String>> rules = new ArrayList<>();
        Map<String, String> rule = new LinkedHashMap<>();
        rules.add(rule);
        rule.put("consumable", "battery");
        rule.put("name", "battery alert");
        rule.put("message", "message here");
        rule.put("value", "60");
        rule.put("operator", "<");

        List<Map<String, String>> values = new ArrayList<>();
        Map<String, String> value = new LinkedHashMap<>();
        values.add(value);
        value.put("name", "temperature");
        value.put("type", "real");
        value.put("value", "35.5");
        value.put("unit", "celsius");
        value.put("symbol", "°C");

        value = new LinkedHashMap<>();
        values.add(value);
        value.put("name", "luminosity");
        value.put("type", "real");
        value.put("value", "200.0");
        value.put("unit", "candela");
        value.put("symbol", "cd");

        SensorCoTo sensor = new SensorCoTo("" + id++, 1234356654, consumables, rules, values, info);
        return sensor;

    }

    NetworkCoTo getNetwork() {
        Map<String, String> info = new LinkedHashMap<>();
        info.put("domain", "br.uff.ic");
        info.put("type", "wireless");
        info.put("OS", "TinyOS");
        info.put("SecNumber", token);

        NetworkCoTo network = new NetworkCoTo("labtempo", info);
        return network;
    }

    CollectorCoTo getCollector() {
        Map<String, String> info = new LinkedHashMap<>();
        info.put("chave", "valor");
        info.put("chave2", "valor2");        
        info.put("SecNumber", token);

        CollectorCoTo collector = new CollectorCoTo("lab", info);

        return collector;
    }
}
