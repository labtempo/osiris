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
package br.uff.labtempo.osiris.to.function;

import br.uff.labtempo.osiris.to.function.interfaces.IParamTypeFnTo;
import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class ParamTypeFnTo implements IParamTypeFnTo {

    private final ValueType type;
    private final String unit;
    private final boolean collection;

    private final transient String TYPE = "type";
    private final transient String UNIT = "unit";
    private final transient String COLLECTION = "collection";

    protected ParamTypeFnTo(Map<String, String> map) {
        this.type = ValueType.getByCode(map.get(TYPE));
        this.unit = map.get(UNIT);
        this.collection = Boolean.getBoolean(map.get(COLLECTION));
    }

    public ParamTypeFnTo(String unit, ValueType type, boolean collection) {
        this.unit = unit;
        this.type = type;
        this.collection = collection;
    }

    public ParamTypeFnTo(String unit, ValueType type) {
        this(unit, type, false);
    }

    @Override
    public String getUnit() {
        return unit;
    }

    @Override
    public ValueType getType() {
        return type;
    }

    @Override
    public boolean isCollection() {
        return collection;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put(TYPE, type.toString());
        map.put(UNIT, unit);
        map.put(COLLECTION, String.valueOf(collection));
        return map;
    }
}
