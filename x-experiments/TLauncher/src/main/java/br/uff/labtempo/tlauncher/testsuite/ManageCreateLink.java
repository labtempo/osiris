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
package br.uff.labtempo.tlauncher.testsuite;

import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.tlauncher.data.CollectorDataBuilder;
import br.uff.labtempo.tlauncher.data.VirtualSensorNetDataBuilder;
import br.uff.labtempo.tlauncher.data.VirtualSensorPrintFormat;
import br.uff.labtempo.tlauncher.model.ConnectionFactory;
import br.uff.labtempo.tlauncher.model.ModuleManager;
import java.io.File;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class ManageCreateLink implements TestSuite {

    private final String collectorId;
    private final String sensorId;

    public ManageCreateLink(String collectorId, String sensorId) {
        this.collectorId = collectorId;
        this.sensorId = sensorId;
    }

    @Override
    public void start() {
        ModuleManager manager = new ModuleManager("..\\..\\VirtualSensorNet\\target\\VirtualSensorNet-1.0-SNAPSHOT-jar-with-dependencies.jar");
        ConnectionFactory factory = manager.getConfig();
        OmcpClient client = factory.getClient();

        VirtualSensorNetDataBuilder vsnDataBuilder = new VirtualSensorNetDataBuilder(client);
        CollectorDataBuilder collectorDataBuilder = new CollectorDataBuilder(client, collectorId);

        try {
            System.out.println("Criando link");
            vsnDataBuilder.createLink(collectorId, sensorId);

        } catch (Exception e) {
        }
    }
}
