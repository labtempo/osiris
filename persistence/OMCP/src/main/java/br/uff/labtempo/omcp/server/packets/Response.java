/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.server.packets;

import br.uff.labtempo.omcp.common.OmcpStatusCodes;
import br.uff.labtempo.omcp.common.OmcpStatusCodes.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe
 */
public class Response {

    private final static String VERSION = "OMCP/0.1";
    private OmcpStatusCodes statusCode;
    private String content;
    private int contentLength;
    private URI origin;
    private URI location;
    private String module;
    private String error;
    private Calendar date;

    public Response() {
        this.date = Calendar.getInstance();
    }

    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(OmcpStatusCodes statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.contentLength = content.length();
        this.content = content;
    }

    /**
     * @param origin the origin to set
     */
    public void setOrigin(URI origin) {
        this.origin = origin;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(URI location) {
        this.location = location;
    }

    /**
     * @param module the module to set
     */
    public void setModule(String module) {
        this.module = module;
    }

    @Override
    public String toString() {
        String newline = "\n";
        StringBuilder sb = new StringBuilder();
        sb.append(VERSION + " ")
                .append(statusCode.toCode())
                .append(" ")
                .append(statusCode.toString())
                .append(newline)
                .append("date: ")
                .append(this.getFormatedDate())
                .append(newline)
                .append("module: ")
                .append(module)
                .append(newline);

        switch (statusCode) {
            case OK:
                sb.append("content-length: ")
                        .append(contentLength)
                        .append(newline)
                        .append(newline)
                        .append(content)
                        .append(newline);
                break;
            case CREATED:
                sb.append("location: ")
                        .append(origin)
                        .append(location.toString())
                        .append(newline);
                break;
            case INTERNAL_SERVER_ERROR:
                sb.append("error: ")
                        .append(error)
                        .append(newline);
                break;
        }
        return sb.toString();
    }

    public void setError(String message) {
        this.error = message;
    }

    /**
     * @return the statusCode
     */
    public OmcpStatusCodes getStatusCode() {
        return statusCode;
    }

    public String getErrorMessage() {
        return error;
    }

    public URI getLocation() {
        if (origin != null) {
            try {
                return new URI(origin.toString() + location.toString());
            } catch (URISyntaxException ex) {
                Logger.getLogger(Response.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return location;
    }

    public String getContent() {
        return content;
    }

    public int getContentLength() {
        return contentLength;
    }

    public String getFormatedDate() {
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        return format.format(this.date.getTime());
    }
}
