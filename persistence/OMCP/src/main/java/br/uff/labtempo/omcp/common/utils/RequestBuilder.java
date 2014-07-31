/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.common.utils;

import br.uff.labtempo.omcp.common.Method;
import static br.uff.labtempo.omcp.common.Method.*;
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

    private Method method;
    private String content;
    private String resource;
    private String module;
    private int contentLength;

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

    public RequestBuilder content(String content) {
        this.content = content;
        this.contentLength = content.length();
        return this;
    }

    private void parseUrl(String url) {
        URI uri = getUri(url);
        this.resource = uri.getPath();
        this.module = uri.getHost();
        if (!VERSION.contains(uri.getScheme().toUpperCase())) {
            throw new RequestException("wrong scheme!");
        }
    }

    public Request build() {
        Request request = new Request(method, resource, VERSION, Calendar.getInstance(), module, content, contentLength);
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
}
