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
package br.uff.labtempo.omcp.client.packets;

import br.uff.labtempo.omcp.common.utils.ResponsePacket;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.utils.ResponseBuilder;
import static junit.framework.Assert.assertEquals;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static br.uff.labtempo.omcp.common.StatusCode.*;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class ResponseFormatTest {
    private ResponsePacket format;
    
    
    private String host;
    private String module;
    private String version;

    @Before
    public void setBuilder() {
        this.host = "omcp://test-module";
        this.module = "test-java/0.1";
        this.version = "OMCP/0.1";
        
    }
    
    @Before
    public void setUp() {
        this.format = new ResponsePacket();
    }
    
    @Test
    public void testCreatedResponse(){
        String content = "test";
        Response response = new ResponseBuilder().ok(content).build();
        
        response.setHost(host);
        response.setModule(module);
        response.setProtocolVersion(version);
        
        String p = format.generate(response);        
        Response packet = format.parse(p);        
        assertEquals(false, packet.isError());
    }
    
}
