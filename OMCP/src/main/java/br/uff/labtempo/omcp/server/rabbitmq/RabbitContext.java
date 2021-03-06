/* 
 * Copyright 2015 Felipe Santos <fralph at ic.uff.br>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.uff.labtempo.omcp.server.rabbitmq;

import br.uff.labtempo.omcp.server.Context;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
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
