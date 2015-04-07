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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class SingleValueFnTo extends ValueFnTo<String> {

    public SingleValueFnTo(Map<String, ? extends Object> map) {
        super((Map<String, String>) map);
        String l = getValueObject();
    }

    public SingleValueFnTo(String name, String value) {
        super(name, value);
    }

    @Override
    public boolean isCollection() {
        return false;
    }

    @Override
    public String getValue() {
        return getValueObject();
    }

    @Override
    public List<String> getValues() {
        List<String> list = new ArrayList<>();
        list.add(getValueObject());
        return list;
    }
}
