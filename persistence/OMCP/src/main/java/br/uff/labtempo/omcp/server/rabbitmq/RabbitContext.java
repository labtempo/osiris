/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.server.rabbitmq;

import br.uff.labtempo.omcp.server.Context;

/**
 *
 * @author Felipe
 */
public class RabbitContext implements Context {

    private String host;
    private String ProtocolVersion;
    private String ModuleDescription;

    public RabbitContext(String host, String ProtocolVersion, String ModuleDescription) {
        this.host = host;
        this.ProtocolVersion = ProtocolVersion;
        this.ModuleDescription = ModuleDescription;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getProtocolVersion() {
        return ProtocolVersion;
    }

    @Override
    public String getModuleDescription() {
        return ModuleDescription;
    }

}
