/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.server.packets;

import br.uff.labtempo.omcp.common.OmcpStatusCodes;
import static br.uff.labtempo.omcp.common.OmcpStatusCodes.*;
import java.net.URI;

/**
 *
 * @author Felipe
 */
public class ResponseBuilder {

    private OmcpStatusCodes statusCode;
    private String content;
    private URI location;
    private Exception error;

    public ResponseBuilder ok(String content) {
        this.statusCode = OK;
        this.content = content;
        return this;
    }

    public ResponseBuilder created(URI uri) {
        this.location = uri;
        this.statusCode = CREATED;
        return this;
    }

    public ResponseBuilder notImplemented() {
        this.statusCode = NOT_IMPLEMENTED;
        return this;
    }

    public ResponseBuilder serverError(Exception ex) {
        this.statusCode = INTERNAL_SERVER_ERROR;
        this.error = ex;
        return this;
    }

    public Response build() {
        Response response = new Response();
        response.setStatusCode(statusCode);
        switch (statusCode) {
            case OK:
                response.setContent(content);
                break;
            case CREATED:
                response.setLocation(location);
                break;
            case INTERNAL_SERVER_ERROR:
                response.setError(error.getMessage());
                break;
        }
        return response;
    }
}
