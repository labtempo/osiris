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

import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.exceptions.BadResponseException;
import br.uff.labtempo.omcp.common.exceptions.RequestException;
import br.uff.labtempo.omcp.common.exceptions.UnreachableModuleException;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public interface OmcpClient extends AutoCloseable {

    void setSourceId(String id);

    Response doGet(String url) throws UnreachableModuleException, RequestException, BadResponseException;

    Response doGet(String url, String contentType) throws UnreachableModuleException, RequestException, BadResponseException;

    Response doPost(String url, String content) throws UnreachableModuleException, RequestException, BadResponseException;

    Response doPost(String url, Object content) throws UnreachableModuleException, RequestException, BadResponseException;

    Response doPost(String url, Object content, String contentType) throws UnreachableModuleException, RequestException, BadResponseException;

    Response doPut(String url, String content) throws UnreachableModuleException, RequestException, BadResponseException;

    Response doPut(String url, Object content) throws UnreachableModuleException, RequestException, BadResponseException;

    Response doPut(String url, Object content, String contentType) throws UnreachableModuleException, RequestException, BadResponseException;

    Response doDelete(String url) throws UnreachableModuleException, RequestException, BadResponseException;

    void doNofity(String url, String content) throws UnreachableModuleException, RequestException;

    void doNofity(String url, Object content) throws UnreachableModuleException, RequestException;

    void doNofity(String url, Object content, String contentType) throws UnreachableModuleException, RequestException;
}
