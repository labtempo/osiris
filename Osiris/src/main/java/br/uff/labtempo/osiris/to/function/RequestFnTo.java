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
public class RequestFnTo {

    private List<Map<String, ? extends Object>> values;

    public RequestFnTo() {
        this.values = new ArrayList<>();
    }

    public void addValue(String name, String value) {
        SingleValueFnTo valueFnTo = new SingleValueFnTo(name, value);
        values.add(valueFnTo.toMap());
    }

    public void addValue(String name, List<String> values) {
        MultiValueFnTo valueFnTo = new MultiValueFnTo(name, values);
        this.values.add(valueFnTo.toMap());
    }

    public List<ValueFnTo> getValues() {
        return Converters.mapToValues(values);
    }

}
