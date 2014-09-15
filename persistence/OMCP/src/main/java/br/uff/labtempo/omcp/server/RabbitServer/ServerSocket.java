/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.server.RabbitServer;

import br.uff.labtempo.omcp.server.RequestHandler;

/**
 *
 * @author Felipe
 */
public interface ServerSocket {

    public void setListener(RabbitListener listener);

    public void run();
    
    public void abort();
}
