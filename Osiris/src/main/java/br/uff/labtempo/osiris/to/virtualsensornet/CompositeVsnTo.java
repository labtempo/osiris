/*
 * Copyright 2015 Felipe.
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
package br.uff.labtempo.osiris.to.virtualsensornet;

import br.uff.labtempo.omcp.common.utils.DateUtil;
import br.uff.labtempo.osiris.to.common.data.FieldTo;
import br.uff.labtempo.osiris.to.virtualsensornet.interfaces.ICompositeVsnTo;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public class CompositeVsnTo implements ICompositeVsnTo {

    private final long id;
    private String label;
    private List<Map<String, String>> fields;

    //helper attributes
    private transient List<? extends FieldTo> helperFieldToList;

    public CompositeVsnTo(long id, String label) {
        this.id = id;
        this.label = label;
        this.fields = new ArrayList<>();
    }

    public CompositeVsnTo(String label) {
        this(0, label);
    }

    public CompositeVsnTo() {
        this("");
        DateUtil dateUtil = new DateUtil();
        this.label = "composite-" + dateUtil.generate(Calendar.getInstance());
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public void addBoundField(long id, String name, long dataTypeId, long converterId, boolean initialized, long sourceId, int aggregates, int dependents) {
        InternalFieldTo fieldTo = new InternalFieldTo(id, name, dataTypeId, converterId, initialized, sourceId, aggregates, dependents);
        createField(fieldTo);
    }

    @Override
    public void bindToField(long id) {
        InternalFieldTo fieldTo = new InternalFieldTo(id);
        createField(fieldTo);
    }

    @Override
    public void bindToField(FieldTo fieldTo) {
        bindToField(fieldTo.getId());
    }

    @Override
    public List<? extends FieldTo> getBoundFields() {
        if (helperFieldToList != null) {
            return helperFieldToList;
        }
        List<InternalFieldTo> fieldTos = new ArrayList<>();
        for (Map<String, String> field : fields) {
            fieldTos.add(new InternalFieldTo(field));
        }
        helperFieldToList = fieldTos;
        return fieldTos;
    }

    private void createField(InternalFieldTo fieldTo) {
        fields.add(fieldTo.toMap());
    }

    @Override
    public void removeBoundFields() {
        fields.clear();
    }

    private class InternalFieldTo extends FieldTo {

        InternalFieldTo(Map<String, String> map) {
            super(map);
        }

        InternalFieldTo(long id) {
            super(id, null, 0, 0);
        }

        InternalFieldTo(long id, String name, long dataTypeId, long converterId, boolean initialized, long sourceId, int aggregates, int dependents) {
            super(id, name, dataTypeId, converterId, initialized, sourceId, aggregates, dependents);
        }

        @Override
        protected Map<String, String> toMap() {
            return super.toMap(); //To change body of generated methods, choose Tools | Templates.
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CompositeVsnTo other = (CompositeVsnTo) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
