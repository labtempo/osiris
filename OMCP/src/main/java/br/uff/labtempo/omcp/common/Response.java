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

import br.uff.labtempo.omcp.common.utils.Serializer;
import java.util.Calendar;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class Response {

    private final StatusCode statusCode;
    private final Calendar date;
    private final int contentLength;
    private final String location;
    private final String errorMessage;
    private final String content;
    private final String contentType;

    private String protocolVersion;
    private String module;
    private String host;

    /**
     * Constructor - Read only object
     *
     * @param protocolVersion
     * @param statusCode
     * @param date
     * @param module
     * @param content
     * @param contentLength
     * @param location
     * @param errorMessage
     */
    public Response(String protocolVersion, StatusCode statusCode, Calendar date, String module, String content, int contentLength, String contentType, String location, String errorMessage) {
        this.protocolVersion = protocolVersion;
        this.statusCode = statusCode;
        this.date = date;
        this.module = module;
        this.content = content;
        this.contentLength = contentLength;
        this.errorMessage = errorMessage;
        this.location = location;
        this.contentType = contentType;
    }

    public String getProtocolVersion() {
        return this.protocolVersion;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public Calendar getDate() {
        return this.date;
    }

    public String getModule() {
        return this.module;
    }

    public int getContentLength() {
        return this.contentLength;
    }

    public String getContent() {
        return this.content;
    }

    public String getContentType() {
        return this.contentType;
    }

    public <T> T getContent(Class klass) {
        Serializer serializer = new Serializer();
        T obj = serializer.<T>deserialize(content, contentType, klass);
        return obj;
    }

    public String getLocation() {
        if (host != null) {
            String location = host + this.location;
            location = location.replace("//", "/").replace(":/", "://");
            return location;
        }
        return this.location;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public boolean isError() {
        return statusCode.isError();
    }

    public boolean hasContent() {
        return (contentLength > 0);
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
