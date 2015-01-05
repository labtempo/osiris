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
package br.uff.labtempo.servertest;

import java.util.logging.Level;
import java.util.logging.Logger;
import service.Service;

/**
 * Hello world!
 *
 */
public class App {

    static Server server;

    public static void main(String[] args) {
        server = new Server();
        //shutdown();
        server.start();
    }

    private static void shutdown() {
        Thread t = new Thread() {
            public void run() {
                try {
                    Thread.sleep(6000);
                    try {
                        server.close();
                    } catch (Exception ex) {
                        Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        t.setDaemon(true);
        t.start();

    }
}
