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
package br.uff.labtempo.osiris.virtualsensornet.controller.util;

import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.exceptions.client.BadResponseException;
import br.uff.labtempo.omcp.common.exceptions.client.RequestException;
import br.uff.labtempo.omcp.common.exceptions.client.UnreachableModuleException;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class OmcpClientWrapper implements OmcpClient {

    private final OmcpClient client;

    public OmcpClientWrapper(OmcpClient client) {
        this.client = client;
    }

    @Override
    public void setSourceId(String id) {
        if (client == null) {
            return;
        }
        client.setSourceId(id);
    }

    @Override
    public Response doGet(String url) throws UnreachableModuleException, RequestException, BadResponseException {
        if (client == null) {
            return null;
        }
        return client.doGet(url);
    }

    @Override
    public Response doGet(String url, String contentType) throws UnreachableModuleException, RequestException, BadResponseException {
        if (client == null) {
            return null;
        }
        return client.doGet(url, contentType);
    }

    @Override
    public Response doPost(String url, String content) throws UnreachableModuleException, RequestException, BadResponseException {
        if (client == null) {
            return null;
        }
        return client.doPost(url, content);
    }

    @Override
    public Response doPost(String url, Object content) throws UnreachableModuleException, RequestException, BadResponseException {
        if (client == null) {
            return null;
        }
        return client.doPost(url, content);
    }

    @Override
    public Response doPost(String url, Object content, String contentType) throws UnreachableModuleException, RequestException, BadResponseException {
        if (client == null) {
            return null;
        }
        return client.doPost(url, content, contentType);
    }

    @Override
    public Response doPut(String url, String content) throws UnreachableModuleException, RequestException, BadResponseException {
        if (client == null) {
            return null;
        }
        return client.doPut(url, content);
    }

    @Override
    public Response doPut(String url, Object content) throws UnreachableModuleException, RequestException, BadResponseException {
        if (client == null) {
            return null;
        }
        return client.doPut(url, content);
    }

    @Override
    public Response doPut(String url, Object content, String contentType) throws UnreachableModuleException, RequestException, BadResponseException {
        if (client == null) {
            return null;
        }
        return client.doPut(url, content, contentType);
    }

    @Override
    public Response doDelete(String url) throws UnreachableModuleException, RequestException, BadResponseException {
        if (client == null) {
            return null;
        }
        return client.doDelete(url);
    }

    @Override
    public void doNofity(String url, String content) throws UnreachableModuleException, RequestException {
        if (client == null) {
            return;
        }
        client.doNofity(url, content);
    }

    @Override
    public void doNofity(String url, Object content) throws UnreachableModuleException, RequestException {
        if (client == null) {
            return;
        }
        client.doNofity(url, content);
    }

    @Override
    public void doNofity(String url, Object content, String contentType) throws UnreachableModuleException, RequestException {
        if (client == null) {
            return;
        }
        client.doNofity(url, content, contentType);
    }

    @Override
    public void close() throws Exception {
        if (client == null) {
            return;
        }
        client.close();
    }
}
