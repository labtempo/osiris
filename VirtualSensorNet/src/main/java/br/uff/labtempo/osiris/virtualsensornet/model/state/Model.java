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
package br.uff.labtempo.osiris.virtualsensornet.model.state;

import br.uff.labtempo.osiris.virtualsensornet.model.interfaces.IModel;
import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
@MappedSuperclass
public abstract class Model implements IModel {

    @Enumerated(EnumType.STRING)
    protected ModelState modelState;

    @Temporal(TemporalType.TIMESTAMP)
    Calendar lastModified;

    public Model() {
        this.modelState = ModelState.NEW;
        updateDate();
    }

    @Override
    public void deactivate() {
        modelState.deactivate(this);
    }

    @Override
    public void reactivate() {
        modelState.reactivate(this);
    }

    @Override
    public final void update() {
        if (modelState == ModelState.INACTIVE) {
            modelState.reactivate(this);
        } else {
            modelState.update(this);
        }
    }

    @Override
    public void malfunction() {
        modelState.malfunction(this);
    }

    @Override
    public Calendar getLastModifiedDate() {
        return lastModified;
    }

    @Override
    public ModelState getModelState() {
        return modelState;
    }

    void updateDate() {
        lastModified = Calendar.getInstance();
    }

}
