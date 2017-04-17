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
package br.uff.labtempo.osiris.function.min;

import br.uff.labtempo.osiris.function.min.configuration.AppConfig;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * osiris-min-function module
 * Calculates the minimum value of a given virtual sensor values list
 * Application main class
 * @author andre.ghigo
 * @author felipe.ralph
 * @version 1.0
 * @since 1.8
 */
public class App {

    private static Bootstrap boot;

    /**
     * Main function that initialize the application
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Properties properties = readConfig();
        shutdownHook();
        boot = new Bootstrap(properties);
        boot.start();
    }

    /**
     * Creates a thread to run when the process is shutdown
     */
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

    /**
     * Read the configuration from the default config.properties file
     * if the file file is not found, uses the default values from the AppConfig
     * @see AppConfig
     * @see Properties
     * @return Properties with the configurations
     * @throws FileNotFoundException
     */
    private static Properties readConfig() throws FileNotFoundException {
        String propertiesFileName = AppConfig.PROPERTIES_FILENAME;
        File base;
        try {
            base = new File(App.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
            File configFile = new File(base, propertiesFileName);
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
        throw new RuntimeException("Erro to open file " + propertiesFileName + "!");
    }
}
