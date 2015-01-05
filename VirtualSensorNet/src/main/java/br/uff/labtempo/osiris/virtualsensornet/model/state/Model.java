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
public abstract class Model implements Serializable {

    @Enumerated(EnumType.STRING)
    protected ModelState state;

    @Temporal(TemporalType.TIMESTAMP)
    Calendar lastModified;

    public Model() {
        this.state = ModelState.NEW;
        updateDate();
    }

    public void deactivate() {
        state.deactivate(this);
    }

    public void reactivate() {
        state.reactivate(this);
    }

    protected final void update() {
        if (state == ModelState.INACTIVE) {
            state.reactivate(this);
        } else {
            state.update(this);
        }
    }

    public void malfunction() {
        state.malfunction(this);
    }

    public Calendar getLastModifiedDate() {
        return lastModified;
    }

    public ModelState getModelState() {
        return state;
    }

    void updateDate() {
        lastModified = Calendar.getInstance();
    }

}
