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
package br.uff.labtempo.osiris.virtualsensornet.controller.internal;

import br.uff.labtempo.omcp.common.exceptions.BadRequestException;
import br.uff.labtempo.omcp.common.exceptions.InternalServerErrorException;
import br.uff.labtempo.osiris.virtualsensornet.model.Aggregatable;
import br.uff.labtempo.osiris.virtualsensornet.model.Field;
import br.uff.labtempo.osiris.virtualsensornet.persistence.DaoFactory;
import br.uff.labtempo.osiris.virtualsensornet.persistence.FieldDao;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class FieldController {

    private final DaoFactory factory;

    public FieldController(DaoFactory factory) {
        this.factory = factory;
    }

    public Field getById(long id) throws InternalServerErrorException {
        try {
            FieldDao fd = factory.getFieldDao();
            return fd.getById(id);
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }
    }

    public List<Field> getAllByIds(List<Long> ids) throws InternalServerErrorException, BadRequestException {
        try {
            List<Field> fields = new ArrayList<>();
            FieldDao fd = factory.getFieldDao();
            for (long id : ids) {
                Field field = fd.getById(id);
                if (field == null) {
                    throw new BadRequestException("Selected Field not found!");
                }
                fields.add(field);
            }
            return fields;
        } catch (BadRequestException bre) {
            throw bre;
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }
    }

    public void merge(Field field) throws InternalServerErrorException {
        try {
            FieldDao fd = factory.getFieldDao();
            fd.update(field);
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }
    }

    public void delete(Field field) throws InternalServerErrorException {
        try {
            FieldDao fd = factory.getFieldDao();
            fd.delete(field);
        } catch (Exception e) {
            throw new InternalServerErrorException("Data query error!");
        }
    }

    public void removeAggregate(Field field, Aggregatable virtualSensor) {
        field.removeAggregate(virtualSensor);
    }

    public void deleteIgnoringInitialization(List<Field> current, Field deleted) throws BadRequestException {
        //cannot delete Field it has aggregates
        if (deleted.hasAggregates()) {
            throw new BadRequestException("You cannot to delete Field because it has aggregates!");
        }
        current.remove(deleted);
    }
}
