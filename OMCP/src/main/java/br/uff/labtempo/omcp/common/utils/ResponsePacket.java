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
import br.uff.labtempo.omcp.common.exceptions.BadResponseException;
import static br.uff.labtempo.omcp.common.utils.Headers.*;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class ResponsePacket {

    //packet properties values
    private String protocolVersion;
    private StatusCode statusCode;

    //headers
    private Calendar date;
    private String module;
    private String content;
    private int contentLength;
    private String contentType;
    private String errorMessage;
    private String location;

    //packet builders
    private final String br = "\n";
    private final String s = " ";
    private final String r = "\r";

    /**
     * Server side method. Generate a text packet from response object.
     *
     * @param response
     * @return
     */
    public String generate(Response response) {
        this.protocolVersion = response.getProtocolVersion();
        this.statusCode = response.getStatusCode();

        this.date = response.getDate();
        this.module = response.getModule();
        this.contentLength = response.getContentLength();        
        this.contentType = response.getContentType();
        this.content = response.getContent();
        this.location = response.getLocation();
        this.errorMessage = response.getErrorMessage();

        StringBuilder sb = new StringBuilder();
        sb.append(protocolVersion)
                .append(s)
                .append(statusCode.toCode())
                .append(s)
                .append(statusCode.toString())
                .append(br)
                .append(DATE.getKey())
                .append(new DateUtil().generate(date))
                .append(br)
                .append(MODULE.getKey())
                .append(module)
                .append(br);

        switch (statusCode) {
            case OK:
                sb.append(CONTENT_LENGTH.getKey())
                        .append(contentLength)
                        .append(br)
                        .append(CONTENT_TYPE.getKey())
                        .append(contentType)
                        .append(br)
                        .append(br)
                        .append(content)
                        .append(br);
                break;
            case CREATED:
                sb.append(LOCATION.getKey())
                        .append(location)
                        .append(br);
                break;
            default:
                sb.append(ERROR.getKey())
                        .append(errorMessage)
                        .append(br);
                break;
        }
        return sb.toString();
    }

    /**
     * Client side method. Parse a text packet to response object.
     *
     * @param response
     * @return
     */
    public Response parse(String response) {
        this.process(response);
        return new Response(protocolVersion, statusCode, date, module, content, contentLength, contentType, location, errorMessage);
    }

    private void process(String message) {
        Queue<String> lines = new LinkedList<>();
        lines.addAll(Arrays.asList(message.split(br)));
        this.defineFirstLine(lines);
        this.extractHeaders(lines);
    }

    private void defineFirstLine(Queue<String> lines) {
        String[] firstLine = lines.poll().split(s);
        this.protocolVersion = firstLine[0];
        try {
            this.statusCode = StatusCode.getByCode(Integer.parseInt(firstLine[1]));
        } catch (Exception ex) {
            throw new BadResponseException("Method is not correct");
        }
    }

    private void extractHeaders(Queue<String> lines) {
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
            this.date = this.defineDate(headers.get(DATE.toString()));
        } else {
            throw new BadResponseException("Packet is not contains date!");
        }

        if (headers.containsKey(MODULE.toString())) {
            this.module = headers.get(MODULE.toString());
        } else {
            throw new BadResponseException("Packet is not contains date!");
        }

        switch (this.statusCode) {
            case OK:
                if (headers.containsKey(CONTENT_LENGTH.toString())) {
                    this.content = defineContent(lines);
                    this.contentLength = Integer.parseInt(headers.get(CONTENT_LENGTH.toString()));
                    this.contentType = headers.get(CONTENT_TYPE.toString());
                } else {
                    throw new BadResponseException("Packet not has content!");
                }
                break;
            case CREATED:
                if (headers.containsKey(LOCATION.toString())) {
                    this.location = headers.get(LOCATION.toString());
                } else {
                    throw new BadResponseException("Packet is not contains location!");
                }
                break;
            default:
                if (headers.containsKey(ERROR.toString())) {
                    this.errorMessage = headers.get(ERROR.toString());
                } else {
                    throw new BadResponseException("Packet is not contains error message!");
                }
                break;
        }
    }

    private Calendar defineDate(String date) {
        try {
            DateUtil util = new DateUtil();
            return util.parse(date);
        } catch (ParseException ex) {
            throw new BadResponseException("Date format is not correct.");
        }
    }

    private String defineContent(Queue<String> lines) {
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line);
        }
        return sb.toString();
    }
}
