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

import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.StatusCode;
import static br.uff.labtempo.omcp.common.StatusCode.*;
import br.uff.labtempo.omcp.common.exceptions.AbstractRequestException;
import java.util.Calendar;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class ResponseBuilder {

    private StatusCode statusCode;
    private String content;
    private String location;
    private String error;
    private int contentLength;
    private String contentType;

    private void setContent(String content) {
        this.statusCode = OK;
        this.content = content;
        this.contentLength = content.length();
    }

    public ResponseBuilder ok() {
        content = "";
        this.setContent(content);
        this.contentType = new Serializer().getTextContentType();
        return this;
    }

    public ResponseBuilder ok(String content) {
        if (content == null) {
            content = "";
        }
        this.setContent(content);
        this.contentType = new Serializer().getTextContentType();
        return this;
    }

    public ResponseBuilder ok(Object object, ContentType type) {
        String content = "";

        if (object == null) {
            object = "";
        }

        Serializer serializer = new Serializer();
        switch (type) {
            case XML:
                this.contentType = serializer.getXmlContentType();
                content = serializer.toXml(object);
                break;
            case JSON:
                this.contentType = serializer.getJsonContentType();
                content = serializer.toJson(object);
                break;
        }
        this.setContent(content);
        return this;
    }

    public ResponseBuilder ok(Object object, String contentType) {

        if (object == null) {
            object = "";
        }
        Serializer serializer = new Serializer();
        this.contentType = serializer.fixContentTypeByReference(contentType);
        String content = serializer.serialize(object, this.contentType);
        this.setContent(content);
        return this;
    }

    public ResponseBuilder created(String uri) {
        this.statusCode = CREATED;
        this.location = uri;
        return this;
    }

    public ResponseBuilder error(AbstractRequestException ex) {
        this.statusCode = ex.getStatusCode();
        this.error = ex.getMessage();
        return this;
    }

    public Response build() {
        Response response = new Response(null, statusCode, Calendar.getInstance(), null, content, contentLength, contentType, location, error);
        return response;
    }

    public Response buildNull() {
        Response response = new Response(null, null, null, null, null, 0, null, null, null);
        return response;
    }

    public enum ContentType {

        XML, JSON;
    }
}
