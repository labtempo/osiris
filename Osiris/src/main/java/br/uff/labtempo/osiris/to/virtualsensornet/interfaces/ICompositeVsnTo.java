/*
 * Copyright 2015 Felipe.
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
package br.uff.labtempo.osiris.to.virtualsensornet.interfaces;

import br.uff.labtempo.osiris.to.common.data.FieldTo;
import java.util.List;

/**
 *
 * @author Felipe
 */
public interface ICompositeVsnTo {

    long getId();

    String getLabel();

    List<? extends FieldTo> getBoundFields();

    void bindToField(long id);

    void bindToField(FieldTo fieldTo);

    void addBoundField(long id, String name, long dataTypeId, long converterId, boolean initialized, long sourceId, int aggregates, int dependents);

    //edit
    void setLabel(String label);

    void removeBoundFields();

}
