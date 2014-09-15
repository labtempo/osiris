/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.server.router;

import br.uff.labtempo.omcp.common.RequestMethod;
import br.uff.labtempo.omcp.server.router.annotations.PathVariable;
import br.uff.labtempo.omcp.server.router.annotations.RequestMapping;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
public class OmcpRequestHandlerTest {

    public OmcpRequestHandlerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of createRegex method, of class OmcpRequestHandler.
     */
    @Test
    public void testCreateRegex() {
        System.out.println("createRegex");
        String value = "";
        OmcpRequestHandler instance = new OmcpRequestHandler();
        System.out.println(instance.createRegex("/teste/:id/teste"));
        System.out.println(instance.createRegex("/teste/:id/content/:contentid"));

        instance.call(this, "called", "/request/1254");
        instance.call(this, "called2", "/request/1254/content/5002asd5");
        instance.call(this, "called3", "/request/content/");
    }

    @RequestMapping(value = "/request/:id/", method = RequestMethod.GET)
    public void called(String id) {
        System.out.println("\n\n---:::chamado:::---");
        System.out.println("--- " + id + " ---\n\n");
    }

    @RequestMapping(value = "/request/:id/content/:content", method = RequestMethod.GET)
    public void called2(String id, String content) {
        System.out.println("\n\n---:::chamado:::---");
        System.out.println("--- " + id + " : " + content + " ---\n\n");
    }

    @RequestMapping(value = "/request/content", method = RequestMethod.GET)
    public void called3() {
        System.out.println("\n\n---:::chamado:::---");
        System.out.println("--- sem argumento ---\n\n");
    }

}
