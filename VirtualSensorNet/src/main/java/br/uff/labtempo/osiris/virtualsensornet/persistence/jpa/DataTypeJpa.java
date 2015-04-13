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
import br.uff.labtempo.osiris.virtualsensornet.model.DataConverter_;
import br.uff.labtempo.osiris.virtualsensornet.model.DataType;
import br.uff.labtempo.osiris.virtualsensornet.model.Field;
import br.uff.labtempo.osiris.virtualsensornet.model.Field_;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DataTypeDao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
    public List<DataType> getAll() {
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<DataType> criteriaQuery = cb.createQuery(DataType.class);
        Root<DataType> root = criteriaQuery.from(DataType.class);
        criteriaQuery.select(root);
        List<DataType> dataTypes = data.getQuery(criteriaQuery);
        return dataTypes;
    }

    @Override
    public List<DataType> getAllDeleted() {
        String sql = "SELECT * FROM datatype WHERE isdeleted = true";
        Query query = data.getQquery(sql, DataType.class);
        List<DataType> dataTypes = query.getResultList();
        return dataTypes;
    }

    @Override
    public long countDirectUseInFields(DataType dataType) {
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
        Root<Field> root = criteriaQuery.from(Field.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.<DataType>get(Field_.dataType), dataType));
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        criteriaQuery.select(cb.count(root));

        long totalUseAsDataType = data.getQuerySingle(criteriaQuery);

        return totalUseAsDataType;
    }

    @Override
    public long countIndirectUseInFields(DataType dataType) {
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
        Root<Field> root = criteriaQuery.from(Field.class);
        criteriaQuery.select(cb.count(root));

        Join<Field, DataConverter> convertRoot = root.join(Field_.converter, JoinType.LEFT);

        Predicate dt = cb.equal(root.<DataType>get(Field_.dataType), dataType);
        Predicate in = cb.equal(convertRoot.<DataType>get(DataConverter_.inputDataType), dataType);
        Predicate out = cb.equal(convertRoot.<DataType>get(DataConverter_.outputDataType), dataType);

        criteriaQuery.where(cb.or(dt, cb.or(in, out)));

        long totalUseAsDataType = data.getQuerySingle(criteriaQuery);

        return totalUseAsDataType;
    }

    @Override
    public long countUseInConverters(DataType dataType) {
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
        Root<DataConverter> root = criteriaQuery.from(DataConverter.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.<DataType>get(DataConverter_.inputDataType), dataType));
        predicates.add(cb.equal(root.<DataType>get(DataConverter_.outputDataType), dataType));
        criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        criteriaQuery.select(cb.count(root));

        long totalUse = data.getQuerySingle(criteriaQuery);

        return totalUse;
    }

    @Override
    public DataType getById(long dataTypeId) {
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
        if (countIndirectUseInFields(o) > 0 || countUseInConverters(o) > 0) {
            //delete logic if is used one
            o.setLogicallyDeleted();
            update(o);
        } else {
            data.delete(o);
        }
    }
}
