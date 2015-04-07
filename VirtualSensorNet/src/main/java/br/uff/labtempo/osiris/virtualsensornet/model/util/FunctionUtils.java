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

import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import br.uff.labtempo.osiris.to.function.ParamFnTo;
import br.uff.labtempo.osiris.to.function.ParamTypeFnTo;
import br.uff.labtempo.osiris.virtualsensornet.model.FunctionParam;
import br.uff.labtempo.osiris.virtualsensornet.model.FunctionType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class FunctionUtils {

    public List<FunctionParam> convertTo(List<ParamFnTo> paramFnTos) {
        List<FunctionParam> functionParams = new ArrayList<>();
        for (ParamFnTo paramFnTo : paramFnTos) {
            FunctionParam functionParam = convertTo(paramFnTo);
            functionParams.add(functionParam);
        }
        return functionParams;
    }

    public FunctionParam convertTo(ParamFnTo fnTo) {
        String name = fnTo.getName();
        ParamTypeFnTo paramTypeFnTo = fnTo.getType();
        FunctionType functionType = convertTo(paramTypeFnTo);
        FunctionParam functionParam = new FunctionParam(name, functionType);
        return functionParam;
    }

    public FunctionType convertTo(ParamTypeFnTo paramTypeFnTo) {
        String unit = paramTypeFnTo.getUnit();
        ValueType type = paramTypeFnTo.getType();
        boolean isCollection = paramTypeFnTo.isCollection();
        FunctionType functionType = new FunctionType(type, unit, isCollection);
        return functionType;
    }

    public List<ParamFnTo> convertFrom(List<FunctionParam> paramFnTos) {
        List<ParamFnTo> functionParams = new ArrayList<>();
        for (FunctionParam paramFnTo : paramFnTos) {
            ParamFnTo functionParam = convertFrom(paramFnTo);
            functionParams.add(functionParam);
        }
        return functionParams;
    }

    public ParamFnTo convertFrom(FunctionParam functionParam) {
        String name = functionParam.getName();
        FunctionType functionType = functionParam.getType();
        ParamTypeFnTo paramTypeFnTo = convertFrom(functionType);
        ParamFnTo paramFnTo = new ParamFnTo(name, paramTypeFnTo);
        return paramFnTo;
    }

    public ParamTypeFnTo convertFrom(FunctionType functionType) {
        String unit = functionType.getUnit();
        ValueType type = functionType.getType();
        boolean isCollection = functionType.isCollection();
        ParamTypeFnTo paramTypeFnTo = new ParamTypeFnTo(unit, type, isCollection);
        return paramTypeFnTo;
    }
}
