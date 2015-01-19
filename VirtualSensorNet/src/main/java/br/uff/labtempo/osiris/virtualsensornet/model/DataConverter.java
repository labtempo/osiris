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
package br.uff.labtempo.osiris.virtualsensornet.model;

import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import br.uff.labtempo.osiris.to.virtualsensornet.ConverterVsnTo;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.hibernate.annotations.Where;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
@Entity
@Where(clause = "isDeleted = 'false'")
public class DataConverter implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String displayName;

    @ManyToOne
    private DataType inputDataType;

    @ManyToOne
    private DataType outputDataType;

    private String expression;

    private boolean isDeleted;

    protected DataConverter() {
    }

    public DataConverter(String displayName, String expression, DataType inputDataType, DataType outputDataType) {
        this.displayName = displayName;
        this.inputDataType = inputDataType;
        this.outputDataType = outputDataType;
        this.expression = expression;
        checkExpression();
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public void setDisplayName(String name) {
        this.displayName = name;
    }

    public boolean isIsDeleted() {
        return isDeleted;
    }

    public void setLogicallyDeleted() {
        isDeleted = true;
    }

    public void setLogicallyDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String convert(String value) throws ScriptException {
        ScriptEngineManager script = new ScriptEngineManager();
        ScriptEngine jsEngine = script.getEngineByName("javascript");

        switch (outputDataType.getValueType()) {
            case NUMBER:
                if (value.contains(".")) {
                    jsEngine.put("value", Double.valueOf(value));
                } else {
                    jsEngine.put("value", Integer.valueOf(value));
                }
                break;
            case LOGIC:
                jsEngine.put("value", Boolean.valueOf(value));
                break;
            case TEXT:
            default:
                jsEngine.put("value", value);
                break;
        }

        try {
            jsEngine.eval(expression);
        } catch (ScriptException ex) {
            Logger.getLogger(DataConverter.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        String result = String.valueOf(jsEngine.get("value"));

        return result;
    }

    public String getExpression() {
        return expression;
    }

    public long getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public DataType getInputDataType() {
        return inputDataType;
    }

    public DataType getOutputDataType() {
        return outputDataType;
    }

    private void checkExpression() {
        String value = "0";

        try {
            ValueType valueType = inputDataType.getValueType();
            switch (valueType) {
                case NUMBER:
                    value = "5.0";
                    break;
                case LOGIC:
                    value = "true";
                    break;
                case TEXT:
                    value = "hello world";
                    break;
            }
        } catch (Exception e) {
        }

        try {
            convert(value);
        } catch (ScriptException ex) {
            Logger.getLogger(DataConverter.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Expression is not correct! Please enter with a valid Javascript code.");
        }
    }

    public ConverterVsnTo getTransferObject(long usedBy) {
        ConverterVsnTo to = new ConverterVsnTo(id, displayName, expression, inputDataType.getId(), outputDataType.getId());
        to.setUsedBy(usedBy);
        return to;
    }

    public void setOutputDataType(DataType dataType) {
        this.outputDataType = dataType;
    }

    public void setInputDataType(DataType dataType) {
        this.inputDataType = dataType;
    }
}
