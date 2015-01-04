/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.model.util;

import br.uff.labtempo.osiris.to.collector.CollectorCoTo;
import br.uff.labtempo.osiris.to.collector.NetworkCoTo;
import br.uff.labtempo.osiris.to.collector.SensorCoTo;
import br.uff.labtempo.osiris.to.common.definitions.LogicalOperator;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

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

        SensorCoTo sensorTo = new SensorCoTo(id++);

        sensorTo.addInfo("chave", "valor");
        sensorTo.addInfo("chave2", "valor2");

        sensorTo.addValue("temperature", 35.5, "celsius", "°C");
        sensorTo.addValue("luminosity", 200.0, "candela", "cd");
        sensorTo.addValue("battery", 50, "volt", "V");

        sensorTo.addConsumable("battery", 25);
        sensorTo.addConsumableRule("low battery", "battery", LogicalOperator.GREATER_THAN, 30, "Battery has low charge, you need change it!");

        sensorTo.addConsumable("fuel", 50);
        sensorTo.addConsumableRule("low fuel", "fuel", LogicalOperator.GREATER_THAN, 70, "Low fuel message!");

        return sensorTo;

    }
    
    NetworkCoTo getNetwork() {
        NetworkCoTo networkCoTo = new NetworkCoTo("labtempo");       
        networkCoTo.addInfo("domain", "br.uff.ic");
        networkCoTo.addInfo("type", "wireless");
        networkCoTo.addInfo("OS", "TinyOS");
        networkCoTo.addInfo("Topologia", "barra");        
        networkCoTo.addInfo("SecNumber", token);
        return  networkCoTo;
    }

    CollectorCoTo getCollector() {
        CollectorCoTo collectorCoTo = new CollectorCoTo("lab", 2, TimeUnit.MINUTES);
        collectorCoTo.addInfo("descricao", "sala do laboratorio");
        collectorCoTo.addInfo("numero", "2");
        collectorCoTo.addInfo("Topologia", "estrela");
        collectorCoTo.addInfo("SecNumber", token);
        return collectorCoTo;
    }
}
