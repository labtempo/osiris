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
public class MalformedUriException extends AbstractClientRuntimeException {

    public MalformedUriException() {
        super();
    }

    public MalformedUriException(String message) {
        super(message);
    }

    public MalformedUriException(Throwable cause) {
        super(cause);
    }

    public MalformedUriException(String message, Throwable cause) {
        super(message, cause);
    }
}
