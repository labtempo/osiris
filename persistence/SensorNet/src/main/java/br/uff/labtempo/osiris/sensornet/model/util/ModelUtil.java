/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.model.util;

import br.uff.labtempo.osiris.collector.to.CollectorCoTo;
import br.uff.labtempo.osiris.collector.to.NetworkCoTo;
import br.uff.labtempo.osiris.collector.to.SensorCoTo;
import br.uff.labtempo.osiris.sensornet.model.jpa.Collector;
import br.uff.labtempo.osiris.sensornet.model.jpa.Consumable;
import br.uff.labtempo.osiris.sensornet.model.jpa.Network;
import br.uff.labtempo.osiris.sensornet.model.jpa.Rule;
import br.uff.labtempo.osiris.sensornet.model.jpa.Sensor;
import br.uff.labtempo.osiris.sensornet.model.jpa.Value;
import br.uff.labtempo.osiris.sensornet.to.CollectorSnTo;
import br.uff.labtempo.osiris.sensornet.to.NetworkSnTo;
import br.uff.labtempo.osiris.sensornet.to.SensorSnTo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public class ModelUtil {

    public SensorSnTo toTransferObject(Sensor sensor) {
        Map<String, Integer> consumables = new HashMap<>();
        for (Consumable consumable : sensor.getConsumables()) {
            consumables.put(consumable.getName(), consumable.getValue());
        }
        List<Map<String, String>> values = new ArrayList<>();
        for (Value value : sensor.getValues()) {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("name", value.getName());
            map.put("type", value.getType());
            map.put("value", value.getValue());
            map.put("unit", value.getUnit());
            map.put("symbol", value.getSymbol());
            values.add(map);
        }
        return new SensorSnTo(sensor.getId(), sensor.getLastModifiedDate().getTimeInMillis(), sensor.getState().toString(), sensor.getNetwork().getId(), sensor.getCollector().getId(), sensor.getTimestamp(), values, consumables, sensor.getInfo());
    }

    public Sensor fromTransferObject(SensorCoTo sensorTo) {
        return new Sensor(sensorTo.getId(), sensorTo.getTimestamp(), creatValueListFromMapList(sensorTo.getValues()), createConsumable(sensorTo.getConsumables(), sensorTo.getRules()), sensorTo.getInfo());
    }

    public boolean updateFromTransferObject(Sensor object, SensorCoTo to) {
        boolean isUpdated = false;
        if (object.getId().equals(to.getId())) {
            //update info
            if (to.getInfo() != null && !object.getInfo().equals(to.getInfo())) {
                object.setInfo(to.getInfo());
                isUpdated = true;
            }
            //update values
            if (to.getValues() != null) {
                object.setValues(creatValueListFromMapList(to.getValues()));
                isUpdated = true;
            }
            //update consumables
            if (updateSensorConsumables(object, to)) {
                isUpdated = true;
            }
            //check consumables
            checkConsumables(object.getConsumables());
        }
        return isUpdated;
    }

    public CollectorSnTo toTransferObject(Collector collector) {
        return new CollectorSnTo(collector.getId(), collector.getLastModifiedDate().getTimeInMillis(), collector.getState().toString(), collector.getNetwork().getId(), collector.getSensors().length, collector.getInfo());

    }

    public Collector fromTransferObject(CollectorCoTo collectorTo) {
        return new Collector(collectorTo.getId(), collectorTo.getInfo());
    }

    public boolean updateFromTransferObject(Collector object, CollectorCoTo to) {
        boolean isUpdated = false;
        if (object.getId().equals(to.getId())) {
            if (to.getInfo() != null && !object.getInfo().equals(to.getInfo())) {
                object.setInfo(to.getInfo());
                isUpdated = true;
            }
        }
        return isUpdated;
    }

    public NetworkSnTo toTransferObject(Network network) {
        return new NetworkSnTo(network.getId(), network.getLastModifiedDate().getTimeInMillis(), network.getState().toString(), network.getCollectors().length, network.getSensors().length, network.getInfo());
    }

    public Network fromTransferObject(NetworkCoTo networkTo) {
        return new Network(networkTo.getId(), networkTo.getInfo());
    }

    public boolean updateFromTransferObject(Network object, NetworkCoTo to) {
        boolean isUpdated = false;
        if (object.getId().equals(to.getId())) {
            if (to.getInfo() != null && !object.getInfo().equals(to.getInfo())) {
                object.setInfo(to.getInfo());
                isUpdated = true;
            }
        }

        return isUpdated;
    }

    private List<Value> creatValueListFromMapList(List<Map<String, String>> maplist) {
        List<Value> values = new ArrayList<>();

        for (Map<String, String> map : maplist) {
            if (map != null) {
                if (map.containsKey("name")
                        && map.containsKey("value")
                        && map.containsKey("type")
                        && map.containsKey("unit")
                        && map.containsKey("symbol")) {

                    values.add(new Value(
                            map.get("name"),
                            map.get("type"),
                            map.get("value"),
                            map.get("unit"),
                            map.get("symbol")));
                }
            }
        }
        return values;
    }

    public List<Consumable> createConsumable(Map<String, Integer> consumableMap, List<Map<String, String>> ruleMapList) {

        List<Consumable> consumables = new ArrayList<>();

        for (Map.Entry<String, Integer> entrySet : consumableMap.entrySet()) {
            String key = entrySet.getKey();
            Integer value = entrySet.getValue();
            Consumable consumable = new Consumable(key, value);

            for (Map<String, String> ruleMap : ruleMapList) {
                if (ruleMap.containsKey("consumable") && ruleMap.get("consumable").equals(key)) {
                    Rule rule = new Rule(
                            ruleMap.get("name"),
                            ruleMap.get("operator"),
                            ruleMap.get("value"),
                            ruleMap.get("message"));
                    consumable.addRule(rule);
                }
            }

            consumables.add(consumable);
        }
        return consumables;
    }

    private boolean updateSensorConsumables(Sensor object, SensorCoTo to) {
        boolean isUpdated = false;

        List<Consumable> current, newer, add, remove, update;

        current = object.getConsumables();
        newer = createConsumable(to.getConsumables(), to.getRules());

        add = new ArrayList<>(newer);
        add.removeAll(current);

        remove = new ArrayList<>(current);
        remove.removeAll(newer);

        update = new ArrayList<>(newer);
        update.removeAll(add);

        current.removeAll(remove);

        for (Consumable consumable : current) {
            if (update.contains(consumable)) {
                for (Consumable updatedConsumable : update) {
                    if (updatedConsumable.equals(consumable) && !updatedConsumable.equalsAll(consumable)) {
                        consumable.setValue(updatedConsumable.getValue());
                        consumable.setRules(updatedConsumable.getRules());
                        isUpdated = true;
                        break;
                    }
                }
            }
        }

        current.addAll(add);

        return isUpdated;
    }

    public List<String> checkConsumables(List<Consumable> consumables) {
        List<String> list = new ArrayList<>();
        for (Consumable consumable : consumables) {            
            int value = consumable.getValue();
            for (Rule rule : consumable.getRules()) {
                if (ruleTest(rule.getOperator(), value, rule.getValue())) {
                    list.add(rule.getName() + ":" + rule.getMessage());
                }
            }
        }
        return list;
    }

    private boolean ruleTest(String operator, int valueA, int valueB) {
        switch (operator) {
            //equal
            case "=":
            case "==":
                if (valueA == valueB) {
                    return true;
                }
                break;
            //not equal
            case "!=":
            case "<>":
                if (valueA != valueB) {
                    return true;
                }
                break;
            //greater than
            case ">":
                if (valueA > valueB) {
                    return true;
                }
                break;
            //less than
            case "<":
                if (valueA < valueB) {
                    return true;
                }
                break;

            //greater than or equal
            case ">=":
                if (valueA >= valueB) {
                    return true;
                }
                break;
            //less than or equal
            case "<=":
                if (valueA <= valueB) {
                    return true;
                }
                break;
        }
        return false;
    }

}
