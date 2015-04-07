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

import br.uff.labtempo.osiris.to.function.interfaces.IInterfaceFnTo;
import br.uff.labtempo.osiris.to.common.definitions.FunctionOperation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class InterfaceFnTo implements IInterfaceFnTo {

    private final String name;
    private final String description;
    private final String address;

    private final List<String> operations;
    private final List<Map<String, String>> requestParams;
    private final List<Map<String, String>> responseParams;

    //helpers
    private transient List<FunctionOperation> helperOperationsToList;
    private transient List<ParamFnTo> helperRequestParamsToList;
    private transient List<ParamFnTo> helperResponseParamsToList;

    public InterfaceFnTo(String name, String description, String address, List<FunctionOperation> operations, List<ParamFnTo> requestParams, List<ParamFnTo> responseParams) {
        this.name = name;
        this.description = description;
        this.address = address;

        this.operations = convertOperations(operations);
        this.requestParams = convertParams(requestParams);
        this.responseParams = convertParams(responseParams);

        this.helperOperationsToList = operations;
        this.helperRequestParamsToList = requestParams;
        this.helperResponseParamsToList = responseParams;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public List<FunctionOperation> getOperations() {
        if (helperOperationsToList == null) {
            helperOperationsToList = revertOperations(operations);
        }
        return helperOperationsToList;
    }

    @Override
    public List<ParamFnTo> getRequestParams() {
        if (helperRequestParamsToList == null) {
            helperRequestParamsToList = revertParams(requestParams);
        }
        return helperRequestParamsToList;
    }

    @Override
    public List<ParamFnTo> getResponseParams() {
        if (helperResponseParamsToList == null) {
            helperResponseParamsToList = revertParams(responseParams);
        }
        return helperResponseParamsToList;
    }

    private List<String> convertOperations(List<FunctionOperation> list) {
        List<String> l = new ArrayList<>();
        for (FunctionOperation operation : list) {
            l.add(operation.toString());
        }
        return l;
    }

    private List<FunctionOperation> revertOperations(List<String> list) {
        List<FunctionOperation> l = new ArrayList<>();
        for (String s : list) {
            l.add(FunctionOperation.getByString(name));
        }
        return l;
    }

    private List<Map<String, String>> convertParams(List<ParamFnTo> list) {
        List<Map<String, String>> l = new ArrayList<>();
        for (ParamFnTo fnTo : list) {
            l.add(fnTo.toMap());
        }
        return l;
    }

    private List<ParamFnTo> revertParams(List<Map<String, String>> list) {
        List<ParamFnTo> l = new ArrayList<>();
        for (Map<String, String> map : list) {
            l.add(new ParamFnTo(map));
        }
        return l;
    }
}
