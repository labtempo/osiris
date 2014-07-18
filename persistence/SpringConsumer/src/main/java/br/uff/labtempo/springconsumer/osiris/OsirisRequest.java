/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.springconsumer.osiris;

import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Felipe
 */
public class OsirisRequest {

    private String method;
    private String resource;
    private String protocol;
    private Map<String, String> headers;
    private String content;

    public OsirisRequest(String method, String resource, String protocol, Map<String, String> headers, String content) {
        this.method = method;
        this.resource = resource;
        this.protocol = protocol;
        this.headers = headers;
        this.content = content;
    }

    /**
     * @return the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * @return the resource
     */
    public String getResource() {
        return resource;
    }

    /**
     * @return the protocol
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * @return the headers
     */
    public String getHeaders() {
        StringBuilder sb = new StringBuilder();

        for (Entry<String, String> entry : headers.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append(";");
        }

        return sb.toString();
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        String separator = System.getProperty("line.separator");
        return new StringBuilder()
                .append("Method: " + method)
                .append(separator)
                .append("Resource: " + resource)
                .append(separator)
                .append("Protocol: " + protocol)
                .append(separator)
                .append("Headers: " + getHeaders())
                .append(separator)
                .append("Content: " + content)
                .toString();

    }

}
