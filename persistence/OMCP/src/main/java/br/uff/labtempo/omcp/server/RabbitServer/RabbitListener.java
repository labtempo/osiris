/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.labtempo.omcp.server.RabbitServer;

import br.uff.labtempo.omcp.server.core.ServerResponseContainer;

/**
 *
 * @author Felipe
 */
interface RabbitListener {
     public boolean incoming(String message, ServerResponseContainer responseContainer);
}
