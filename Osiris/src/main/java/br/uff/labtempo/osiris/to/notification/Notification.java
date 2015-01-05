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
package br.uff.labtempo.osiris.to.notification;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
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
