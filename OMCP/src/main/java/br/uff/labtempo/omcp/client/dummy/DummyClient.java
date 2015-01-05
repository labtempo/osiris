/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.client.dummy;

import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.exceptions.BadResponseException;
import br.uff.labtempo.omcp.common.exceptions.RequestException;
import br.uff.labtempo.omcp.common.exceptions.UnreachableModuleException;
import br.uff.labtempo.omcp.common.utils.RequestBuilder;
import br.uff.labtempo.omcp.common.utils.RequestPacket;


/**
 *
 * @author Felipe
 */
public class DummyClient implements OmcpClient {

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
    }

    protected Response call(Request request) {
        System.out.println(repacket(request));
        return null;
    }

    protected void publish(Request request) {
        System.out.println(repacket(request));
    }

    private String repacket(Request request) {
        return new RequestPacket().generate(request);
    }

    @Override
    public void close() throws Exception {
        System.out.println("Closed!");
    }
}
