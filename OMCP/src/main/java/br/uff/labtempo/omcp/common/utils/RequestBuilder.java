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
package br.uff.labtempo.omcp.common.utils;

import br.uff.labtempo.omcp.common.RequestMethod;
import static br.uff.labtempo.omcp.common.RequestMethod.*;
import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.common.exceptions.RequestException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class RequestBuilder {

    private static final String VERSION = "OMCP/0.1";
    private RequestMethod method;
    private String content;
    private String resource;
    private String module;
    private int contentLength;
    private String contentType;

    public RequestBuilder() {
        this.content = "";
        this.contentLength = content.length();
    }

    public RequestBuilder onGet(String url) {
        this.method = GET;
        this.parseUrl(url);
        return this;
    }

    public RequestBuilder onPost(String url) {
        this.method = POST;
        this.parseUrl(url);
        return this;
    }

    public RequestBuilder onPut(String url) {
        this.method = PUT;
        this.parseUrl(url);
        return this;
    }

    public RequestBuilder onDelete(String url) {
        this.method = DELETE;
        this.parseUrl(url);
        return this;
    }

    public RequestBuilder onNotify(String url) {
        this.method = NOTIFY;
        this.parseUrl(url);
        return this;
    }

    private void setContent(String content) {
        this.content = content;
        this.contentLength = content.length();
    }

    public RequestBuilder content(String content) {
        this.setContent(content);
        this.contentType = new Serializer().getTextContentType();
        return this;
    }

    public RequestBuilder objectContent(Object object, String contentType) {
        Serializer serializer = new Serializer();
        String content = null;
        try {
            this.contentType = serializer.fixContentTypeByReference(contentType);
            content = serializer.serialize(object, this.contentType);
        } catch (Exception e) {
            this.contentType = serializer.getTextContentType();
            content = object.toString();
        }
        this.setContent(content);
        return this;
    }

    public RequestBuilder xmlContent(Object object) {
        return objectContent(object, new Serializer().getXmlContentType());
    }

    public RequestBuilder jsonContent(Object object) {        
        return objectContent(object, new Serializer().getJsonContentType());
    }

    private void parseUrl(String url) {
        URI uri = getUri(url);
        this.resource = uri.getPath();
        this.module = uri.getHost();
        if (uri.getQuery() != null) {
            this.resource += "?" + uri.getQuery();
        }
        if (!VERSION.contains(uri.getScheme().toUpperCase())) {
            throw new RequestException("wrong scheme!");
        }
    }

    public Request build() {
        Request request = new Request(method, resource, VERSION, Calendar.getInstance(), module, content, contentLength, contentType);
        return request;
    }

    private URI getUri(String url) {
        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException ex) {
            throw new RequestException("Could not parse url!", ex);
        }
        return uri;
    }

    public RequestBuilder setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }
}
