/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.omcp;

import com.google.gson.Gson;

/**
 *
 * @author Felipe
 */
public class Notification {

    public enum Type {

        Created, Modified, Deleted;
    }

    private Type type;
    private String url;
    private transient Object object;
    private String data;
    private String className;

    public Notification(Type type, String url) {
        this.url = url;
        this.type = type;
    }

    public Notification(Type type, Object object, String klass) {
        this.object = object;
        this.type = type;
        this.className = klass;
        this.data = toJson(object);
    }

    public static Notification parse(String json) {
        return Notification.<Notification>fromJson(json, Notification.class);
    }

    public boolean hasData() {
        if(object == null){
            return data != null;
        }
        return object != null;
    }

    public boolean hasUrl() {
        return url != null;
    }

    public Type getType() {
        return type;
    }

    public <T> T getData(Class<T> klass) {
        if (object == null) {
            object = (T) Notification.<T>fromJson(data, klass);
        }
        return (T) object;
    }

    public String getUrl() {
        return url;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public String toString() {
        return toJson(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)//mesma instancia  
        {
            return true;
        }
        if (obj == null)//objeto null sempre igual a false  
        {
            return false;
        }
        if (getClass() != obj.getClass())//são de de classes diferentes  
        {
            return false;
        }

        Notification other = (Notification) obj;

        if (getType() == other.getType()) {
            if (other.url != null && url != null && url.equals(other.url)) {
                return true;
            }
            if (other.object != null && object != null && object.equals(other.object)) {
                return true;
            }
            if (other.data != null) {
                if (data != null && data.equals(other.data)) {
                    return true;
                }
                if (object != null && other.data.equals(toJson(object))) {
                    return true;
                }
            }
        }
        return false;
    }

    private String toJson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    private static <T> T fromJson(String json, Class klass) {
        Gson gson = new Gson();
        return (T) gson.<T>fromJson(json, klass);
    }
}
