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

    private final String host;
    private final String protocolVersion;
    private final String moduleDescription;
    private final String moduleName;

    public RabbitContext(String host, String protocolVersion, String moduleDescription, String moduleName) {
        this.host = host;
        this.protocolVersion = protocolVersion;
        this.moduleDescription = moduleDescription;
        this.moduleName = moduleName;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getProtocolVersion() {
        return protocolVersion;
    }

    @Override
    public String getModuleDescription() {
        return moduleDescription;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }

}
