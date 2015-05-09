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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class ParameterizedRequestFn {

    private RequestFnTo requestFnTo;
    private final String EQUALS = "=";
    private final String AND = "&";
    private final String QUERY_SYMBOL = "?";
    private final String SEPARATOR = ",";
    private final String START_BRACKET = "[";
    private final String END_BRACKET = "]";

    //TODO: url encode and decode
    //TODO: extract requestFnTo from url
    public ParameterizedRequestFn(RequestFnTo requestFnTo) {
        this.requestFnTo = requestFnTo;
    }

    public ParameterizedRequestFn(String requestQuery) {
        RequestFnTo requestFnTo = new RequestFnTo();
        try {
            String query;
            if (requestQuery.contains(QUERY_SYMBOL)) {
                query = requestQuery.split(Pattern.quote(QUERY_SYMBOL))[1];
            } else {
                query = requestQuery;
            }
            String[] keyValues = query.split(Pattern.quote(AND));
            for (String keyValue : keyValues) {
                String[] temp = keyValue.split(Pattern.quote(EQUALS));
                String key = temp[0];
                String value = temp[1];
                decodeValues(key, value, requestFnTo);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        this.requestFnTo = requestFnTo;

    }

    public ParameterizedRequestFn(Map<String, String> params) {
        RequestFnTo requestFnTo = new RequestFnTo();
        try {
            for (Map.Entry<String, String> entrySet : params.entrySet()) {
                String key = entrySet.getKey();
                String value = entrySet.getValue();
                decodeValues(key, value, requestFnTo);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        this.requestFnTo = requestFnTo;
    }

    public RequestFnTo getRequestFnTo() {
        return requestFnTo;
    }
    
    public String getRequestUri(String uri) {
        try {
            List<ValueFnTo> values = requestFnTo.getValues();
            StringBuilder params = new StringBuilder();
            for (ValueFnTo value : values) {
                String paramName = value.getName();
                String paramValue;
                if (value.isCollection()) {
                    List<String> tempValues = new ArrayList(value.getValues());
                    StringBuilder sb = new StringBuilder();
                    sb.append(START_BRACKET);
                    sb.append(URLEncoder.encode(tempValues.remove(0), "UTF-8"));
                    for (String tempValue : tempValues) {
                        sb.append(SEPARATOR);
                        sb.append(URLEncoder.encode(tempValue, "UTF-8"));
                    }
                    sb.append(END_BRACKET);
                    paramValue = sb.toString();
                } else {
                    paramValue = URLEncoder.encode(value.getValue(), "UTF-8");
                }

                if (params.length() > 0) {
                    params.append(AND);
                }

                params.append(paramName);
                params.append(EQUALS);
                params.append(paramValue);
            }
            return uri + QUERY_SYMBOL + params.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void decodeValues(String key, String value, RequestFnTo requestFnTo) throws UnsupportedEncodingException {
        if (value.contains(START_BRACKET) && value.contains(END_BRACKET)) {
            String[] values = value.replace(START_BRACKET, "").replace(END_BRACKET, "").split(Pattern.quote(SEPARATOR));
            List<String> list = new ArrayList<>();
            for (String v : values) {
                list.add(URLDecoder.decode(v, "UTF-8"));
            }
            requestFnTo.addValue(key, list);
        } else {
            String v = URLDecoder.decode(value, "UTF-8");
            requestFnTo.addValue(key, v);
        }
    }
}
