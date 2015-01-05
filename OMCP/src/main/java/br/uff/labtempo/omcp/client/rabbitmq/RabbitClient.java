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
package br.uff.labtempo.omcp.client.rabbitmq;

import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.common.exceptions.BadResponseException;
import br.uff.labtempo.omcp.common.exceptions.ConnectionException;
import br.uff.labtempo.omcp.common.exceptions.RequestException;
import br.uff.labtempo.omcp.common.exceptions.UnreachableModuleException;
import br.uff.labtempo.omcp.common.utils.RabbitUtil;
import br.uff.labtempo.omcp.common.utils.RequestBuilder;
import br.uff.labtempo.omcp.common.utils.RequestPacket;
import br.uff.labtempo.omcp.common.utils.ResponsePacket;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class RabbitClient implements OmcpClient {

    private RabbitClientSocket socket;

    //osiris
    private final String DOMAIN = "osiris";
    private String id = "";

    public RabbitClient(String host, String user, String password) throws ConnectionException {
        this.socket = new RabbitClientSocket(host, user, password);
        this.socket.connect();
    }

    public RabbitClient(String host) throws ConnectionException {
        this.socket = new RabbitClientSocket(host);
        this.socket.connect();
    }

    public boolean isAlive() {
        return socket.isAlive();
    }

    @Override
    public void close() {
        if (socket != null) {
            this.socket.close();
        }
    }

    @Override
    public Response doGet(String url) throws UnreachableModuleException, RequestException, BadResponseException {
        Request request = new RequestBuilder().onGet(url).build();
        return call(request);
    }

    @Override
    public Response doGet(String url, String contentType) throws UnreachableModuleException, RequestException, BadResponseException {
        Request request = new RequestBuilder().onGet(url).setContentType(contentType).build();
        return call(request);
    }

    @Override
    public Response doPost(String url, String content) throws UnreachableModuleException, RequestException, BadResponseException {
        Request request = new RequestBuilder().onPost(url).content(content).build();
        return call(request);
    }

    @Override
    public Response doPost(String url, Object content) throws UnreachableModuleException, RequestException, BadResponseException {
        Request request = new RequestBuilder().onPost(url).jsonContent(content).build();
        return call(request);
    }

    @Override
    public Response doPost(String url, Object content, String contentType) throws UnreachableModuleException, RequestException, BadResponseException {
        Request request = new RequestBuilder().onPost(url).objectContent(content, contentType).build();
        return call(request);
    }

    @Override
    public Response doPut(String url, String content) throws UnreachableModuleException, RequestException, BadResponseException {
        Request request = new RequestBuilder().onPut(url).content(content).build();
        return call(request);
    }

    @Override
    public Response doPut(String url, Object content) throws UnreachableModuleException, RequestException, BadResponseException {
        Request request = new RequestBuilder().onPut(url).jsonContent(content).build();
        return call(request);
    }

    @Override
    public Response doPut(String url, Object content, String contentType) throws UnreachableModuleException, RequestException, BadResponseException {
        Request request = new RequestBuilder().onPut(url).objectContent(content, contentType).build();
        return call(request);
    }

    @Override
    public Response doDelete(String url) throws UnreachableModuleException, RequestException, BadResponseException {
        Request request = new RequestBuilder().onDelete(url).build();
        return call(request);
    }

    @Override
    public void doNofity(String url, String content) throws UnreachableModuleException, RequestException {
        Request request = new RequestBuilder().onNotify(url).content(content).build();
        publish(request);
    }

    @Override
    public void doNofity(String url, Object content) throws UnreachableModuleException, RequestException {
        Request request = new RequestBuilder().onNotify(url).jsonContent(content).build();
        publish(request);
    }

    @Override
    public void doNofity(String url, Object content, String contentType) throws UnreachableModuleException, RequestException {
        Request request = new RequestBuilder().onNotify(url).objectContent(content, contentType).build();
        publish(request);
    }

    @Override
    public void setSourceId(String id) {
        this.id = id;
    }

    private Response call(Request request) {
        request.setSource(id);
        String packet = new RequestPacket().generate(request);
        packet = socket.call(RabbitUtil.getHostAddress(request, DOMAIN), packet);
        Response response = new ResponsePacket().parse(packet);
        return response;
    }

    private void publish(Request request) {
        request.setSource(id);
        String packet = new RequestPacket().generate(request);
        socket.publish(RabbitUtil.getHostAddress(request, DOMAIN), packet, RabbitUtil.getRoutingKey(request));
    }

}
