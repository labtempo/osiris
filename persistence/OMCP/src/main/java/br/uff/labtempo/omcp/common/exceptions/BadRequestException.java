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
public class BadRequestException extends AbstractRequestException {

    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public StatusCode getStatusCode() {
        return StatusCode.BAD_REQUEST;
    }
}
