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
package br.uff.labtempo.osiris.thincollector;

import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.omcp.client.OmcpClientBuilder;
import br.uff.labtempo.osiris.to.collector.CollectorCoTo;
import br.uff.labtempo.osiris.to.collector.NetworkCoTo;
import br.uff.labtempo.osiris.to.collector.SampleCoTo;
import br.uff.labtempo.osiris.to.collector.SensorCoTo;
import br.uff.labtempo.osiris.to.common.definitions.Path;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        try {

            String[] credentials = args[0].split(":");
            String[] identifiers = args[1].split(":");
            String[] tuple = args[2].split(":");

            String host = credentials[0];
            String user = credentials[1];
            String pass = credentials[2];

            String networkId = identifiers[0];
            String collectorId = identifiers[1];
            String sensorId = identifiers[2];

            String name = tuple[0];
            String value = tuple[1];
            String unit = tuple[2];
            String symbol = tuple[3];
            try (OmcpClient client = new OmcpClientBuilder().host(host).user(user, pass).source("thin-collector").build()) {
                String url = Path.MESSAGEGROUP_COLLECTOR.toString() + networkId + Path.SEPARATOR + collectorId + Path.SEPARATOR + sensorId;
                NetworkCoTo nct = new NetworkCoTo(networkId);
                CollectorCoTo cct = new CollectorCoTo(collectorId, 10, TimeUnit.SECONDS);
                SensorCoTo sct = new SensorCoTo(sensorId, System.currentTimeMillis(), (int) System.nanoTime());
                sct.addValue(name, Integer.valueOf(value), unit, symbol);
                SampleCoTo sampleCoTo = new SampleCoTo(nct, cct, sct);
                client.doNofity(url, sampleCoTo);
                System.out.println(value);
            }
        } catch (Exception e) {
            System.out.println("Require args separete by :");
            System.out.println("Example:");
            System.out.println("");
            System.out.println("127.0.0.1:user:pass");
            System.out.println("networkId:collectorId:sensorId");
            System.out.println("temperatura:30.5:Celsius:C");
        }
        
        System.exit(0);
    }
}
