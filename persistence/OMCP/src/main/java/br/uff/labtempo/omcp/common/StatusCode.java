/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.common;

/**
 *
 * @author Felipe
 */
public enum StatusCode {

    /*
     * Inspired from HTTP Status Code:
     * 
     * 1xx: Informational - Request received, continuing process 
     * 2xx: Success - The action was successfully received, understood, and accepted
     * 3xx: Redirection - Further action must be taken in order to complete the request
     * 4xx: Client Error - The request contains bad syntax or cannot be fulfilled
     * 5xx: Server Error - The server failed to fulfill an apparently valid request
     *
     */
    /* Success */
    /**
     * Code to a succeed request<br>Value 200
     */
    OK(200),
    /**
     * Code to a succeed request to create<br>Value 501
     */
    CREATED(201),
    /*Client errors*/
    /**
     * Code to an erroneous client request<br>Value 400
     */
    BAD_REQUEST(400),
    /**
     * Code to a forbidden request of client<br>Value 403
     */
    FORBIDDEN(403),
    /**
     * Code to a client request to a non existent resource<br>Value 404
     */
    NOT_FOUND(404),
    /**
     * Code to a client request with a method not allowed<br>Value 405
     */
    METHOD_NOT_ALLOWED(405),
    /**
     * Code to a client request timeout<br>Value 408
     */
    REQUEST_TIMEOUT(408),
    /*Server errors*/
    /**
     * Code to a server internal error<br>Value 500
     */
    INTERNAL_SERVER_ERROR(500),
    /**
     * Code to request that is not implemented in server<br>Value 501
     */
    NOT_IMPLEMENTED(501);

    private boolean error;
    private final int code;

    private StatusCode(int code) {
        this.code = code;

        if (this.code >= 400) {
            this.error = true;
        }
    }

    public int toCode() {
        return code;
    }

    public boolean isError() {
        return error;
    }

    @Override
    public String toString() {
        return super.toString().replace("_", " ");
    }

    public static StatusCode getByCode(int code) {
        for (StatusCode statusCode : values()) {
            if (statusCode.code == code) {
                return statusCode;
            }
        }
        throw new IllegalArgumentException("No enum constant " + StatusCode.class.getName());
    }

}
