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
 * @author Felipe Santos <fralph at ic.uff.br>
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
                .append("content-length:" + content.length() + "\n")
                .append("content-type:" + "text/plain" + "\n\n")
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
