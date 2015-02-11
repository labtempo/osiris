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
package br.uff.labtempo.osiris.sensornet.usecase.config;

import br.uff.labtempo.osiris.sensornet.persistence.jpa.JpaDaoFactory;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class PersistenceProvider {

    public static PersistenceProvider instance;
    private Map<String, JpaDaoFactory> providers;

    private PersistenceProvider() {
        providers = new HashMap<>();
    }

    public static JpaDaoFactory getInstance(String name) throws Exception {
        if (instance == null) {
            instance = new PersistenceProvider();
        }
        return instance.getProvider(name);
    }

    public JpaDaoFactory getProvider(String name) throws Exception {
        JpaDaoFactory jdf = providers.get(name);
        if (jdf == null) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory(name);
            jdf = JpaDaoFactory.newInstance(emf);
            providers.put(name, jdf);
        }
        return jdf;
    }

    @Override
    protected void finalize() throws Throwable {
        for (Map.Entry<String, JpaDaoFactory> entrySet : providers.entrySet()) {
            String key = entrySet.getKey();
            JpaDaoFactory value = entrySet.getValue();
            try {
                value.close();
            } catch (Exception e) {
            }
        }
        super.finalize(); //To change body of generated methods, choose Tools | Templates.
    }

}
