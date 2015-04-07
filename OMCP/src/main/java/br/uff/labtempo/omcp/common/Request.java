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
package br.uff.labtempo.omcp.common;

import br.uff.labtempo.omcp.common.exceptions.BadRequestException;
import br.uff.labtempo.omcp.common.utils.Serializer;
import java.util.Calendar;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class Request {

    private final RequestMethod method;
    private final String version;
    private final String resource;
    private final Calendar date;
    private final String module;
    private final String content;
    private final int contentLength;
    private final String contentType;
    /**
     * Represents a module name Intend that is a module to module communication
     */
    private String source;

    public Request(RequestMethod method, String resource, String version, Calendar date, String module, String content, int contentLength, String contentType) {
        this.method = method;
        this.resource = resource;
        this.version = version;
        this.date = date;
        this.module = module;
        this.content = content;
        this.contentLength = contentLength;
        this.contentType = contentType;
    }

    public RequestMethod getMethod() {
        return method;
    }

    public String getVersion() {
        return version;
    }

    public String getResource() {
        return resource;
    }

    public Calendar getDate() {
        return date;
    }

    public String getModule() {
        return module;
    }

    public String getContent() {
        return content;
    }

    public <T> T getContent(Class klass) throws BadRequestException {
        try {
            Serializer serializer = new Serializer();
            T obj = serializer.<T>deserialize(content, contentType, klass);
            return obj;
        } catch (Exception ex) {
            throw new BadRequestException("Request message format is not correct.");
        }
    }

    public int getContentLength() {
        return contentLength;
    }

    public String getContentType() {
        return contentType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
