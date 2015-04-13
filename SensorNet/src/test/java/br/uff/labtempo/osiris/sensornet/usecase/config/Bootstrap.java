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

import br.uff.labtempo.osiris.sensornet.controller.CollectorController;
import br.uff.labtempo.osiris.sensornet.controller.NetworkController;
import br.uff.labtempo.osiris.sensornet.controller.NotifyController;
import br.uff.labtempo.osiris.sensornet.controller.SensorController;
import br.uff.labtempo.osiris.sensornet.persistence.jpa.JpaDaoFactory;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class Bootstrap implements AutoCloseable {

    private static JpaDaoFactory factory;

    private  static NotifyController notifyController;
    private  static SensorController sensorController;
    private  static CollectorController collectorController;
    private  static NetworkController networkController;
    
    static{
        try {
            //String persistenceUnitName = "postgres";
            String persistenceUnitName = "hsqldb";//error in the delete
            factory = PersistenceProvider.getInstance(persistenceUnitName);
            notifyController = new NotifyController(factory);
            sensorController = new SensorController(factory);
            collectorController = new CollectorController(factory);
            networkController = new NetworkController(factory);
        } catch (Exception ex) {
            Logger.getLogger(Bootstrap.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    public Bootstrap() throws Exception {
        
    }

    public NotifyController getNotifyController() {
        return notifyController;
    }

    public SensorController getSensorController() {
        return sensorController;
    }

    public CollectorController getCollectorController() {
        return collectorController;
    }

    public NetworkController getNetworkController() {
        return networkController;
    }

    

    @Override
    public void close() throws Exception {
        //factory.close();
    }
}
