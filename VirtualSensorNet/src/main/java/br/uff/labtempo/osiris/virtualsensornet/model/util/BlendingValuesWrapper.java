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
package br.uff.labtempo.osiris.virtualsensornet.model.util;

import br.uff.labtempo.osiris.to.function.ValueFnTo;
import br.uff.labtempo.osiris.virtualsensornet.model.util.field.FieldValuesWrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class BlendingValuesWrapper implements FieldValuesWrapper<BlendingValuesWrapper.BlendingValue> {

    private final List<BlendingValue> blendingValues;
    private final long creationTimestampInMillis;
    private final int creationPrecisionInNano;
    private final long acquisitionTimestampInMillis;
    private final long creationInterval;
    private final TimeUnit creationIntervalTimeUnit;

    public BlendingValuesWrapper(List<ValueFnTo> valueFnTos, long creationTimestampInMillis, int creationPrecisionInNano, long acquisitionTimestampInMillis, long creationInterval, TimeUnit creationIntervalTimeUnit) {
        this.blendingValues = new ArrayList<>();
        this.creationTimestampInMillis = creationTimestampInMillis;
        this.creationPrecisionInNano = creationPrecisionInNano;
        this.acquisitionTimestampInMillis = acquisitionTimestampInMillis;
        this.creationInterval = creationInterval;
        this.creationIntervalTimeUnit = creationIntervalTimeUnit;

        for (ValueFnTo valueFnTo : valueFnTos) {
            blendingValues.add(new BlendingValue(valueFnTo));
        }
    }

    @Override
    public long getCreationTimestampInMillis() {
        return creationTimestampInMillis;
    }

    @Override
    public long getAcquisitionTimestampInMillis() {
        return acquisitionTimestampInMillis;
    }

    @Override
    public TimeUnit getCreationIntervalTimeUnit() {
        return creationIntervalTimeUnit;
    }

    @Override
    public int getCreationPrecisionInNano() {
        return creationPrecisionInNano;
    }

    @Override
    public long getCreationInterval() {
        return creationInterval;
    }

    @Override
    public List<BlendingValue> getValues() {
        return blendingValues;
    }

    public class BlendingValue {

        private final ValueFnTo to;

        protected BlendingValue(ValueFnTo to) {
            this.to = to;
        }

        public String getName() {
            return to.getName();
        }

        public String getValue() {
            return to.getValue();
        }
    }
}
