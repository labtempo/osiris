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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public abstract class ValueFnTo<T> {

    private final String name;
    private final T value;

    private final transient String NAME = "name";
    private final transient String VALUE = "value";

    public ValueFnTo(Map<String, T> map) {
        this.name = (String) map.get(NAME);
        this.value = (T) map.get(VALUE);
    }

    public ValueFnTo(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public abstract boolean isCollection();

    public abstract String getValue();

    public abstract List<String> getValues();

    public Map<String, ? extends Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(NAME, name);
        map.put(VALUE, value);
        return map;
    }

    protected T getValueObject() {
        return value;
    }
}
