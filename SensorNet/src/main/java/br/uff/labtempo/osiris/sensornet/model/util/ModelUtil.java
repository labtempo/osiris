/* 
 * Copyright 2015 Felipe Santos <fralph at ic.uff.br>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.uff.labtempo.osiris.sensornet.model.util;

import br.uff.labtempo.osiris.to.collector.CollectorCoTo;
import br.uff.labtempo.osiris.to.collector.NetworkCoTo;
import br.uff.labtempo.osiris.to.collector.SensorCoTo;
import br.uff.labtempo.osiris.sensornet.model.jpa.Collector;
import br.uff.labtempo.osiris.sensornet.model.jpa.Consumable;
import br.uff.labtempo.osiris.sensornet.model.jpa.Network;
import br.uff.labtempo.osiris.sensornet.model.jpa.Rule;
import br.uff.labtempo.osiris.sensornet.model.jpa.Sensor;
import br.uff.labtempo.osiris.sensornet.model.jpa.Value;
import br.uff.labtempo.osiris.to.common.data.ConsumableRuleTo;
import br.uff.labtempo.osiris.to.common.data.ConsumableTo;
import br.uff.labtempo.osiris.to.common.definitions.LogicalOperator;
import br.uff.labtempo.osiris.to.common.data.ValueTo;
import br.uff.labtempo.osiris.to.common.definitions.State;
import br.uff.labtempo.osiris.to.sensornet.CollectorSnTo;
import br.uff.labtempo.osiris.to.sensornet.NetworkSnTo;
import br.uff.labtempo.osiris.to.sensornet.SensorSnTo;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class ModelUtil {

    public SensorSnTo toTransferObject(Sensor sensor) {
        SensorSnTo sensorSnTo = new SensorSnTo(sensor.getId(), sensor.getModelState().getState(), sensor.getTimestamp(), sensor.getTimestampUnit(), sensor.getTimeOfCollectionInMillis(), sensor.getLastModifiedDate(), sensor.getNetwork().getId(), sensor.getCollector().getId());

        for (Consumable consumable : sensor.getConsumables()) {
            sensorSnTo.addConsumable(consumable.getName(), consumable.getValue());
        }

        for (Value value : sensor.getValues()) {
            sensorSnTo.addValue(value.getName(), value.getType(), value.getValue(), value.getUnit(), value.getSymbol());
        }

        sensorSnTo.addInfo(sensor.getInfo());

        return sensorSnTo;
    }

    public Sensor fromTransferObject(SensorCoTo sensorTo) {
        String id = sensorTo.getId();
        long timestamp = sensorTo.getTimestamp();
        TimeUnit timestampUnit = sensorTo.getTimestampUnit();
        long timeCollectionInMillis = sensorTo.getTimeOfCollectionInMillis();
        List<Value> values = creatValueListFromMapList(sensorTo.getValuesTo());
        List<Consumable> consumables = createConsumable(sensorTo.getConsumablesTo(), sensorTo.getConsumableRulesTo());
        Map<String, String> info = sensorTo.getInfo();

        return new Sensor(id, timestamp, timestampUnit, timeCollectionInMillis, values, consumables, info);
    }

    public boolean updateFromTransferObject(Sensor object, SensorCoTo to) {
        boolean isUpdated = false;
        //same id and greater capture time of sample 
        if (object.getId().equals(to.getId()) && object.getTimestamp() < to.getTimestamp()) {
            //update info
            if (to.getInfo() != null && !object.getInfo().equals(to.getInfo())) {
                object.setInfo(to.getInfo());
                isUpdated = true;
            }
            //update values
            if (to.getValuesTo() != null) {
                object.setValues(creatValueListFromMapList(to.getValuesTo()));
                object.setTimestamp(to.getTimestamp());
                object.setTimestampUnit(to.getTimestampUnit());
                object.setTimeOfCollectionInMillis(to.getTimeOfCollectionInMillis());
                isUpdated = true;
            }
            //update consumables
            if (updateSensorConsumables(object, to)) {
                isUpdated = true;
            }
        }
        return isUpdated;
    }

    public CollectorSnTo toTransferObject(Collector collector) {
        String id = collector.getId();
        State state = collector.getModelState().getState();
        long interval = collector.getInterval();
        TimeUnit timeUnit = collector.getTimeUnit();
        Calendar lastModified = collector.getLastModifiedDate();
        String networkId = collector.getNetwork().getId();
        int totalSensors = collector.getSensors().length;
        Map<String, String> info = collector.getInfo();

        CollectorSnTo collectorSnTo = new CollectorSnTo(id, state, interval, timeUnit, lastModified, networkId, totalSensors);
        collectorSnTo.addInfo(info);

        return collectorSnTo;

    }

    public Collector fromTransferObject(CollectorCoTo collectorTo) {
        return new Collector(collectorTo.getId(), collectorTo.getInterval(), collectorTo.getTimeUnit(), collectorTo.getInfo());
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
        String id = network.getId();
        State state = network.getModelState().getState();
        Calendar lastModified = network.getLastModifiedDate();
        int totalCollectors = network.getCollectors().length;
        int totalSensors = network.getSensors().length;
        Map<String, String> info = network.getInfo();

        NetworkSnTo networkSnTo = new NetworkSnTo(id, state, lastModified, totalCollectors, totalSensors);
        networkSnTo.addInfo(info);

        return networkSnTo;
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

    private List<Value> creatValueListFromMapList(List<? extends ValueTo> valuesTo) {
        List<Value> values = new ArrayList<>();

        for (ValueTo valueTo : valuesTo) {
            if (valueTo != null) {
                values.add(new Value(valueTo.getName(), valueTo.getType(), valueTo.getValue(), valueTo.getUnit(), valueTo.getSymbol()));
            }
        }

        return values;
    }

    public List<Consumable> createConsumable(List<? extends ConsumableTo> consumablesTo, List<? extends ConsumableRuleTo> consumableRulesTo) {

        List<Consumable> consumables = new ArrayList<>();

        for (ConsumableTo consumableTo : consumablesTo) {
            String name = consumableTo.getName();
            Integer value = consumableTo.getValue();
            Consumable consumable = new Consumable(name, value);

            for (ConsumableRuleTo consumableRuleTo : consumableRulesTo) {
                if (consumableRuleTo.getConsumableName().equals(name)) {
                    Rule rule = new Rule(consumableRuleTo.getName(), consumableRuleTo.getOperator(), consumableRuleTo.getLimitValue(), consumableRuleTo.getMessage());
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
        newer = createConsumable(to.getConsumablesTo(), to.getConsumableRulesTo());
        //TODO: revisar este algoritmo
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

    public List<ConsumableInfo> checkBrokenConsumables(List<Consumable> consumables) {
        List<ConsumableInfo> list = new ArrayList<>();
        for (Consumable consumable : consumables) {
            ConsumableInfo info = new ConsumableInfo(consumable);
            int value = consumable.getValue();
            for (Rule rule : consumable.getRules()) {
                if (testOfBreakingRule(rule.getOperator(), value, rule.getValue())) {
                    info.addBrokenRule(rule);
                }
            }
            if (info.isBroken()) {
                list.add(info);
            }
        }
        return list;
    }

    private boolean testOfBreakingRule(LogicalOperator operator, int valueA, int valueB) {
        switch (operator) {
            //equal
            case EQUAL:
                if (valueA == valueB) {
                    return true;
                }
                break;
            //not equal
            case NOT_EQUAL:
                if (valueA != valueB) {
                    return true;
                }
                break;
            //greater than
            case GREATER_THAN:
                if (valueA > valueB) {
                    return true;
                }
                break;
            //less than
            case LESS_THAN:
                if (valueA < valueB) {
                    return true;
                }
                break;

            //greater than or equal
            case GREATER_THAN_OR_EQUAL:
                if (valueA >= valueB) {
                    return true;
                }
                break;
            //less than or equal
            case LESS_THAN_OR_EQUAL:
                if (valueA <= valueB) {
                    return true;
                }
                break;
        }
        return false;
    }

}
