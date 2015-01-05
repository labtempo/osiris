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
package br.uff.labtempo.osiris.collector.api;

import java.util.Properties;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class CollectorApi {
    private long collctionInterval;
    private String id;
    private Properties properties;
    
    public CollectorApi(String id, long collctionInterval) {
        this.id = id;
        this.collctionInterval = collctionInterval;
        this.properties = new Properties();        
    }
    
    public void addInfo(String name, String description){
        properties.setProperty(name, description);
    }
    
    public void removeInfo(String name){
        properties.remove(name);
    }
            
}
