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
package br.uff.labtempo.osiris.virtualsensornet.persistence.jpa;

import br.uff.labtempo.osiris.virtualsensornet.model.Field;
import br.uff.labtempo.osiris.virtualsensornet.persistence.FieldDao;

/**
 *
 * @author Felipe
 */
public class FieldJpa implements FieldDao {

    private final DataManager data;

    public FieldJpa(DataManager data) {
        this.data = data;
    }

    @Override
    public Field getById(long id) {
        return data.get(Field.class, id);
    }

    @Override
    public void save(Field o) {
        data.save(o);
    }

    @Override
    public void update(Field o) {
        data.update(o);
    }

    @Override
    public void delete(Field o) {
        data.delete(o);
    }

}
