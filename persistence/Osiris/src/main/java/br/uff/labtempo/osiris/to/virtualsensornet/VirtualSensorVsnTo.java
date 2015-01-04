/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.to.virtualsensornet;

import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe
 */
public class VirtualSensorVsnTo {

    private String id;

    private String state;

    private long timestamp;

    private List<Map<String, String>> values;

    private String type;
    
    //helper attributes
    private transient List<ValueTo> helperValueToList;

    public List<ValueTo> getValuesTo() {
        if (helperValueToList != null) {
            return helperValueToList;
        }
        List<ValueTo> valuesTo = new ArrayList<>();
        for (Map<String, String> value : values) {
            ValueTo valueTo = new ValueTo(value);
            valuesTo.add(valueTo);
        }
        helperValueToList = valuesTo;
        return valuesTo;
    }

    public class ValueTo extends br.uff.labtempo.osiris.to.common.data.ValueTo {

        public ValueTo(String name, ValueType valueType, String value, String unit, String symbol) {
            super(name, valueType, value, unit, symbol);
        }

        ValueTo(Map<String, String> map) {
            super(map);
        }

        @Override
        public Map<String, String> toMap() {
            return super.toMap();
        }
    }

}
