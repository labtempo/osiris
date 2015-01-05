/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.to.notification;

/**
 *
 * @author Felipe
 */
public class Notification {

    private Level level;
    private String uri;
    private String title;
    private String message;
    private String origin;

    public Level getLevel() {
        return level;
    }

    public String getUri() {
        return uri;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getOrigin() {
        return origin;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public enum Level {

        VERBOSE,
        DEBUG,
        INFO,
        WARNING,
        CRITICAL,
        ERROR;

        @Override
        public String toString() {
            return this.name().toLowerCase(); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
