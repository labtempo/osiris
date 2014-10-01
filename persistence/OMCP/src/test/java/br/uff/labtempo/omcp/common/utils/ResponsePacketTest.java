/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.common.utils;

import br.uff.labtempo.omcp.common.Response;
import static br.uff.labtempo.omcp.common.StatusCode.*;
import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.omcp.common.exceptions.NotImplementedException;
import br.uff.labtempo.omcp.common.utils.DateUtil;
import br.uff.labtempo.omcp.common.utils.ResponseBuilder;
import br.uff.labtempo.omcp.common.utils.ResponsePacket;
import java.net.URI;
import java.net.URISyntaxException;
import static junit.framework.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Felipe
 */
public class ResponsePacketTest {

    private String host;
    private String module;
    private String version;
    private ResponsePacket rPacket;

    @Before
    public void setBuilder() {
        this.host = "omcp://test-module";
        this.module = "test-java/0.1";
        this.version = "OMCP/0.1";

        this.rPacket = new ResponsePacket();
    }

    @Test
    public void testOkPacket() throws URISyntaxException {
        System.out.println("ok response");
        String content = "request ok!";
        Response response = new ResponseBuilder().ok(content).build();

        response.setHost(host);
        response.setModule(module);
        response.setProtocolVersion(version);

        String packet = new StringBuilder()
                .append("OMCP/0.1 200 OK\n")
                .append("date:" + new DateUtil().generate(response.getDate()) + "\n")
                .append("module:" + module + "\n")
                .append("content-length:" + content.length() + "\n\n")
                .append(content + "\n")
                .toString();

        assertEquals(packet, rPacket.generate(response));
    }

    @Test
    public void testCreatedPacket() throws URISyntaxException {
        System.out.println("created response");
        String location = ("/sensors/3");
        Response response = new ResponseBuilder().created(location).build();

        response.setHost(host);
        response.setModule(module);
        response.setProtocolVersion(version);

        assertEquals(host + location, response.getLocation());
        String packet = new StringBuilder()
                .append("OMCP/0.1 201 CREATED\n")
                .append("date:" + new DateUtil().generate(response.getDate()) + "\n")
                .append("module:" + module + "\n")
                .append("location:" + host + location + "\n")
                .toString();

        assertEquals(packet, rPacket.generate(response));
    }

    @Test
    public void testNotFoundPacket() throws URISyntaxException {
        System.out.println("not found response");
        String errorMessage = "error";
        Response response = new ResponseBuilder().error(new NotImplementedException(errorMessage)).build();

        response.setHost(host);
        response.setModule(module);
        response.setProtocolVersion(version);

        String packet = new StringBuilder()
                .append("OMCP/0.1 501 NOT IMPLEMENTED\n")
                .append("date:" + new DateUtil().generate(response.getDate()) + "\n")
                .append("module:" + module + "\n")
                .append("error:" + errorMessage + "\n")
                .toString();

        assertEquals(packet, rPacket.generate(response));
    }

    @Test
    public void testServerErrorPacket() {
        System.out.println("server error response");
        String errorMessage = "nao foi possivel criar entrada";
        Response response = new ResponseBuilder().error(new InternalServerErrorException(errorMessage)).build();

        response.setHost(host);
        response.setModule(module);
        response.setProtocolVersion(version);

        String packet = new StringBuilder()
                .append("OMCP/0.1 500 INTERNAL SERVER ERROR\n")
                .append("date:" + new DateUtil().generate(response.getDate()) + "\n")
                .append("module:" + module + "\n")
                .append("error:" + errorMessage + "\n")
                .toString();

        assertEquals(packet, rPacket.generate(response));
    }
}
