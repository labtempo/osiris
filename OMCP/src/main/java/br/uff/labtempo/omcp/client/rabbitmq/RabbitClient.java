/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author Felipe
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
