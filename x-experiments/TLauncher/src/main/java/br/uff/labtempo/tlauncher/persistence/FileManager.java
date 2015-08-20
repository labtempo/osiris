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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class FileManager {

    public File getFile(String address) {
        File file = new File(address);
        if (file.exists()) {
            return file;
        }
        return null;
    }

    public File createFolder(String address) {
        File root = new File(".");
        File folder = createFolder(root, address);
        return folder;
    }
    public File createFolder(File file, String address) {
        return createFolder(file, address, false);
    }

    public File createFolder(File file, String address, boolean deleteOlder) {
        File folder = new File(file, address);
        if (!folder.exists()) {
            if (!folder.mkdir()) {
                return null;
            }
        }else{
            if(deleteOlder){
                try {
                    FileUtils.deleteDirectory(folder);
                    createFolder(file, address);
                } catch (IOException ex) {
                    Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return folder;
    }

    public FilePrinter getFilePrinter(String fileName, File folder, boolean isAppendable) throws IOException {
        FilePrinter printer = new FilePrinter(fileName, folder, isAppendable);
        return printer;
    }

    public String getTimestampedFileName(String prefix) {
        String fileName = new SimpleDateFormat("'" + prefix + "_'yyyyMMdd-HHmm").format(new Date());
        return fileName;
    }
}
