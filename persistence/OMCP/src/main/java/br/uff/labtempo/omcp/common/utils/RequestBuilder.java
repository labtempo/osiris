/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author Felipe
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

    public RequestBuilder xmlContent(Object object) {
        String content = null;
        Serializer serializer = new Serializer();

        this.contentType = serializer.getXmlContentType();
        content = serializer.toXml(object);

        this.setContent(content);
        return this;
    }

    public RequestBuilder jsonContent(Object object) {
        String content = null;
        Serializer serializer = new Serializer();

        this.contentType = serializer.getJsonContentType();
        content = serializer.toJson(object);

        this.setContent(content);
        return this;
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
