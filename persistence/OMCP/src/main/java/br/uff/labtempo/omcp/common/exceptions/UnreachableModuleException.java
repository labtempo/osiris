/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.common.exceptions;

/**
 * This exception can be designate these problems when calling other module:<br>
 * <ol>
 * <li>not connected to broker</li>
 * <li>not has the declared queue</li>
 * <li>module not exists</li>
 * </ol>
 *
 * @author Felipe
 */
public class UnreachableModuleException extends AbstractClientRuntimeException {

    public UnreachableModuleException() {
        super();
    }

    public UnreachableModuleException(String message) {
        super(message);
    }

    public UnreachableModuleException(Throwable cause) {
        super(cause);
    }

    public UnreachableModuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
