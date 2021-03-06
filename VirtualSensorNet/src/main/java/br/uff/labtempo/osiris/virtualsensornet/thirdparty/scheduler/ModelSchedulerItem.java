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
package br.uff.labtempo.osiris.virtualsensornet.thirdparty.scheduler;

import br.uff.labtempo.osiris.utils.scheduling.SchedulerItem;
import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensor;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
@Entity
@Table(name = "SCHEDULING_TABLE")
public class ModelSchedulerItem implements Serializable, SchedulerItem {

    @Id
    long id;

    private long virtualSensorId;

    private long timeToNextUpdate;
    private long intervalInMillis;

    private boolean isRemovable;

    public ModelSchedulerItem() {
    }

    public ModelSchedulerItem(VirtualSensor virtualSensor) {
        this.virtualSensorId = virtualSensor.getId();
        setTime(virtualSensor);
    }

    @Override
    public long getObjectId() {
        return virtualSensorId;
    }

    @Override
    public long getTimeToNextUpdate() {
        return timeToNextUpdate;
    }

    @Override
    public void updateTimeToNextUpdate(long estimatedTimeToNextUpdate) {
        this.timeToNextUpdate = estimatedTimeToNextUpdate;
    }

    @Override
    public void updateIntervalInMillis(long intervalInMillis) {
        this.intervalInMillis = intervalInMillis;
    }

    public Long getId() {
        return id;
    }

    public void setTime(VirtualSensor virtualSensor) {
        if (virtualSensor == null) {
            isRemovable = true;
            return;
        }
        this.intervalInMillis = TimeUnit.MILLISECONDS.convert(virtualSensor.getCreationInterval(), virtualSensor.getCreationIntervalTimeUnit());
        this.timeToNextUpdate = virtualSensor.getCreationTimestampInMillis() + intervalInMillis;
    }

    public void setRemoved() {
        setTime(null);
    }

    @Override
    public long getIntervalInMillis() {
        return intervalInMillis / 2;
    }

    @Override
    public boolean isRemovable() {
        return isRemovable;
    }
}
