/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.common.exceptions;

import br.uff.labtempo.omcp.common.StatusCode;

/**
 * Client Error Exception
 *
 * @author Felipe
 */
public class RequestTimeoutException extends AbstractRequestException {

    public RequestTimeoutException() {
        super();
    }

    public RequestTimeoutException(String message) {
        super(message);
    }

    public RequestTimeoutException(Throwable cause) {
        super(cause);
    }

    public RequestTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public StatusCode getStatusCode() {
        return StatusCode.REQUEST_TIMEOUT;
    }
}
