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
class Converters {

    static List<Map<String, ? extends Object>> valuesToMap(List<ValueFnTo> list) {
        List<Map<String, ? extends Object>> mapList = new ArrayList<>();
        for (ValueFnTo valueFnTo : list) {
            mapList.add(valueFnTo.toMap());
        }
        return mapList;
    }

    static List<ValueFnTo> mapToValues(List<Map<String, ? extends Object>> mapList) {
        List<ValueFnTo> list = new ArrayList<>();
        for (Map<String, ? extends Object> map : mapList) {
            try {
                SingleValueFnTo svft = new SingleValueFnTo(map);
                list.add(svft);
            } catch (ClassCastException ex) {
                MultiValueFnTo mvft = new MultiValueFnTo(map);
                list.add(mvft);
            }
        }
        return list;
    }
}
