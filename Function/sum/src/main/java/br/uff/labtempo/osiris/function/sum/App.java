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
package br.uff.labtempo.osiris.function.sum;

import br.uff.labtempo.osiris.function.sum.configuration.AppConfig;

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
 * oiris-sum-function
 * Function module of the OSIRIS Framework that calculates the sum of a given list of virtual sensors values
 * Main application to initialize the application
 * @author Felipe Ralph
 * @author andre.ghigo
 * @version 1.0
 * @see 1.8
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
        String name = AppConfig.PROPERTIES_FILENAME;
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
                properties.setProperty(AppConfig.RABBITMQ_IP_PROPERTIES_KEY, AppConfig.DEFAULT_RABBITMQ_IP);
                properties.setProperty(AppConfig.RABBITMQ_USERNAME_PROPERTIES_KEY, AppConfig.DEFAULT_RABBITMQ_USERNAME);
                properties.setProperty(AppConfig.RABBITMQ_PASSWORD_PROPERTIES_KEY, AppConfig.DEFAULT_RABBITMQ_PASSWORD);

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

                return properties;
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new RuntimeException("Erro to open file " + name + "!");
    }
}
