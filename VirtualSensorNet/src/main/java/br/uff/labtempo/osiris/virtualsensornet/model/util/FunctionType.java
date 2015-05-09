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
package br.uff.labtempo.osiris.virtualsensornet.model.util;

import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import java.util.Objects;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class FunctionType {

    private final ValueType type;
    private final String unit;
    private final boolean isCollection;

    public FunctionType(ValueType type, String unit, boolean isCollection) {
        this.type = type;
        this.unit = unit;
        this.isCollection = isCollection;
    }

    public ValueType getType() {
        return type;
    }

    public String getUnit() {
        return unit;
    }

    public boolean hasUnit() {
        if (unit == null || unit.isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean isCollection() {
        return isCollection;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.type);
        hash = 83 * hash + Objects.hashCode(this.unit);
        hash = 83 * hash + (this.isCollection ? 1 : 0);
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
        final FunctionType other = (FunctionType) obj;
        if (this.type != other.type) {
            return false;
        }
        if (!Objects.equals(this.unit, other.unit)) {
            return false;
        }
        if (this.isCollection != other.isCollection) {
            return false;
        }
        return true;
    }

}
