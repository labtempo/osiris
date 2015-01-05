/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.util.components.conn.rpc;

/**
 *
 * @author Felipe
 */
class RPCClientException extends Exception {

    public RPCClientException() {
        super();
    }

    public RPCClientException(String message) {
        super(message);
    }

    public RPCClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public RPCClientException(Throwable cause) {
        super(cause);
    }

}
