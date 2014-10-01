/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.common;

import br.uff.labtempo.omcp.common.StatusCode.*;
import java.util.Calendar;

/**
 *
 * @author Felipe
 */
public class Response {

    
    private final StatusCode statusCode;
    private final Calendar date;
    private final int contentLength;
    private final String location;
    private final String errorMessage;
    private final String content;
    
    private String protocolVersion;
    private String module;
    private String host;

    /**
     * Constructor - Read only object
     *
     * @param protocolVersion
     * @param statusCode
     * @param date
     * @param module
     * @param content
     * @param contentLength
     * @param location
     * @param errorMessage
     */
    public Response(String protocolVersion, StatusCode statusCode, Calendar date, String module, String content, int contentLength, String location, String errorMessage) {
        this.protocolVersion = protocolVersion;
        this.statusCode = statusCode;
        this.date = date;
        this.module = module;
        this.content = content;
        this.contentLength = contentLength;
        this.errorMessage = errorMessage;
        this.location = location;
    }

    public String getProtocolVersion() {
        return this.protocolVersion;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public Calendar getDate() {
        return this.date;
    }

    public String getModule() {
        return this.module;
    }

    public int getContentLength() {
        return this.contentLength;
    }

    public String getContent() {
        return this.content;
    }

    public String getLocation() {
        if (host != null) {
            return host + location;
        }
        return this.location;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public boolean isError() {
        return statusCode.isError();
    }

    public boolean hasContent() {
        return (contentLength > 0);
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
