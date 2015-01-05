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
package br.uff.labtempo.osiris.collector;

import br.uff.labtempo.osiris.to.collector.CollectorCoTo;
import br.uff.labtempo.osiris.to.collector.NetworkCoTo;
import br.uff.labtempo.osiris.to.collector.SampleCoTo;
import br.uff.labtempo.osiris.to.collector.SensorCoTo;
import br.uff.labtempo.osiris.to.common.definitions.LogicalOperator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
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
        SensorCoTo sensorTo = new SensorCoTo(index++);

        sensorTo.addInfo("chave", "valor");
        sensorTo.addInfo("chave2", "valor2");

        sensorTo.addValue("temperature", 35.5 + value, "celsius", "°C");
        sensorTo.addValue("luminosity", 200.0 + value, "candela", "cd");
        sensorTo.addValue("battery", 50 + value, "volt", "V");

        sensorTo.addConsumable("battery", 25 + value);
        sensorTo.addConsumableRule("low battery", "battery", LogicalOperator.GREATER_THAN, 30, "Battery has low charge, you need change it!");

        sensorTo.addConsumable("fuel", 50 + value);
        sensorTo.addConsumableRule("low fuel", "fuel", LogicalOperator.GREATER_THAN, 70, "Low fuel message!");

        return sensorTo;
    }

    NetworkCoTo getNetwork() {
        NetworkCoTo networkCoTo = new NetworkCoTo(networkId);       
        networkCoTo.addInfo("domain", "br.uff.ic");
        networkCoTo.addInfo("type", "wireless");
        networkCoTo.addInfo("OS", "TinyOS");
        networkCoTo.addInfo("Topologia", "barra");
        return  networkCoTo;
    }

    CollectorCoTo getCollector() {
        CollectorCoTo collectorCoTo = new CollectorCoTo(collectorId, 2, TimeUnit.MINUTES);
        collectorCoTo.addInfo("descricao", "sala do laboratorio");
        collectorCoTo.addInfo("numero", "2");
        collectorCoTo.addInfo("Topologia", "estrela");
        return collectorCoTo;
    }
}
