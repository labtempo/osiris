/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.controller.exceptions;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class NewMoteException extends Exception {

    private static final long serialVersionUID = 1L;

    public NewMoteException() {
        super();
    }

    public NewMoteException(String message) {
        super(message);
    }

    public NewMoteException(String message, Throwable cause) {
        super(message, cause);
    }

    public NewMoteException(Throwable cause) {
        super(cause);
    }
}
