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
package br.uff.labtempo.osiris.to.virtualsensornet;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class BlendingBondVsnTo {

    private final long fieldId;
    private final String paramName;

    private final transient String PARAM_NAME = "paramName";
    private final transient String FIELD_ID = "fieldId";

    protected BlendingBondVsnTo(Map<String, String> map) {
        this.paramName = map.get(PARAM_NAME);
        this.fieldId = Long.parseLong(map.get(FIELD_ID));
    }

    public BlendingBondVsnTo(String paramName, long fieldId) {
        this.paramName = paramName;
        this.fieldId = fieldId;
    }

    public String getParamName() {
        return paramName;
    }

    public long getFieldId() {
        return fieldId;
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put(FIELD_ID, String.valueOf(fieldId));
        map.put(PARAM_NAME, paramName);
        return map;
    }
}
