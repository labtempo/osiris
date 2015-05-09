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
package br.uff.labtempo.osiris.to.sensornet;

import br.uff.labtempo.osiris.to.sensornet.interfaces.IRevisionValueSnTo;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class RevisionValueSnTo implements IRevisionValueSnTo {

    private String name;
    private String unit;
    private String value;

    private final transient String NAME = "name";
    private final transient String UNIT = "unit";
    private final transient String VALUE = "value";

    public RevisionValueSnTo(String name, String unit, String value) {
        this.name = name;
        this.unit = unit;
        this.value = value;
    }

    public RevisionValueSnTo(Map<String, String> map) {
        this.name = map.get(NAME);
        this.unit = map.get(UNIT);
        this.value = map.get(VALUE);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUnit() {
        return unit;
    }

    @Override
    public String getValue() {
        return value;
    }

    protected Map<String, String> toMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put(NAME, name);
        map.put(UNIT, unit);
        map.put(VALUE, value);
        return map;
    }
}
