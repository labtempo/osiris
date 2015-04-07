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
package br.uff.labtempo.osiris.virtualsensornet.model.interfaces;

import br.uff.labtempo.osiris.to.common.definitions.FunctionOperation;
import br.uff.labtempo.osiris.to.virtualsensornet.BlendingVsnTo;
import br.uff.labtempo.osiris.virtualsensornet.model.BlendingBond;
import br.uff.labtempo.osiris.virtualsensornet.model.Function;
import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensor;
import java.util.List;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public interface IBlending {

    long getCallIntervalInMillis();

    FunctionOperation getCallMode();

    Function getFunction();

    List<BlendingBond> getRequestFields();

    List<BlendingBond> getResponseFields();

    BlendingVsnTo getUniqueTransferObject();

    VirtualSensor getVirtualSensor();

    boolean isAggregated();

    //changeable
    void removeFunction();

    //changeable
    void setCallIntervalInMillis(long callIntervalInMillis);

    //changeable
    void setCallMode(FunctionOperation callMode);

    //changeable
    void setFunction(Function function, FunctionOperation mode);

    //changeable
    void setFunction(Function function);

    //updateable
    void setRequestFields(List<BlendingBond> requestFields);

    //updateable
    void setResponseFields(List<BlendingBond> responseFields);
    
}
