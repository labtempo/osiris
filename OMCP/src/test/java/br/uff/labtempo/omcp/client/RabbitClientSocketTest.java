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
package br.uff.labtempo.omcp.client;

//import br.uff.labtempo.omcp.client.rabbitmq.RabbitClientSocket;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class RabbitClientSocketTest {

    //TODO: reimplementar classe de teste
    //TODO: melhorar teste quando o servidor está offline
    //private RabbitClientSocket connection;
    //@Before
    public void setUp() throws Exception {

        //this.connection = new RabbitClientSocket("192.168.0.7", "admin", "admin");
        //this.connection.connect();
    }

    //@After
    public void tearDown() throws Exception {
        //this.connection.close();
    }

    /**
     * Test of call method, of class RabbitBridge.
     */
    //@Test
    public void testCall() throws Exception {
//        if (connection.isLive() == false) {
//            return;
//        }
//
//        Request request = new RequestBuilder().onGet("omcp://sensornet/resource").build();
//        String packet = this.connection.call("osiris.sensornet", new RequestPacket().generate(request));
//        
//        ResponsePacket rp = new ResponsePacket();
//        Response response = rp.parse(packet);
//        System.out.println(rp.generate(response));
    }

    //@Test
    public void testPublish() throws Exception {
//        if (connection.isLive() == false) {
//
//        }
//        Request request = new RequestBuilder().onNotify("omcp://sensornet/resource").build();
//        this.connection.publish("osiris.sensornet", new RequestPacket().generate(request));
    }

    //@Test(expected = RuntimeException.class)
    public void testCallToInexistentQueue() throws Exception {
//        if (connection.isLive() == false) {
//            throw new RuntimeException();
//        }
//        this.connection.call("xyz", "teste");
    }

    //@Test(expected = RuntimeException.class)
    public void testPublishToInexistentQueue() throws Exception {
//        if (connection.isLive() == false) {
//            throw new RuntimeException();
//        }
//        this.connection.publish("xyz", "teste");
    }
}
