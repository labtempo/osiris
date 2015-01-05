/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.labtempo.omcp.common.exceptions;

/**
 *
 * @author Felipe
 */
public class AbstractClientRuntimeException extends RuntimeException{
    public AbstractClientRuntimeException() {
        super();
    }

    public AbstractClientRuntimeException(String message) {
        super(message);
    }

    public AbstractClientRuntimeException(Throwable cause) {
        super(cause);
    }

    public AbstractClientRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
