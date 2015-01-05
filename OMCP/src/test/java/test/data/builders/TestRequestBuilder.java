/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.data.builders;

import br.uff.labtempo.omcp.common.exceptions.BadRequestException;
import br.uff.labtempo.omcp.common.exceptions.MethodNotAllowedException;
import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.common.utils.RequestPacket;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class TestRequestBuilder {

    private String resource = "/sources";
    private String version = "OMCP/0.1";
    private String dateHead = "date:";
    private String date = "Thu, 24 Jul 2014 15:40:03 GMT";
    private String hostHead = "module:";
    private String host = "sensornet";
    private String contentLenghtHead = "content-length:";
    private String content = "message";
    private int contentLength = 0;
    private String contentTypeHead = "content-type:";
    private String contentType = "text/plain";
    private String method = "";

    private String lineEmpty = "\n\n";
    private String br = "\n";
    private String s = " ";

    private String packetWithNoDate() {
        return method + s + resource + s + version + br
                + hostHead + host + br
                + lineEmpty;
    }

    private String packetWithNoHost() {
        return method + s + resource + s + version + br
                + dateHead + date + br
                + lineEmpty;
    }

    private String packetWithNoContent() {
        return method + s + resource + s + version + br
                + dateHead + date + br
                + hostHead + host + br
                + lineEmpty;
    }

    private String packetWithContent() {
        return method + s + resource + s + version + br
                + dateHead + date + br
                + hostHead + host + br
                + contentLenghtHead + contentLength + br                
                + contentTypeHead + contentType + br
                + lineEmpty + content + br;
    }

    public TestRequestBuilder method(String method) {
        this.method = method;
        return this;
    }

    public Request buildNoHost() throws BadRequestException, MethodNotAllowedException {
        String message = packetWithNoHost();
        return new RequestPacket().parse(message);
    }

    public Request buildNoDate() throws BadRequestException, MethodNotAllowedException {
        String message = packetWithNoDate();
        return new RequestPacket().parse(message);
    }

    public Request buildNoContent() throws BadRequestException, MethodNotAllowedException {
        String message = packetWithNoContent();
        return new RequestPacket().parse(message);
    }

    public Request buildContent() throws BadRequestException, MethodNotAllowedException {
        String message = packetWithContent();
        return new RequestPacket().parse(message);
    }

    public TestRequestBuilder date(String date) {
        this.date = date;
        return this;
    }

    public TestRequestBuilder host(String host) {
        this.host = host;
        return this;
    }

    public TestRequestBuilder resource(String resource) {
        this.resource = resource;
        return this;
    }

    public TestRequestBuilder version(String version) {
        this.version = version;
        return this;
    }

    public TestRequestBuilder content(String content) {
        this.content = content;
        return this;
    }
}
