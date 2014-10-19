/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.common.utils;

import br.uff.labtempo.omcp.common.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Felipe
 */
public class SerializerTest {

    private final Serializer serializer;
    private Map<String, String> map;

    public SerializerTest() {
        this.serializer = new Serializer();
        this.map = new HashMap<>();
        
        map.put("nome", "felipe");
        map.put("felipe", "name");
        
    }

    @Test
    public void testXml() {    
        
        String xml = serializer.toXml(map);

        Map<String, String> newResponse = serializer.<Map<String, String>>fromXml(xml, map.getClass());

        assertEquals(map, newResponse);

    }
    
    @Test
    public void testJson() {    
        
        String xml = serializer.toJson(map);

        Map<String, String> newResponse = serializer.<Map<String, String>>fromJson(xml, map.getClass());

        assertEquals(map, newResponse);

    }
    
    @Test
    public void testContentTypeXml() {    
        String ct = "application/xml";
        String xml = serializer.serialize(map,ct);

        Map<String, String> newResponse = serializer.<Map<String, String>>deserialize(xml, ct, map.getClass());

        assertEquals(map, newResponse);

    }
    
    @Test
    public void testContentTypeJson() {    
        String ct = "application/json";
        String xml = serializer.serialize(map,ct);

        Map<String, String> newResponse = serializer.<Map<String, String>>deserialize(xml, ct, map.getClass());

        assertEquals(map, newResponse);

    }


}
