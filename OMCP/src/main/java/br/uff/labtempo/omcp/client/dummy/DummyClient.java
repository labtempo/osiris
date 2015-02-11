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
package br.uff.labtempo.omcp.client.dummy;

import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.exceptions.client.BadResponseException;
import br.uff.labtempo.omcp.common.exceptions.client.RequestException;
import br.uff.labtempo.omcp.common.exceptions.client.UnreachableModuleException;
import br.uff.labtempo.omcp.common.utils.RequestBuilder;
import br.uff.labtempo.omcp.common.utils.RequestPacket;


/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
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
