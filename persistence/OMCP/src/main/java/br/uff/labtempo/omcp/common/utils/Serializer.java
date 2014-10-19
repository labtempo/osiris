/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.common.utils;

import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;

/**
 *
 * @author Felipe
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

    protected <T> T fromJson(String json, Class klass) {
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
