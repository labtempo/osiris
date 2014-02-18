/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.util.components;

/**
 *
 * @author Felipe
 */
public class ComponentInitializationException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ComponentInitializationException() {
        super();
    }

    public ComponentInitializationException(String message) {
        super(message);
    }

    public ComponentInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ComponentInitializationException(Throwable cause) {
        super(cause);
    }
}
