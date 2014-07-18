/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.server.packets;

import static br.uff.labtempo.omcp.common.OmcpStatusCodes.*;
import java.net.URI;
import java.net.URISyntaxException;
import static junit.framework.Assert.assertEquals;
import org.junit.Test;


/**
 *
 * @author Felipe
 */
public class ResponseTest {

   @Test
    public void testOkPacket() throws URISyntaxException {
        System.out.println("ok response");

        String module = "Sensornet/Java-0.1";
        String content = "request ok!";
        Response response = new Response();
        response.setStatusCode(OK);
        response.setContent(content);
        response.setModule(module);

        String packet = new StringBuilder()
                .append("OMCP/0.1 200 OK\n")
                .append("date: " + response.getFormatedDate() + "\n")
                .append("module: " + module + "\n")
                .append("content-length: " + content.length() + "\n\n")
                .append(content + "\n")
                .toString();

        assertEquals(packet, response.toString());
    }

    @Test
    public void testCreatedPacket() throws URISyntaxException {
        System.out.println("created response");

        URI location = new URI("/sensors");
        URI origin = new URI("omcp://sensornet");

        String module = "Sensornet/Java-0.1";
        Response response = new Response();
        response.setStatusCode(CREATED);
        response.setLocation(location);
        response.setOrigin(origin);
        response.setModule(module);

        URI result = new URI("omcp://sensornet/sensors");

        assertEquals(result, response.getLocation());

        String packet = new StringBuilder()
                .append("OMCP/0.1 201 CREATED\n")
                .append("date: " + response.getFormatedDate() + "\n")
                .append("module: " + module + "\n")
                .append("location: " + result + "\n")
                .toString();

        assertEquals(packet, response.toString());
    }

    @Test
    public void testNotFoundPacket() throws URISyntaxException {
        System.out.println("not found response");
        String module = "Sensornet/Java-0.1";
        Response response = new Response();
        response.setStatusCode(NOT_IMPLEMENTED);
        response.setModule(module);

        String packet = new StringBuilder()
                .append("OMCP/0.1 501 NOT IMPLEMENTED\n")
                .append("date: " + response.getFormatedDate() + "\n")
                .append("module: " + module + "\n")
                .toString();

        assertEquals(packet, response.toString());
    }

    @Test
    public void testServerErrorPacket() {
        System.out.println("server error response");
        String module = "Sensornet/Java-0.1";
        String error = "nao foi possivel criar entrada";
        Response response = new Response();
        response.setStatusCode(INTERNAL_SERVER_ERROR);
        response.setError(error);
        response.setModule(module);

        String packet = new StringBuilder()
                .append("OMCP/0.1 500 INTERNAL SERVER ERROR\n")
                .append("date: " + response.getFormatedDate() + "\n")
                .append("module: " + module + "\n")
                .append("error: " + error + "\n")
                .toString();

        assertEquals(packet, response.toString());
    }
}
