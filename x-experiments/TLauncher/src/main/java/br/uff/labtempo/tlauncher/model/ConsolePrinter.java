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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class ConsolePrinter implements Printer {

    private String charset;

    public ConsolePrinter() {
        this.charset = "CP857";
        //"CP857"
    }

    public ConsolePrinter(String charset) {
        this.charset = charset;
    }

    @Override
    public void setInputStream(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }
}
