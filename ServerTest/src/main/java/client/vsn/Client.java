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
package client.vsn;

import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.omcp.client.OmcpClientBuilder;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.utils.ResponsePacket;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class Client {

    public static void main(String[] args) throws Exception {
        try (OmcpClient connection = new OmcpClientBuilder().host("192.168.0.7").user("admin", "admin").source("generico").build()) {
            Response  r = connection.doGet("omcp://sensornet/labtempo/collectors/datacenter/sensors/50/?chave=valor&chave2=valor2");
            System.out.println(new ResponsePacket().generate(r));
             r = connection.doGet("omcp://sensornet/labtempo/collectors/datacenter/sensors/50/");  
//            connection.doNofity("omcp://teste/","teste");
//            connection.doNofity("omcp://test.ex/sensor/","teste");
//            connection.doNofity("omcp://test.ex/collector1/sample","teste");
//            connection.doNofity("omcp://test.ex/collector/","teste");
//            connection.doNofity("omcp://test.ex/collector/01","teste");
            System.out.println(new ResponsePacket().generate(r));
        }
    }
}
