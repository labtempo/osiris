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
package br.uff.labtempo.osiris.sensornet.model.state;

import java.io.Serializable;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.persistence.ElementCollection;
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
    protected ModelState modelState;

    @Temporal(TemporalType.TIMESTAMP)
    Calendar lastModified;

    //@ManyToOne(cascade = CascadeType.ALL)
    @ElementCollection
    private Map<String, String> info;

    public Model() {
        this.info = new LinkedHashMap<>();
        this.modelState = ModelState.NEW;
        updateDate();
    }

    public void deactivate() {
        modelState.deactivate(this);
    }

    public void reactivate() {
        modelState.reactivate(this);
    }

    public final void update() {
        if (modelState == ModelState.INACTIVE) {
            modelState.reactivate(this);
        } else {
            modelState.update(this);
        }
    }

    public void malfunction() {
        modelState.malfunction(this);
    }

    public Calendar getLastModifiedDate() {
        return lastModified;
    }

    public ModelState getModelState() {
        return modelState;
    }

    public Map<String, String> getInfo() {
        return info;
    }

    public void setInfo(Map<String, String> info) {
        this.info = info;
    }

    void updateDate() {
        lastModified = Calendar.getInstance();
    }

}
