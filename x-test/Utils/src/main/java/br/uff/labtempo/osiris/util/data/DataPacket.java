/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.util.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class DataPacket {

    private Map<Field, Object> attributes;
    private static final char DELIMITER = ';';

    private DataPacket(String reference, String id, String parent, long timestamp, long interval, String data) {

        if (parent == null) {
            parent = "";
        }
        try {
            attributes = new HashMap<>();
            attributes.put(Field.REFERENCE, reference.toString());
            attributes.put(Field.ID, id.toString());
            attributes.put(Field.PARENT, parent.toString());
            attributes.put(Field.TIMESTAMP, timestamp);
            attributes.put(Field.INTERVAL, interval);
            attributes.put(Field.DATA, data.toString());
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
    }

    public static DataPacket create(String reference, String id, String parent, long timestamp, long delay, String data) {
        return new DataPacket(reference, id, parent, timestamp, delay, data);
    }

    public static DataPacket create(String packet) {
        String[] args = packet.split(String.valueOf(DELIMITER));
        try {

            Map<Field, String> attr = new HashMap<>();
            int i = 0;
            for (Field field : Field.values()) {
                attr.put(field, args[i++]);
            }

            String reference = attr.get(Field.REFERENCE).toString();
            String id = attr.get(Field.ID).toString();
            String parent = attr.get(Field.PARENT).toString();
            long timestamp = Long.parseLong(attr.get(Field.TIMESTAMP).toString());
            long interval = Long.parseLong(attr.get(Field.INTERVAL).toString());
            String data = attr.get(Field.DATA).toString();
            return create(reference, id, parent, timestamp, interval, data);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Can't parse data string");
        }
    }

    public String getFullResourcePath(){
        return getSourceId()+"."+getId();
    }
    
    public String getSourceId() {
        return (String) attributes.get(Field.REFERENCE);
    }

    public String getId() {
        return (String) attributes.get(Field.ID);
    }

    public String getRole() {
        return (String) attributes.get(Field.ROLE);
    }

    public String getParent() {
        if (((String) attributes.get(Field.PARENT)).isEmpty()) {
            return null;
        }
        return (String) attributes.get(Field.PARENT);
    }

    public long getTimestamp() {
        return (long) attributes.get(Field.TIMESTAMP);
    }

    public long getInterval() {
        return (long) attributes.get(Field.INTERVAL);
    }

    public long getDelay() {
        return 10;
    }

    public String getData() {
        return (String) attributes.get(Field.DATA);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Field field : Field.values()) {
            Object obj = attributes.get(field);

            if (obj == null) {
                obj = "";
            }

            builder.append(obj);
            builder.append(DELIMITER);
        }
        return builder.toString();
    }

    private enum Field {

        REFERENCE, ID, ROLE, PARENT, TIMESTAMP, INTERVAL, DELAY, DATA
    }
}
