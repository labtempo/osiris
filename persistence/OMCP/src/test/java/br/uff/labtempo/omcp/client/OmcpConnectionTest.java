/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.client;

import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.StatusCode;
import br.uff.labtempo.omcp.server.OmcpServer;
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
public class OmcpConnectionTest {

    private OmcpConnection connection;
    private OmcpServer server;
    private final String pwd;
    private final String usr;
    private final String ip;

    public OmcpConnectionTest() {
        ip = "192.168.0.7";
        usr = "admin";
        pwd = "admin";
        
        
    }
   
    @Before
    public void configure() {
        try {
            this.connection = new OmcpConnection(ip, usr, pwd);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            this.server = new OmcpServer("test", ip, usr, pwd);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Thread t =  new Thread(new Runnable() {
            @Override
            public void run() {
                server.start();
                System.out.println("Thread end!");
            }
        });
        
        t.setName("server");
        t.setDaemon(true);
        t.start();
    }

    
    
    public void closeAll() {
        connection.close();
        server.stop();
    }

    @Test
    public void doGet() {
        Response resp = connection.doGet("omcp://test/resouce");
        assertEquals(StatusCode.NOT_FOUND, resp.getStatusCode());
    }
    
    @Test
    public void doPost() {
        Response resp = connection.doPost("omcp://test/resouce","teste post");
        assertEquals(StatusCode.NOT_FOUND, resp.getStatusCode());
    }

}
