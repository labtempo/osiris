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
package br.uff.labtempo.osiris.sensornet.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
@Entity
@Cacheable
public class Revision implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private Sensor sensor;

    @OneToMany(mappedBy = "revision", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<RevisionConsumableItem> consumableItems;

    @OneToMany(mappedBy = "revision", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<RevisionFieldItem> fieldItems;

    private long captureTimestampInMillis;
    private int capturePrecisionInNano;
    private long acquisitionTimestampInMillis;
    private long storageTimestampInMillis;

    protected Revision() {
    }

    public Revision(Sensor sensor) {
        this.sensor = sensor;
        this.captureTimestampInMillis = sensor.getCaptureTimestampInMillis();
        this.capturePrecisionInNano = sensor.getCapturePrecisionInNano();
        this.acquisitionTimestampInMillis = sensor.getAcquisitionTimestampInMillis();

        List<RevisionFieldItem> revisionFieldItems = createRevisionFieldItems(sensor.getFields());
        this.fieldItems = revisionFieldItems;

        List<RevisionConsumableItem> revisionConsumableItems = createRevisionConsumableItems(sensor.getConsumables());
        this.consumableItems = revisionConsumableItems;
        this.storageTimestampInMillis = System.currentTimeMillis();
    }

    public List<RevisionConsumableItem> getConsumableItems() {
        return consumableItems;
    }

    public List<RevisionFieldItem> getFieldItems() {
        return fieldItems;
    }

    private List<RevisionFieldItem> createRevisionFieldItems(List<Field> fields) {
        List<RevisionFieldItem> revisionItems = new ArrayList<>();
        for (Field field : fields) {
            if (field.getValue() != null) {
                RevisionFieldItem item = new RevisionFieldItem(field, this);
                revisionItems.add(item);
            }
        }
        return revisionItems;
    }

    private List<RevisionConsumableItem> createRevisionConsumableItems(List<Consumable> consumables) {
        List<RevisionConsumableItem> revisionItems = new ArrayList<>();
        for (Consumable consumable : consumables) {
            RevisionConsumableItem item = new RevisionConsumableItem(consumable, this);
            revisionItems.add(item);
        }
        return revisionItems;
    }

    public long getCaptureTimestampInMillis() {
        return captureTimestampInMillis;
    }

    public long getStorageTimestampInMillis() {
        return storageTimestampInMillis;
    }

    public int getCapturePrecisionInNano() {
        return capturePrecisionInNano;
    }

    public long getAcquisitionTimestampInMillis() {
        return acquisitionTimestampInMillis;
    }

    public long getId() {
        return id;
    }

    public Sensor getSensor() {
        return sensor;
    }
}
