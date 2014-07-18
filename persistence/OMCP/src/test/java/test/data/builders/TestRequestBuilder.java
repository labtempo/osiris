/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.data.builders;

import br.uff.labtempo.omcp.server.packets.Request;
import br.uff.labtempo.omcp.server.packets.RequestBuilder;

/**
 *
 * @author Felipe
 */
public class TestRequestBuilder {

    private String resource = "/sources";
    private String version = "OMCP/0.1";
    private String dateHead = "date:";
    private String date = "Thu, 26 Jun 2014 19:56:39 GMT";
    private String hostHead = "host:";
    private String host = "sensornet";
    private String content = "message";
    private String method = "";

    private String lineEmpty = "\n\n";
    private String br = "\n";
    private String s = " ";

    private String packetWithNoDate() {
        return method + s + resource + s + version + br
                + hostHead + s + host + br
                + lineEmpty;
    }

    private String packetWithNoHost() {
        return method + s + resource + s + version + br
                + dateHead + s + date + br
                + lineEmpty;
    }

    private String packetWithNoContent() {
        return method + s + resource + s + version + br
                + dateHead + s + date + br
                + hostHead + s + host + br
                + lineEmpty;
    }

    private String packetWithContent() {
        return packetWithNoContent() + content + br;
    }

    public TestRequestBuilder method(String method) {
        this.method = method;
        return this;
    }

    public Request buildNoHost() {
        String message = packetWithNoHost();
        return new RequestBuilder().message(message).build();
    }

    public Request buildNoDate() {
        String message = packetWithNoDate();
        return new RequestBuilder().message(message).build();
    }

    public Request buildNoContent() {
        String message = packetWithNoContent();
        return new RequestBuilder().message(message).build();
    }

    public Request buildContent() {
        String message = packetWithContent();
        return new RequestBuilder().message(message).build();
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
