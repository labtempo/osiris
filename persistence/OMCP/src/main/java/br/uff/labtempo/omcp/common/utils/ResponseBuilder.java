/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.common.utils;

import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.StatusCode;
import static br.uff.labtempo.omcp.common.StatusCode.*;
import br.uff.labtempo.omcp.common.exceptions.AbstractRequestException;
import java.util.Calendar;

/**
 *
 * @author Felipe
 */
public class ResponseBuilder {

    private static String protocolVersion;
    private static String moduleDescription;
    private static String host;

    {
        if (protocolVersion == null
                || moduleDescription == null
                || host == null) {
            throw new RuntimeException("Builder needs to be set up!");
        }
    }

    private StatusCode statusCode;
    private String content;
    private String location;
    private String error;
    private int contentLength;

    public ResponseBuilder ok(String content) {
        this.statusCode = OK;
        this.content = content;
        this.contentLength = content.length();
        return this;
    }

    public ResponseBuilder created(String uri) {
        this.statusCode = CREATED;
        this.location = host + uri;
        return this;
    }

    public ResponseBuilder error(AbstractRequestException ex) {
        this.statusCode = ex.getStatusCode();
        this.error = ex.getMessage();
        return this;
    }

    public Response build() {
        Response response = new Response(protocolVersion, statusCode, Calendar.getInstance(), moduleDescription, content, contentLength, location, error);
        return response;
    }

    public static void config(String host, String moduleDescription, String protocolVersion) {
        ResponseBuilder.host = host;
        ResponseBuilder.moduleDescription = moduleDescription;
        ResponseBuilder.protocolVersion = protocolVersion;
    }
}
