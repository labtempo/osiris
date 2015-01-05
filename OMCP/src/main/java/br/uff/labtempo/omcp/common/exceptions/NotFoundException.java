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
public class NotFoundException extends AbstractRequestException {

    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    @Override
    public StatusCode getStatusCode() {
        return StatusCode.NOT_FOUND;
    }
}
