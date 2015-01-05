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

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
@Entity
public class Converter implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String name;

    @ManyToOne
    private DataType dataType;

    private String expression;

    public Converter() {
    }

    public Converter(String name, DataType dataType, String expression) {
        this.name = name;
        this.dataType = dataType;
        this.expression = expression;
        checkExpression();
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String convert(String value) throws ScriptException {
        ScriptEngineManager script = new ScriptEngineManager();
        ScriptEngine jsEngine = script.getEngineByName("javascript");

        jsEngine.put("value", value);

        try {
            jsEngine.eval(expression);
        } catch (ScriptException ex) {
            Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        String result = String.valueOf(jsEngine.get("value"));

        return result;
    }

    public DataType getDataType() {
        return dataType;
    }

    public String getExpression() {
        return expression;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    private void checkExpression() {
        String value = "0";

        try {
            ValueType valueType = dataType.getValueType();
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
            Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Expression is not correct! Please enter with a Javascript code.");
        }
    }

}
