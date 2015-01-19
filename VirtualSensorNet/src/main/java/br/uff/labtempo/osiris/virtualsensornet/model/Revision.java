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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
public class Revision implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private VirtualSensor virtualSensor;

    @OneToMany(mappedBy = "revision", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<RevisionItem> items;

    private long timestamp;

    protected Revision() {
    }

    public Revision(VirtualSensor virtualSensor, List<Field> fields, long timestamp) {
        this.virtualSensor = virtualSensor;
        this.timestamp = timestamp;

        List<RevisionItem> revisionItems = createRevisionItems(fields);
        this.items = revisionItems;
    }

    public List<RevisionItem> getItems() {
        return items;
    }

    public long getTimestamp() {
        return timestamp;
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
}
