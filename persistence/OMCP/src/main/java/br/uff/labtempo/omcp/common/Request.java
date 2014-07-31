/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.common;

import java.util.Calendar;

/**
 *
 * @author Felipe
 */
public class Request {

    private final Method method;
    private final String version;
    private final String resource;
    private final Calendar date;
    private final String module;
    private final String content;
    private final int contentLength;

    public Request(Method method, String resource, String version, Calendar date, String module, String content, int contentLength) {
        this.method = method;
        this.resource = resource;
        this.version = version;
        this.date = date;
        this.module = module;
        this.content = content;
        this.contentLength = contentLength;
    }

    public Method getMethod() {
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

    public int getContentLength() {
        return contentLength;
    }
}
