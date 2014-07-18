/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.springconsumer.osiris;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 *
 * @author Felipe
 */
public class RequestBuilder {

    private String method;
    private String resource;
    private String protocol;
    private Map<String, String> headers;
    private String content;

    public void addPacket(String packet) {
        String separator = System.getProperty("line.separator");
        Queue<String> lines = new LinkedList<>();
        
        for(String line:packet.split("\n")){
            lines.add(line);
        }

        defineFirstLine(lines);
        defineHeaders(lines);
        defineContent(lines);
    }

    private void defineFirstLine(Queue<String> lines) {
        String[] firstLine = lines.poll().split("\\s");
        this.method = firstLine[0];
        this.resource = firstLine[1];
        this.protocol = firstLine[2];
    }

    private void defineHeaders(Queue<String> lines) {
        this.headers = new LinkedHashMap<>();
        
        String line;
        while ( (line = lines.poll()) != null) {
            if (line.length() == 0) {
                break;
            }
            String[] header = line.split(": ",2);
            this.headers.put(header[0], header[1]);
        }
    }

    private void defineContent(Queue<String> lines) {
        StringBuilder sb = new StringBuilder();

        for (String line : lines) {
            sb.append(line);
        }
        
        this.content = sb.toString();
    }

    public OsirisRequest build() {
        return new OsirisRequest(method, resource, protocol, headers, content);
    }

}
