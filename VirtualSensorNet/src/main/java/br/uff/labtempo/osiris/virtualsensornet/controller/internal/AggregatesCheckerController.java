/*
 * Copyright 2015 Felipe Santos <feliperalph at hotmail.com>.
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
package br.uff.labtempo.osiris.virtualsensornet.controller.internal;

import br.uff.labtempo.osiris.virtualsensornet.controller.util.AggregatesChecker;
import br.uff.labtempo.osiris.to.virtualsensornet.VirtualSensorType;
import br.uff.labtempo.osiris.virtualsensornet.model.Field;
import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensor;
import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensorComposite;
import br.uff.labtempo.osiris.virtualsensornet.controller.util.AnnouncerWrapper;
import br.uff.labtempo.osiris.virtualsensornet.model.state.ModelState;
import br.uff.labtempo.osiris.virtualsensornet.persistence.CompositeDao;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.virtualsensornet.thirdparty.announcer.AnnouncerAgent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class AggregatesCheckerController implements AggregatesChecker {

    private final DaoFactory factory;
    private AnnouncerAgent announcer;
    private final ExecutorService executor;

    public AggregatesCheckerController(DaoFactory factory, AnnouncerAgent announcer) {
        this.factory = factory;
        this.announcer = new AnnouncerWrapper(announcer);
        this.executor = Executors.newCachedThreadPool();
    }

    public AggregatesCheckerController(DaoFactory factory) {
        this(factory, null);
    }

    public void setAnnouncerAgent(AnnouncerAgent announcer) {
        this.announcer = announcer;
    }

    @Override
    public void check(VirtualSensor virtualSensor) {
        VirtualSensorType type = virtualSensor.getVirtualSensorType();

        //not checks aggregates if sensor is a composite
        if (type == VirtualSensorType.COMPOSITE) {
            return;
        }

        //get dao
        final CompositeDao compositeDao = factory.getPersistentCompositeDao();

        //get fields
        final List<Field> fields = virtualSensor.getFields();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                //notify changes one time only, by grouping composites into an set 
                Set<VirtualSensor> aggregatables = new HashSet<>();
                for (Field field : fields) {
                    aggregatables.addAll(field.getAggregates());
                }

                if (!aggregatables.isEmpty()) {
                    for (VirtualSensor aggregatable : aggregatables) {
                        VirtualSensor virtualSensor = aggregatable;

                        //check only composite as aggregate
                        if (virtualSensor.getVirtualSensorType() == VirtualSensorType.COMPOSITE) {
                            VirtualSensorComposite composite = (VirtualSensorComposite) virtualSensor;
                            composite.setFieldsValues(null);
                            compositeDao.save(composite);
                            //notify sensor if its state is equals "reactivated", "malfunction" or "inactive"
                            if (ModelState.REACTIVATED.equals(composite.getModelState())) {
                                announcer.notifyReactivation(composite.getTransferObject());
                            } else if (ModelState.MALFUNCTION.equals(composite.getModelState())) {
                                announcer.notifyMalfunction(composite.getTransferObject());
                            } else if (ModelState.INACTIVE.equals(composite.getModelState())) {
                                announcer.notifyDeactivation(composite.getTransferObject());
                            }
                            announcer.broadcastIt(composite.getTransferObject());
                        }
                    }
                }
            }
        };
        executor.execute(runnable);
    }
}
