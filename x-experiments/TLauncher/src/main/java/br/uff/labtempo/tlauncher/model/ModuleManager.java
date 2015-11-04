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
package br.uff.labtempo.tlauncher.model;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class ModuleManager implements Printer, Closeable {

    private boolean running;
    private final Object monitor = new Object();
    private final String JAVA_COMMAND = "java -jar ";
    private final String SERVER_STARTED_MESSAGE = "...Awaiting requests";
    private final ConsoleCommand command;
    private final String path;

    public ModuleManager(String path) {
        this.path = path;
        this.command = new ConsoleCommand(JAVA_COMMAND + path, this);
    }

    public void start() throws Exception {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    command.execute();
                } catch (Exception ex) {
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.isDaemon();
        thread.start();
        running = true;
        synchronized (monitor) {
            while (running) {
                try {
                    this.monitor.wait();
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public ConnectionFactory getConfig() {
        Properties properties = readConfig(path);
        ConnectionFactory factory = new ConnectionFactory(properties);
        return factory;
    }

    public Properties getProperties() {
        Properties properties = readConfig(path);
        return properties;
    }

    @Override
    public void setInputStream(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "CP857"));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            if (SERVER_STARTED_MESSAGE.equals(line)) {
                running = false;
                synchronized (monitor) {
                    monitor.notifyAll();
                }
            }
        }
    }

    @Override
    public void close() throws IOException {
        command.abort();
    }

    private Properties readConfig(String path) {
        String name = "config.properties";
        File base;
        try {
            base = new File(path);
            File configFile = new File(base.getParent(), name);
            Properties properties;
            try (FileInputStream fileInput = new FileInputStream(configFile)) {
                properties = new Properties();
                properties.load(fileInput);
            }
            return properties;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
