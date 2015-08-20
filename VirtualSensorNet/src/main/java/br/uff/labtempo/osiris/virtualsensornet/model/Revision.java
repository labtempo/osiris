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
package br.uff.labtempo.osiris.virtualsensornet.model;

import br.uff.labtempo.osiris.to.virtualsensornet.RevisionFieldVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.RevisionVsnTo;
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
import javax.persistence.OneToOne;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
@Entity
@Cacheable(false)
public class Revision implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long virtualSensorId;

    @OneToMany(mappedBy = "revision", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<RevisionItem> items;

    private long creationTimestampInMillis;
    private int creationPrecisionInNano;
    private long acquisitionTimestampInMillis;
    private long storageTimestampInMillis;

    protected Revision() {
    }

    public Revision(VirtualSensor virtualSensor) {
        this.virtualSensorId = virtualSensor.getId();
        this.creationTimestampInMillis = virtualSensor.getCreationTimestampInMillis();
        this.creationPrecisionInNano = virtualSensor.getCreationPrecisionInNano();
        this.acquisitionTimestampInMillis = virtualSensor.getAcquisitionTimestampInMillis();

        List<RevisionItem> revisionItems = createRevisionItems(virtualSensor.getFields());
        this.items = revisionItems;
        this.storageTimestampInMillis = System.currentTimeMillis();
    }

    public List<RevisionItem> getItems() {
        return items;
    }

    private List<RevisionItem> createRevisionItems(List<Field> fields) {
        List<RevisionItem> revisionItems = new ArrayList<>();
        for (Field field : fields) {
            if (field.getValue() != null) {
                RevisionItem item = new RevisionItem(field, this);
                revisionItems.add(item);
            }
        }
        return revisionItems;
    }

    public long getCreationTimestampInMillis() {
        return creationTimestampInMillis;
    }

    public long getStorageTimestampInMillis() {
        return storageTimestampInMillis;
    }

    public int getCreationPrecisionInNano() {
        return creationPrecisionInNano;
    }

    public long getAcquisitionTimestampInMillis() {
        return acquisitionTimestampInMillis;
    }

    public RevisionVsnTo getTransferObject() {
        RevisionVsnTo revisionVsnTo = new RevisionVsnTo(creationTimestampInMillis, creationPrecisionInNano, acquisitionTimestampInMillis, storageTimestampInMillis);
        for (RevisionItem item : items) {
            RevisionFieldVsnTo revisionFieldVsnTo = item.getTransferObject();
            revisionVsnTo.addRevisionField(revisionFieldVsnTo);
        }
        return revisionVsnTo;
    }
}
