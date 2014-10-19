/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.collector;

import br.uff.labtempo.osiris.collector.temp.Rule;
import br.uff.labtempo.osiris.collector.temp.Collector;
import br.uff.labtempo.osiris.collector.temp.Network;
import br.uff.labtempo.osiris.collector.temp.Sensor;
import br.uff.labtempo.osiris.collector.temp.Info;
import java.util.Calendar;

/**
 *
 * @author Felipe
 */
public class DataBuilder {

    Sensor getSensor() {
        Sensor sensor = new Sensor(5, Calendar.getInstance().getTimeInMillis());
        Info info = new Info();
        info.add("chave", "valor");
        info.add("chave2", "valor2");

        sensor.setInfo(info);

        sensor.addValue("temperature", 35.5, "celsius", "°C");
        sensor.addValue("luminosity", 200.0, "candela", "cd");
        sensor.addValue("battery", 50, "volt", "V");

        Rule r1 = new Rule("battery");
        r1.add("low", 30);
        r1.add("full", 100);

        Rule r2 = new Rule("interval");
        r2.add("amount", 30);
        r2.add("unit", "seconds");

        sensor.addRule(r1);
        sensor.addRule(r2);

        return sensor;

    }

    Network getNetwork() {
        Network network = new Network("labtempo");
        Info info = new Info();
        info.add("domain", "br.uff.ic");
        info.add("type", "wireless");
        info.add("OS", "TinyOS");

        network.setInfo(info);

        return network;
    }

    Collector getCollector() {
        Collector collector = new Collector("lab");
        Info info = new Info();
        info.add("chave", "valor");
        info.add("chave2", "valor2");

        collector.setInfo(info);

        return collector;
    }
}
