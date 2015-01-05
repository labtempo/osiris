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
package br.uff.labtempo.osiris.virtualsensornet.persistence.jpa;

import br.uff.labtempo.osiris.virtualsensornet.model.DataType;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DataTypeDao;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class DataTypeJpa implements DataTypeDao {

    private final DataManager data;

    public DataTypeJpa(DataManager data) {
        this.data = data;
    }

    @Override
    public DataType get(long dataTypeId) {
        return data.get(DataType.class, dataTypeId);
    }

    @Override
    public void save(DataType o) {
        data.save(o);
    }

    @Override
    public void update(DataType o) {
        data.update(o);
    }

    @Override
    public void delete(DataType o) {
        data.delete(o);
    }

}
