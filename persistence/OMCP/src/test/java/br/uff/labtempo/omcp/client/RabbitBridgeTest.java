/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.labtempo.omcp.client;

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
public class RabbitBridgeTest {
    private RabbitBridge connection;
    
      
    @Before
    public void setUp() throws Exception{
        this.connection = new RabbitBridge("192.168.0.7", "admin", "admin");
    }
    
    @After
    public void tearDown() throws Exception{
        this.connection.close();
    }

    /**
     * Test of call method, of class RabbitBridge.
     */
    @Test
    public void testCall() throws Exception {
        this.connection.call("A", "teste");
    }
    
    @Test
    public void testPublish() throws Exception {
        this.connection.publish("A", "teste");
    }
    
    @Test(expected = RuntimeException.class)
    public void testCallToInexistentQueue() throws Exception {
        this.connection.call("xyz", "teste");        
    }
    
    @Test(expected = RuntimeException.class)
    public void testPublishToInexistentQueue() throws Exception {
        this.connection.publish("xyz", "teste");        
    }
}
