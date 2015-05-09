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
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.Cascade;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
@Entity
public class RevisionItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @Cascade({org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE})
    private Field field;
    private String fieldValue;

    @ManyToOne
    private Revision revision;

    protected RevisionItem() {
    }

    public RevisionItem(Field field, Revision revision) {
        this.field = field;
        this.fieldValue = field.getValue();
        this.revision = revision;

        field.setStored();
    }

    public Field getField() {
        return field;
    }

    public String getValue() {
        return fieldValue;
    }

    public RevisionFieldVsnTo getTransferObject() {
        RevisionFieldVsnTo revisionFieldVsnTo = new RevisionFieldVsnTo(field.getReferenceName(), field.getUnit(),fieldValue, field.getId());
        return revisionFieldVsnTo;
    }
}
