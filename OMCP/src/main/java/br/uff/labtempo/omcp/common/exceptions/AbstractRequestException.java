/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.common.exceptions;

import br.uff.labtempo.omcp.common.StatusCode;

/**
 * Abstract Exception
 *
 * @author Felipe
 */
public abstract class AbstractRequestException extends Exception {

    public AbstractRequestException() {
        super();
    }

    public AbstractRequestException(String message) {
        super(message);
    }

    public AbstractRequestException(Throwable cause) {
        super(cause);
    }

    public AbstractRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract StatusCode getStatusCode();
}
