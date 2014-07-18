/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.server.packets;

import br.uff.labtempo.omcp.common.OmcpMethods;
import java.net.URI;
import java.util.Calendar;

/**
 *
 * @author Felipe
 */
public class Request {

    private OmcpMethods method;
    private String version;
    private URI resource;

    private Calendar date;
    private URI host;

    private String content;

    public void setVersion(String version) {
        this.version = version;
    }

    public void setMethod(OmcpMethods method) {
        this.method = method;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public void setHost(URI host) {
        this.host = host;
    }

    public void setResource(URI resource) {
        this.resource = resource;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the method
     */
    public OmcpMethods getMethod() {
        return method;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @return the resource
     */
    public URI getResource() {
        return resource;
    }

    /**
     * @return the date
     */
    public Calendar getDate() {
        return date;
    }

    /**
     * @return the host
     */
    public URI getHost() {
        return host;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

}
