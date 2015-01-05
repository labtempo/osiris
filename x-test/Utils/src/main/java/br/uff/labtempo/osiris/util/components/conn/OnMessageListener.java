/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.labtempo.osiris.util.components.conn;

/**
 *
 * @author Felipe
 */
public interface OnMessageListener {
    public void onReceiveMessage(String message, String subject);
}
