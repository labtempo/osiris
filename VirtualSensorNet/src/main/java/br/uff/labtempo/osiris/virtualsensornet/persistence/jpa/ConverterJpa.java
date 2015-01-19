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

import br.uff.labtempo.osiris.virtualsensornet.model.DataConverter;
import br.uff.labtempo.osiris.virtualsensornet.model.Field;
import br.uff.labtempo.osiris.virtualsensornet.model.Field_;
import br.uff.labtempo.osiris.virtualsensornet.persistence.ConverterDao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
class ConverterJpa implements ConverterDao {

    private final DataManager data;

    public ConverterJpa(DataManager data) {
        this.data = data;
    }

    @Override
    public List<DataConverter> getAll() {
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<DataConverter> criteriaQuery = cb.createQuery(DataConverter.class);
        Root<DataConverter> root = criteriaQuery.from(DataConverter.class);
        criteriaQuery.select(root);
        List<DataConverter> dataConverters = data.getQuery(criteriaQuery);
        return dataConverters;
    }

    @Override
    public List<DataConverter> getAllDeleted() {
        String sql = "SELECT * FROM dataconverter WHERE isdeleted = true";
        Query query = data.getQquery(sql, DataConverter.class);
        List<DataConverter> dataTypes = query.getResultList();
        return dataTypes;
    }

    @Override
    public DataConverter getById(long id) {
        return data.get(DataConverter.class, id);
    }

    @Override
    public long countUseInField(DataConverter converter) {
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
        Root<Field> root = criteriaQuery.from(Field.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.<DataConverter>get(Field_.converter), converter));
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        criteriaQuery.select(cb.count(root));

        long totalUse = data.getQuerySingle(criteriaQuery);

        return totalUse;
    }

    @Override
    public void save(DataConverter o) {
        data.save(o);
    }

    @Override
    public void update(DataConverter o) {
        data.update(o);
    }

    @Override
    public void delete(DataConverter o) {
        if (countUseInField(o) > 0) {
            //delete logic if is used one
            o.setLogicallyDeleted();
            update(o);
        } else {
            data.delete(o);
        }
    }

}
