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
package br.uff.labtempo.omcp.common.utils;

import br.uff.labtempo.omcp.common.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
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
