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
package br.uff.labtempo.osiris.sensornet;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class App {

    private static Bootstrap boot;

    public static void main(String[] args) throws Exception {
        shutdownHook();
        boot = new Bootstrap();
        boot.start();
    }

    /**
     * Eventos
     *
     * created * nova rede * novo sensor na rede * rede reativada(retornando do
     * estado inativo) * sensor reativado(retornando do estado de inativo)
     *
     * modified * rede atualizada(metadados) * sensor atualizado(datos +
     * metadados) * sensor bateria baixa(extra - regra de negócio do sensornet)
     *
     * deleted * sensor inativo(estrapolou o intervalo de captura) * rede
     * inativa(sem nenhum sensor ativo)
     *
     */
    /**
     * Estados dos sensores
     *
     * * novo * atualizado * inativo * reativado
     *
     * novo -> atualizado -> inativo -> reativado -> atualizado novo -> inativo
     * -> reativado -> atualizado
     *
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
}
