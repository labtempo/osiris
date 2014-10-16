/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.collector;

import com.google.gson.Gson;
import java.util.Calendar;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Felipe
 */
public class SensorTest {

    private final Gson gson;

    public SensorTest() {
        gson = new Gson();
    }

    @Test
    public void serialzationTest() {
        System.out.println("serialzationTest");
        Sensor sensor = new Sensor(5, Calendar.getInstance().getTimeInMillis());
        Info info = new Info();

        info.add("chave", "valor");

//        sensor.setInfo(info);

        info.add("chave2", "valor2");

        sensor.addValue("temperature", 35.5, "celsius", "°C");
        sensor.addValue("luminosity", 200.0, "candela", "cd");        
        sensor.addValue("battery", 50, "volt", "V");
        

        Rule r1 = new Rule("battery");
        r1.add("low", 30);
        r1.add("full", 100);

        Rule r2 = new Rule("interval");
        r2.add("amount", 30);
        r2.add("unit", "seconds");

//        sensor.addRule(r1);
//        sensor.addRule(r2);

        String json = gson.toJson(sensor);

        Sensor result = gson.fromJson(json, sensor.getClass());

        boolean r = result.equals(sensor);

        assertEquals(sensor, (result));

    }

}
