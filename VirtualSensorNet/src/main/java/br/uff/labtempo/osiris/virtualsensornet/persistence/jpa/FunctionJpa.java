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
package br.uff.labtempo.osiris.virtualsensornet.persistence.jpa;

import br.uff.labtempo.osiris.virtualsensornet.model.Function;
import br.uff.labtempo.osiris.virtualsensornet.persistence.FunctionDao;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class FunctionJpa implements FunctionDao {

    private final DataManager data;

    public FunctionJpa(DataManager data) {
        this.data = data;
    }

    @Override
    public Function getById(long id) {
        return data.get(Function.class, id);
    }

    @Override
    public Function get(Function function) {
        return getById(function.getId());
    }

    @Override
    public List<Function> getAll() {
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<Function> criteriaQuery = cb.createQuery(Function.class);
        Root<Function> root = criteriaQuery.from(Function.class);
        criteriaQuery.select(root);
        List<Function> dataTypes = data.getQuery(criteriaQuery);
        return dataTypes;
    }

    @Override
    public long countUse(Function function) {
        //TODO: implementar count
        return 0;
    }
    
    @Override
    public void save(Function o) {
        data.save(o);
    }

    @Override
    public void update(Function o) {
        data.update(o);
    }

    @Override
    public void delete(Function o) {
        data.delete(o);
    }

}
