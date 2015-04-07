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

import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class ResponseFnTo {

    private final long requestTimestampInMillis;
    private final long responseTimestampInMillis;
    private final List<Map<String, ? extends Object>> values;

    //helper    
    private transient List<ValueFnTo> helperValuesToList;

    public ResponseFnTo(long requestTimestampInMillis, long responseTimestampInMillis, List<ValueFnTo> values) {
        this.requestTimestampInMillis = requestTimestampInMillis;
        this.responseTimestampInMillis = responseTimestampInMillis;
        this.helperValuesToList = values;
        this.values = Converters.valuesToMap(values);
    }

    public List<ValueFnTo> getValues() {
        if(helperValuesToList == null){
            helperValuesToList = Converters.mapToValues(values);
        }
        return helperValuesToList;
    }

    public long getRequestTimestampInMillis() {
        return requestTimestampInMillis;
    }

    public long getResponseTimestampInMillis() {
        return responseTimestampInMillis;
    }

   
}
