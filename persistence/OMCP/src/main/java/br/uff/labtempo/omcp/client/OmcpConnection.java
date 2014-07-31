/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.client;

import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.common.exceptions.BadResponseException;
import br.uff.labtempo.omcp.common.exceptions.ConnectionException;
import br.uff.labtempo.omcp.common.exceptions.RequestException;
import br.uff.labtempo.omcp.common.exceptions.UnreachableModuleException;
import br.uff.labtempo.omcp.common.utils.RequestBuilder;
import br.uff.labtempo.omcp.common.utils.RequestPacket;
import br.uff.labtempo.omcp.common.utils.ResponsePacket;

/**
 *
 * @author Felipe
 */
public class OmcpConnection {

    private RabbitClientSocket bridge;

    //osiris
    private final String DOMAIN = "osiris";

    public OmcpConnection(String host, String user, String password) throws ConnectionException {
        this.bridge = new RabbitClientSocket(host, user, password);
        this.bridge.connect();
    }

    public OmcpConnection(String host) throws ConnectionException {
        this.bridge = new RabbitClientSocket(host);
        this.bridge.connect();
    }

    public void close() {
        if (bridge != null) {
            this.bridge.close();
        }
    }

    public Response doGet(String url) throws UnreachableModuleException, RequestException, BadResponseException {
        Request request = new RequestBuilder().onGet(url).build();
        return call(request);
    }

    public Response doPost(String url, String content) throws UnreachableModuleException, RequestException, BadResponseException {
        Request request = new RequestBuilder().onPost(url).content(content).build();
        return call(request);
    }

    public Response doPut(String url, String content) throws UnreachableModuleException, RequestException, BadResponseException {
        Request request = new RequestBuilder().onPut(url).content(content).build();
        return call(request);
    }

    public Response doDelete(String url) throws UnreachableModuleException, RequestException, BadResponseException {
        Request request = new RequestBuilder().onDelete(url).build();
        return call(request);
    }

    public void doNofity(String url) throws UnreachableModuleException, RequestException {
        Request request = new RequestBuilder().onNotify(url).build();
        publish(request);
    }

    private Response call(Request request) {        
        String packet = new RequestPacket().generate(request);
        packet = bridge.call(getHostAddress(request), packet);
        Response response = new ResponsePacket().parse(packet);
        return response;
    }

    private void publish(Request request) {
        String packet = new RequestPacket().generate(request);
        bridge.publish(getHostAddress(request), packet);
    }

    private String getHostAddress(Request request) {
        return DOMAIN + "." + request.getModule();
    }
}
