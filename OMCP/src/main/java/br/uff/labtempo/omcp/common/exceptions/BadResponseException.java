/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.common.exceptions;

/**
 * Exception with the role to identify a erroneous response packet from a
 * server.
 *
 * @author Felipe
 */
public class BadResponseException extends AbstractClientRuntimeException {

    public BadResponseException() {
        super();
    }

    public BadResponseException(String message) {
        super(message);
    }

    public BadResponseException(Throwable cause) {
        super(cause);
    }

    public BadResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}
