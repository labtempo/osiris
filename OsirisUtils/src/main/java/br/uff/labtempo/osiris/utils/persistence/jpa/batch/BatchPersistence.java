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
package br.uff.labtempo.osiris.utils.persistence.jpa.batch;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author Felipe
 */
public interface BatchPersistence extends AutoCloseable {

    void delete(Object o);

    <T> T get(Class<T> entityType, Object key);

    <T> T getReference(Class<T> entityType, Object key);

    void save(Object o);

    void update(Object o);

    CriteriaBuilder getCriteriaBuilder();

    <T> List<T> getQuery(CriteriaQuery<T> query);

    <T> T getQuerySingle(CriteriaQuery<T> query);
}
