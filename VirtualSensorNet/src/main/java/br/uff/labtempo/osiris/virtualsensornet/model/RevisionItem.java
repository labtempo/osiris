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

import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import br.uff.labtempo.osiris.to.virtualsensornet.RevisionFieldVsnTo;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
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
@Cacheable(false)
public class RevisionItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String value;
    private String referenceName;
    private String unit;
    private String symbol;
    private ValueType type;
    
    @ManyToOne
    private Revision revision;

    protected RevisionItem() {
    }

    public RevisionItem(Field field, Revision revision) {
        this.value = field.getValue();
        this.referenceName = field.getReferenceName();
        this.unit = field.getUnit();
        this.symbol = field.getSymbol();
        this.type = field.getValueType();

        this.revision = revision;

        field.setStored();
    }

    public String getValue() {
        return value;
    }

    public RevisionFieldVsnTo getTransferObject() {
        //TODO: alterar RevisionFieldVsnTo
        RevisionFieldVsnTo revisionFieldVsnTo = new RevisionFieldVsnTo(referenceName, unit, value, symbol);
        return revisionFieldVsnTo;
    }
}
