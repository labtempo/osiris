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

import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class Serializer {

    private final String TEXT_CT = "text/plain";
    private final String[] XML_CT = new String[]{
        "application/xml",
        "text/xml",
        "application/xhtml+xml",
        "application/atom+xml"
    };

    private final String[] JSON_CT = new String[]{
        "application/json",
        "application/javascript",
        "text/javascript"
    };

    public String toJson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public <T> T fromJson(String json, Class klass) {
        Gson gson = new Gson();
        return (T) gson.<T>fromJson(json, klass);
    }

    public <T> String toXml(T object) {
        XStream xstream = new XStream();
        xstream.alias(object.getClass().getSimpleName(), object.getClass());
        return xstream.toXML(object);
    }

    public <T> T fromXml(String xml, Class klass) {
        XStream xstream = new XStream();
        xstream.alias(klass.getSimpleName(), klass);
        return (T) xstream.fromXML(xml);
    }

    public String serialize(Object object, String contentType) {
        for (String xml : XML_CT) {
            if (xml.equals(contentType.toLowerCase())) {
                return toXml(object);
            }
        }
        return toJson(object);
    }

    public <T> T deserialize(String content, String contentType, Class klass) {
        for (String xml : XML_CT) {
            if (xml.equals(contentType.toLowerCase())) {
                return this.<T>fromXml(content, klass);
            }
        }
        return this.<T>fromJson(content, klass);
    }

    public String getTextContentType() {
        return TEXT_CT;
    }

    public String getXmlContentType() {
        return XML_CT[0];
    }

    public String getJsonContentType() {
        return JSON_CT[0];
    }

    public String fixContentTypeByReference(String contentType) {
        for (String xml : XML_CT) {
            if (contentType!= null && xml.equals(contentType.toLowerCase())) {
                return xml;
            }
        }
        return JSON_CT[0];
    }
}
