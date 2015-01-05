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

import br.uff.labtempo.omcp.common.Request;
import br.uff.labtempo.omcp.common.RequestMethod;
import static br.uff.labtempo.omcp.common.RequestMethod.*;
import br.uff.labtempo.omcp.common.exceptions.BadRequestException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import static br.uff.labtempo.omcp.common.utils.Headers.*;
import java.util.Arrays;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class RequestPacket {

    private RequestMethod method;
    private String protocolVersion;
    private String resource;
    private Calendar date;
    private String module;
    private String source;
    private String content;
    private int contentLength;

    //packet builders
    private final String br = "\n";
    private final String s = " ";
    private final String r = "\r";
    private String contentType;

    public String generate(Request request) {
        //TODO: falta teste para este item
        this.protocolVersion = request.getVersion();
        this.method = request.getMethod();
        this.resource = request.getResource();
        this.date = request.getDate();
        this.module = request.getModule();
        this.contentLength = request.getContentLength();
        this.contentType = request.getContentType();
        this.content = request.getContent();
        this.source = request.getSource();

        StringBuilder sb = new StringBuilder();
        sb.append(method)
                .append(s)
                .append(resource)
                .append(s)
                .append(protocolVersion)
                .append(br)
                .append(DATE.getKey())
                .append(new DateUtil().generate(date))
                .append(br)
                .append(MODULE.getKey())
                .append(module)
                .append(br)
                .append(SOURCE.getKey())
                .append(source)
                .append(br);

        if (contentType != null) {
            sb.append(CONTENT_TYPE.getKey())
                    .append(contentType)
                    .append(br);
        }
        if (POST.equals(method) || PUT.equals(method) || NOTIFY.equals(method)) {
            sb.append(CONTENT_LENGTH.getKey())
                    .append(contentLength)
                    .append(br)
                    .append(br)
                    .append(content)
                    .append(br);
        }

        return sb.toString();
    }

    public Request parse(String message) throws BadRequestException {
        this.process(message);

        Request request = new Request(this.method, this.resource, this.protocolVersion, this.date, this.module, this.content, this.contentLength, this.contentType);
        return request;
    }

    private void process(String message) throws BadRequestException {
        Queue<String> lines = new LinkedList<>();
        lines.addAll(Arrays.asList(message.split(br)));
        defineFirstLine(lines);
        extractHeaders(lines);
    }

    private void defineFirstLine(Queue<String> lines) throws BadRequestException {
        String[] firstLine = lines.poll().split(s);

        try {
            this.method = Enum.valueOf(RequestMethod.class, firstLine[0]);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Method is not correct");
        }

        this.resource = firstLine[1];
        this.protocolVersion = firstLine[2];

        if (this.method == null) {
            throw new BadRequestException("Method is null.");
        }

        if (this.resource == null) {
            throw new BadRequestException("Resource is null.");
        }

        if (this.protocolVersion == null) {
            throw new BadRequestException("Version is null.");
        }
    }

    private void extractHeaders(Queue<String> lines) throws BadRequestException {
        Map<String, String> headers = new LinkedHashMap<>();
        String line;
        while ((line = lines.poll()) != null) {
            if (line.length() == 0) {
                break;
            }
            String[] header = line.split(Headers.getSeparator(), 2);
            headers.put(header[0].toLowerCase(), header[1].replace(br, "").replace(r, ""));
        }

        if (headers.containsKey(DATE.toString())) {
            defineDate(headers.get(DATE.toString()));
        } else {
            throw new BadRequestException("Packet is not contains date.");
        }

        if (headers.containsKey(MODULE.toString())) {
            defineHost(headers.get(MODULE.toString()));
        } else {
            throw new BadRequestException("Packet is not contains host.");
        }

        if (headers.containsKey(SOURCE.toString())) {
            defineSource(headers.get(SOURCE.toString()));
        }
//        else {
//            throw new BadRequestException("Packet is not contains source.");
//        }

        if (GET.equals(method) || POST.equals(method) || PUT.equals(method) || NOTIFY.equals(method)) {
            this.contentType = headers.get(CONTENT_TYPE.toString());
        }

        if (POST.equals(method) || PUT.equals(method) || NOTIFY.equals(method)) {
            if (headers.containsKey(CONTENT_LENGTH.toString())) {
                this.content = defineContent(lines);
                this.contentLength = Integer.parseInt(headers.get(CONTENT_LENGTH.toString()));

                //is content null? is content empty?
                if (this.content == null || this.content.length() == 0) {
                    throw new BadRequestException("Method '" + this.method + "' needs a content.");
                }

                if (this.contentType == null) {
                    throw new BadRequestException("The content type needs to be declared.");
                }

            } else {
                throw new BadRequestException("Packet not has content!");
            }
        }
    }

    private void defineDate(String date) throws BadRequestException {
        try {
            DateUtil util = new DateUtil();
            this.date = util.parse(date);
        } catch (ParseException ex) {
            throw new BadRequestException("Date format is not correct.");
        }
    }

    private void defineHost(String host) {
        this.module = host;
    }

    private void defineSource(String source) {
        this.source = source;
    }

    private String defineContent(Queue<String> lines) {
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line);
        }
        return sb.toString();
    }
}
