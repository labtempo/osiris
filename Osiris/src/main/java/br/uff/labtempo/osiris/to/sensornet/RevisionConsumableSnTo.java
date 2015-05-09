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

import br.uff.labtempo.osiris.to.sensornet.interfaces.IRevisionConsumableSnTo;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class RevisionConsumableSnTo implements IRevisionConsumableSnTo {

    private String name;
    private Integer value;

    private final transient String NAME = "name";
    private final transient String VALUE = "value";

    public RevisionConsumableSnTo(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public RevisionConsumableSnTo(Map<String, Object> map) {
        this.name = (String) map.get(NAME);
        this.value = (Integer) map.get(VALUE);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getValue() {
        return value;
    }

    protected Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(NAME, name);
        map.put(VALUE, value);
        return map;
    }
}
