/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.labtempo.osiris.collector;

import br.uff.labtempo.osiris.collector.temp.Collector;
import br.uff.labtempo.osiris.collector.temp.Network;
import br.uff.labtempo.osiris.collector.temp.Sample;
import br.uff.labtempo.osiris.collector.temp.Sensor;
import com.google.gson.Gson;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Felipe
 */
public class SampleTest {
    private final Gson gson;
    private final DataBuilder data;

    public SampleTest() {
        gson = new Gson();
        data = new DataBuilder();
    }
    
    
    @Test
    public void serializationTest() {
        Network network = data.getNetwork();
        Collector collector = data.getCollector();
        Sensor sensor = data.getSensor();
        
        Sample sample =  new Sample(network, collector, sensor);
            
        String json = gson.toJson(sample);
        
        Sample newSample = gson.fromJson(json, sample.getClass());
        
        assertEquals(sample, newSample);
    }
    
}
