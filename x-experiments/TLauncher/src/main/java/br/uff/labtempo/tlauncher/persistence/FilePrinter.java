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
package br.uff.labtempo.tlauncher.persistence;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class FilePrinter implements AutoCloseable {

    private final Writer writer;
    public static String newline = System.getProperty("line.separator");

    public FilePrinter(String fileName, File folder, boolean isAppendable) throws IOException {
        this.writer = configWriter(fileName, folder, isAppendable);
    }

    public FilePrinter(String fileName, boolean isAppendable) throws IOException {
        this(fileName, new File("."), isAppendable);
    }

    public FilePrinter(String fileName) throws IOException {
        this(fileName, false);
    }

    public void println(Object x) throws IOException {
        println(x.toString());
    }

    public void print(Object x) throws IOException {
        print(x.toString());
    }

    public void println(long x) throws IOException {
        println(String.valueOf(x));
    }

    public void print(long x) throws IOException {
        print(String.valueOf(x));
    }

    public void println(String x) throws IOException {
        print(x);
        print(newline);
    }

    public void print(String x) throws IOException {
        writer.write(x);
    }

    @Override
    public void close() throws Exception {
        writer.flush();
        writer.close();
    }

    private BufferedWriter configWriter(String fileName, File folder, boolean isAppendable) throws IOException {
        File file = new File(folder, fileName);
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, isAppendable));
        return writer;
    }
}
