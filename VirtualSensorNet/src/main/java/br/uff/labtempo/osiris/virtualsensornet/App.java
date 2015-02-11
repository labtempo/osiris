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
package br.uff.labtempo.osiris.virtualsensornet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class App {

    private static Bootstrap boot;

    public static void main(String[] args) throws Exception {
        Properties properties = readConfig();
        shutdownHook();
        boot = new Bootstrap(properties);
        boot.start();
    }

    private static void shutdownHook() {
        System.out.println("Control + C to terminate");
        final Thread thread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("ShutdownHook is running...");
                thread.setName("Shutdown hook");
                try {
                    boot.close();
                    thread.join();
                } catch (Exception ex) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("ShutdownHook end");
            }
        });
    }

    private static Properties readConfig() throws FileNotFoundException {
        String name = "config.properties";
        File base;
        try {
            base = new File(App.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
            File configFile = new File(base, name);
            if (configFile.exists()) {
                FileInputStream fileInput = new FileInputStream(configFile);
                Properties properties = new Properties();
                properties.load(fileInput);
                fileInput.close();

                return properties;
            } else {
                Properties properties = new Properties();
                properties.setProperty("rabbitmq.server.ip", "192.168.0.7");
                properties.setProperty("rabbitmq.user.name", "admin");
                properties.setProperty("rabbitmq.user.pass", "admin");
                properties.setProperty("postgres.server.ip", "192.168.0.7");                
                properties.setProperty("postgres.server.port", "5432");
                properties.setProperty("postgres.user.name", "postgres");
                properties.setProperty("postgres.user.pass", "postgres");
                properties.setProperty("postgres.server.db", "OsirisVSN");

                FileOutputStream fileOut = new FileOutputStream(configFile);

                Properties tmp = new Properties() {
                    @Override
                    public synchronized Enumeration<Object> keys() {
                        return Collections.enumeration(new TreeSet<Object>(super.keySet()));
                    }
                };

                tmp.putAll(properties);
                tmp.store(fileOut, "Osiris Module Config File");
                fileOut.close();

                throw new RuntimeException("You should configure " + name + " on JAR's folder");
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new RuntimeException("Erro to open file "+name+"!");
    }
}
