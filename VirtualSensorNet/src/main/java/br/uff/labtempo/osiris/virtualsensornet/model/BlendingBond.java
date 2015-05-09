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
package br.uff.labtempo.osiris.virtualsensornet.model;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
@Embeddable
public class BlendingBond implements Serializable {

    private String name;
    @ManyToOne
    private Field field;

    protected BlendingBond() {
    }

    public BlendingBond(String name, Field field) {
        this.name = name;
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public Field getField() {
        return field;
    }
    
    public void canRequest(){
        
    }
}
