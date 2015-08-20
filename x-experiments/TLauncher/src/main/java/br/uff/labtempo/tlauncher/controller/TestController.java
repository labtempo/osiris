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
package br.uff.labtempo.tlauncher.controller;

/**
 *
 * @author Felipe Santos <live.proto at hotmail.com>
 */
public class TestController {

    /**
     * Efficiency Test
     */
    public void latenceTest() {
        /*
         * restart rabbit(pronto)
         ------------------------
         * iniciar virtualsensornet(pronto)
         * criar link
         * criar pasta do teste
         * criar arquivo de teste
         *      {criar service e client}
         * repetir 100x{ 
         *      enviar dados
         *      gravar os tempos em arquivos
         * } 
         */
    }
    public void flowRateTest() {
        /*
         * restart rabbit
         * iniciar virtualsensornet
         * criar link
         * enviar e recuperar dados
         * gravar os tempos em arquivos
         * enviar dados em pontencia de 10, iniciando com 10
         * limite do envio até o tempo de 1 segundo
         */
    }

    /**
     * Effectiveness Test
     */
    public void duplicateDataTest() {
        /*
         * restart rabbit
         * start sensornet
         * start virtualsensornet
         * send dual data and recovery unique data
         * print tuple to file
         */
    }

    public void switchingCollectorsTest() {
    }

}
