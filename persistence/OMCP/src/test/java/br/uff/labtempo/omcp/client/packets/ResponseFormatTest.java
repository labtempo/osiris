/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author Felipe
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
        ResponseBuilder.config(host, module, version);
        
    }
    
    @Before
    public void setUp() {
        this.format = new ResponsePacket();
    }
    
    @Test
    public void testCreatedResponse(){
        String content = "test";
        Response response = new ResponseBuilder().ok(content).build();
        
        String p = format.generate(response);        
        Response packet = format.parse(p);        
        assertEquals(false, packet.isError());
    }
    
}
