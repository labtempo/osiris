/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.server;

/**
 *
 * @author Felipe
 */
public interface Context {
    String getHost();
    String getProtocolVersion();
    String getModuleDescription();
    String getModuleName();
}
