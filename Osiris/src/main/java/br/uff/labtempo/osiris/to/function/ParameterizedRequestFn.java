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
package br.uff.labtempo.osiris.to.function;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class ParameterizedRequestFn {

    private String requestUri;
    private final String EQUALS = "=";
    private final String AND = "&";
    private final String ARGS = "?";

    public ParameterizedRequestFn(String uri, RequestFnTo requestFnTo) {
        List<ValueFnTo> values = requestFnTo.getValues();
        StringBuilder params = new StringBuilder();
        for (ValueFnTo value : values) {
            String paramName = value.getName();
            String paramValue;
            if (value.isCollection()) {
                List<String> tempValues = new ArrayList(value.getValues());
                StringBuilder sb = new StringBuilder();
                sb.append("[");
                sb.append(tempValues.remove(0));
                for (String tempValue : tempValues) {
                    sb.append(",");
                    sb.append(tempValue);
                }
                sb.append("]");
                paramValue = sb.toString();
            } else {
                paramValue = value.getValue();
            }

            if (params.length() > 0) {
                params.append(AND);
            }

            params.append(paramName);
            params.append(EQUALS);
            params.append(paramValue);
        }

        this.requestUri = uri + ARGS + params.toString();

    }

    public String getRequestUri() {
        return requestUri;
    }

    @Override
    public String toString() {
        return getRequestUri();
    }

}
