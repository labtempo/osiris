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

import br.uff.labtempo.osiris.to.virtualsensornet.interfaces.IValueVsnTo;
import br.uff.labtempo.osiris.to.common.data.ValueTo;
import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public class ValueVsnTo extends ValueTo implements IValueVsnTo {

    private long id;
    private final transient String ID = "id";

    protected ValueVsnTo(Map<String, String> map) {
        super(map);
        this.id = Long.valueOf(map.get(ID));
    }

    protected ValueVsnTo(long id, String name, ValueType type, String value, String unit, String symbol) {
        super(name, type, value, unit, symbol);
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = super.toMap();
        map.put(ID, String.valueOf(id));
        return map;
    }

}
