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
import br.uff.labtempo.osiris.to.virtualsensornet.VirtualSensorType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class VirtualSensorTest {

    public VirtualSensorTest() {
        
    }

    @Test
    public void testSomeMethod() {
//        DataType dtc = new DataType("temperature", ValueType.NUMBER, "celsius", "°C");
//        DataType dtk = new DataType("temperature", ValueType.NUMBER, "kelvin", "°K");
//        DataType dtf = new DataType("temperature", ValueType.NUMBER, "fahrenheit", "°F");        
//        DataType dte = new DataType("temperature", ValueType.NUMBER, "extra", "°E");
//        
//        Converter cToK = new Converter("celsiusToKelvin", dtk, "value += 273.15;");
//        Converter cToF = new Converter("celsiusToFahrenheit", dtf, "value = value *1.8000 + 32.00;");
//        
//        Field f1 = new Field("temperature", dtc);
//        f1.setConverter(cToK);
//        Field f2 = new Field("temperature", dtc);
//        
//        List<Field> fs = new ArrayList<>();
//        
//        fs.add(f1);
//        fs.add(f2);
//        
//        DummyVs dv = new DummyVs(fs);
//        
//        Field f3 = new Field("temperature", dtc);
//        f1.setConverter(cToF);
//        Field f4 = new Field("temperature", dte);
        
        //List<Field> fs = new ArrayList<>();
    }

    private class DummyVs extends VirtualSensor {

        public DummyVs(List<Field> fields) {
            super(VirtualSensorType.LINK, fields, 0,TimeUnit.MILLISECONDS);
        }

//        @Override
//        public boolean updateFields(List<Field> newFields) {
//            return super.updateFields(newFields);
//        }

    }
}
