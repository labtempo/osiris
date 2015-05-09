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

import br.uff.labtempo.osiris.to.sensornet.RevisionValueSnTo;
import br.uff.labtempo.osiris.to.sensornet.RevisionSnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.RevisionFieldVsnTo;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
@Entity
@Cacheable
public class RevisionFieldItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private Field field;
    private String fieldValue;

    @ManyToOne
    private Revision revision;

    protected RevisionFieldItem() {
    }

    public RevisionFieldItem(Field value, Revision revision) {
        this.field = value;
        this.fieldValue = value.getValue();
        this.revision = revision;
    }

    public Field getField() {
        return field;
    }

    public String getValue() {
        return fieldValue;
    }
    
    public RevisionValueSnTo getTransferObject() {
        RevisionValueSnTo revisionFieldSnTo = new RevisionValueSnTo(field.getName(), field.getUnit(),fieldValue);
        return revisionFieldSnTo;
    }
}
