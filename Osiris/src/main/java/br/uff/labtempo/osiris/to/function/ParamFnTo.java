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

import br.uff.labtempo.osiris.to.function.interfaces.IParamFnTo;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class ParamFnTo implements IParamFnTo {

    private final String name;
    private final ParamTypeFnTo type;

    private final transient String NAME = "name";

    protected ParamFnTo(Map<String, String> map) {
        this.name = map.get(NAME);
        this.type = new ParamTypeFnTo(map);
    }
    
    public ParamFnTo(String name, ParamTypeFnTo type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ParamTypeFnTo getType() {
        return type;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = type.toMap();
        map.put(NAME, name);
        return map;
    }
}
