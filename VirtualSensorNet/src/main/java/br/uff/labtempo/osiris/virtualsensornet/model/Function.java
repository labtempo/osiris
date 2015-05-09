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

import br.uff.labtempo.osiris.to.common.definitions.FunctionOperation;
import br.uff.labtempo.osiris.to.function.ParamFnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.FunctionVsnTo;
import br.uff.labtempo.osiris.virtualsensornet.model.util.FunctionUtils;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
@Entity
public class Function implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String description;
    private String address;
    
    @ElementCollection
    private List<FunctionParam> requestParams;
    
    @ElementCollection
    private List<FunctionParam> responseParams;
    
    @Enumerated(EnumType.STRING)
    @ElementCollection
    private List<FunctionOperation> operations;

    protected Function() {
    }

    public Function(String name, String description, String address, List<FunctionParam> requestParams, List<FunctionParam> responseParams, List<FunctionOperation> operations) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.requestParams = requestParams;
        this.responseParams = responseParams;
        this.operations = operations;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public List<FunctionParam> getRequestParams() {
        return requestParams;
    }

    public List<FunctionParam> getResponseParams() {
        return responseParams;
    }

    public List<FunctionOperation> getOperations() {
        return operations;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRequestParams(List<FunctionParam> requestParams) {
        this.requestParams = requestParams;
    }

    public void setResponseParams(List<FunctionParam> responseParams) {
        this.responseParams = responseParams;
    }

    public void setOperations(List<FunctionOperation> operations) {
        this.operations = operations;
    }

    public FunctionVsnTo getTransferObject() {
        FunctionUtils utils = new FunctionUtils();
        long id = this.id;
        String name = this.name;
        String description = this.description;
        String address = this.address;
        List<FunctionOperation> operations = this.operations;
        List<ParamFnTo> requestParams = utils.convertFrom(this.requestParams);
        List<ParamFnTo> responseParams = utils.convertFrom(this.responseParams);

        return new FunctionVsnTo(id, name, description, address, operations, requestParams, responseParams);
    }
}
