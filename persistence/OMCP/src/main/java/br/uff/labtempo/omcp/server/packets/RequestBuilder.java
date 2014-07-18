/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.server.packets;

import br.uff.labtempo.omcp.common.OmcpMethods;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.TimeZone;

/**
 *
 * @author Felipe
 */
public class RequestBuilder {

    private OmcpMethods method;
    private String version;
    private URI resource;

    private Calendar date;
    private URI host;

    private String content;

    public RequestBuilder message(String message) {
        Queue<String> lines = new LinkedList<>();
        for (String line : message.split("\n")) {
            lines.add(line);
        }
        defineFirstLine(lines);
        extractHeaders(lines);
        defineContent(lines);
        return this;
    }

    private void defineFirstLine(Queue<String> lines) {
        String[] firstLine = lines.poll().split("\\s");
        try {
            this.method = Enum.valueOf(OmcpMethods.class, firstLine[0]);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Method is not correct");
        }
        try {
            this.resource = new URI(firstLine[1]);
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException("Resource is null.");
        }
        this.version = firstLine[2];

        if (this.method == null) {
            throw new IllegalArgumentException("Method is null.");
        }

        if (this.resource == null) {
            throw new IllegalArgumentException("Resource is null.");
        }

        if (this.version == null) {
            throw new IllegalArgumentException("Version is null.");
        }

    }

    private void extractHeaders(Queue<String> lines) {
        Map<String, String> headers = new LinkedHashMap<>();
        String line;
        while ((line = lines.poll()) != null) {
            if (line.length() == 0) {
                break;
            }
            String[] header = line.split(": ", 2);
            headers.put(header[0].toLowerCase(), header[1].replace("\n", "").replace("\r", ""));
        }
        if (headers.containsKey("date")) {
            defineDate(headers.get("date"));
        } else {
            throw new IllegalArgumentException("Packet is not contains date.");
        }
        if (headers.containsKey("host")) {
            defineHost(headers.get("host"));
        } else {
            throw new IllegalArgumentException("Packet is not contains host.");
        }
    }

    private void defineDate(String date) {
        try {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            calendar.setTime(sdf.parse(date));
            this.date = calendar;
        } catch (ParseException ex) {
            throw new IllegalArgumentException("Date format is not correct.");
        }
    }

    private void defineHost(String host) {
        try {
            URI uri = new URI(host);
            this.host = uri;
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException("Host uri format is not correct.");
        }

    }

    private void defineContent(Queue<String> lines) {
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line);
        }
        this.content = sb.toString();
    }

    public Request build() {
        Request request = new Request();

        request.setMethod(this.method);
        request.setVersion(this.version);

        request.setResource(this.resource);
        request.setHost(this.host);
        request.setDate(this.date);

        if (this.method.equals(OmcpMethods.POST) || this.method.equals(OmcpMethods.PUT)) {
            if (this.content != null && this.content.length() > 0) {
                request.setContent(this.content);
            }else{
                throw new IllegalArgumentException("Method needs a content.");
            }
        }
        return request;
    }
}
