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
package br.uff.labtempo.osiris.to.virtualsensornet;

import br.uff.labtempo.osiris.to.virtualsensornet.interfaces.IConverterVsnTo;
import java.util.Objects;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class ConverterVsnTo implements IConverterVsnTo {

    private final long id;
    private String displayName;
    private String expression;
    private long inputDataTypeId;
    private long outputDataTypeId;
    private long usedBy;

    public ConverterVsnTo(String displayName, String expression, long inputDataTypeId, long outputDataTypeId) {
        this(0, displayName, expression, inputDataTypeId, outputDataTypeId);
    }

    public ConverterVsnTo(long id, String displayName, String expression, long inputDataTypeId, long outputDataTypeId) {
        this.id = id;
        this.displayName = displayName;
        this.expression = expression;
        this.inputDataTypeId = inputDataTypeId;
        this.outputDataTypeId = outputDataTypeId;
    }

    public void setUsedBy(long usedBy) {
        this.usedBy = usedBy;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getExpression() {
        return expression;
    }

    @Override
    public long getInputDataTypeId() {
        return inputDataTypeId;
    }

    @Override
    public long getOutputDataTypeId() {
        return outputDataTypeId;
    }

    @Override
    public long getUsedBy() {
        return usedBy;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public void setExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public void setInputDataTypeId(long inputDataTypeId) {
        this.inputDataTypeId = inputDataTypeId;
    }

    @Override
    public void setOutputDataTypeId(long outputDataTypeId) {
        this.outputDataTypeId = outputDataTypeId;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.displayName);
        hash = 17 * hash + Objects.hashCode(this.expression);
        hash = 17 * hash + (int) (this.inputDataTypeId ^ (this.inputDataTypeId >>> 32));
        hash = 17 * hash + (int) (this.outputDataTypeId ^ (this.outputDataTypeId >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ConverterVsnTo other = (ConverterVsnTo) obj;
        if (!Objects.equals(this.displayName, other.displayName)) {
            return false;
        }
        if (!Objects.equals(this.expression, other.expression)) {
            return false;
        }
        if (this.inputDataTypeId != other.inputDataTypeId) {
            return false;
        }
        if (this.outputDataTypeId != other.outputDataTypeId) {
            return false;
        }
        return true;
    }
}
