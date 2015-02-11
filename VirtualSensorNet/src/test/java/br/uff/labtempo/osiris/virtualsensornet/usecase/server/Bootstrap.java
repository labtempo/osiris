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
package br.uff.labtempo.osiris.virtualsensornet.usecase.server;

import br.uff.labtempo.osiris.virtualsensornet.controller.ConverterController;
import br.uff.labtempo.osiris.virtualsensornet.controller.DataTypeController;
import br.uff.labtempo.osiris.virtualsensornet.controller.NotifyController;
import br.uff.labtempo.osiris.virtualsensornet.controller.VirtualSensorController;
import br.uff.labtempo.osiris.virtualsensornet.controller.VirtualSensorLinkController;
import br.uff.labtempo.osiris.virtualsensornet.persistence.jpa.JpaDaoFactory;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class Bootstrap implements AutoCloseable {

    private final JpaDaoFactory factory;

    private final NotifyController notifyController;
    private final VirtualSensorLinkController linkController;
    private final DataTypeController dataTypeController;
    private final ConverterController converterController;
    private final VirtualSensorController virtualSensorController;

    public Bootstrap() throws Exception {
        String persistenceUnitName = "hsqldb";

        this.factory = PersistenceProvider.getInstance(persistenceUnitName);

        this.notifyController = new NotifyController(factory);
        this.linkController = new VirtualSensorLinkController(factory);
        this.dataTypeController = new DataTypeController(factory);
        this.converterController = new ConverterController(factory);
        this.virtualSensorController = new VirtualSensorController(factory);
    }

    public NotifyController getNotifyController() {
        return notifyController;
    }

    public VirtualSensorLinkController getLinkController() {
        return linkController;
    }

    public DataTypeController getDataTypeController() {
        return dataTypeController;
    }

    public ConverterController getConverterController() {
        return converterController;
    }

    public VirtualSensorController getVirtualSensorController() {
        return virtualSensorController;
    }

    @Override
    public void close() throws Exception {
        //factory.close();
    }
}
