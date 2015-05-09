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
package br.uff.labtempo.osiris.virtualsensornet.model;

import br.uff.labtempo.osiris.virtualsensornet.model.util.FunctionType;
import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
@Embeddable
public class FunctionParam implements Serializable {

    private String name;
    @Enumerated(EnumType.STRING)
    private ValueType type;
    private String unit;
    private boolean isCollection;

    protected FunctionParam() {
    }

    public FunctionParam(String name, FunctionType functionType) {
        this.name = name;
        this.type = functionType.getType();
        this.unit = functionType.getUnit();
        this.isCollection = functionType.isCollection();
    }

    public String getName() {

        return name;
    }

    public FunctionType getType() {
        FunctionType functionType = new FunctionType(type, unit, isCollection);
        return functionType;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.name);
        hash = 67 * hash + Objects.hashCode(this.type);
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
        final FunctionParam other = (FunctionParam) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        return true;
    }

}
