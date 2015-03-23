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
package br.uff.labtempo.osiris.virtualsensornet.model.util;

import br.uff.labtempo.omcp.common.exceptions.BadRequestException;
import br.uff.labtempo.osiris.virtualsensornet.model.DataConverter;
import br.uff.labtempo.osiris.virtualsensornet.model.DataType;
import br.uff.labtempo.osiris.virtualsensornet.model.Field;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Felipe Santos <feliperalph at hotmail.com>
 */
public class FieldUpgradeHelper {

    private boolean isChanged;
    private List<Field> insertedFields;
    private List<Field> removedFields;

    public boolean isChanged() {
        return isChanged;
    }

    public void upgradeFieldList(List<Field> current, List<Field> newest) throws BadRequestException {
        isChanged = false;
        /**
         * current - all fields has id
         *
         * newest - all field not has id
         */
        List<Field> currIntersection = new ArrayList<>();
        List<Field> neweIntersection = new ArrayList<>();
        /**
         * UPDATING cannot change datatype from a initialized field
         *
         * can add converters
         */
        for (Field curr : current) {
            for (Field newe : newest) {
                if (curr.getId() == newe.getId()) {
                    updating(curr, newe);
                    currIntersection.add(curr);
                    neweIntersection.add(newe);
                    break;
                }
            }
        }

        /**
         * DELETION cannot delete a initialized field
         *
         * cannot delete a field that has aggregates
         */
        List<Field> toRemove = new ArrayList<>(current);
        toRemove.removeAll(currIntersection);
        for (Field deleted : toRemove) {
            deletion(current, deleted);
        }
        removedFields = toRemove;

        /**
         * INSERTION
         */
        List<Field> toInsert = new ArrayList<>(newest);
        toInsert.removeAll(neweIntersection);
        current.addAll(toInsert);
        insertedFields = toInsert;

        if (insertedFields.size() > 0 || removedFields.size() > 0) {
            isChanged = true;
        }
        //return removed fields to persists
        //return toRemove;
    }

    private void deletion(List<Field> current, Field deleted) throws BadRequestException {
        //cannot delete Field it was initialized
        if (deleted.isStored()) {
            throw new BadRequestException("You cannot to delete a initialized Field!");
        }
        //cannot delete Field it has aggregates
        if (deleted.hasAggregates()) {
            throw new BadRequestException("You cannot to delete Field because it has aggregates!");
        }
        isChanged = true;
        current.remove(deleted);
    }

    private void updating(Field current, Field newest) throws BadRequestException {
        //check datatype - isStored or has aggregates exeception
        DataType currDataType = current.getDataType();
        DataType newestDataType = newest.getDataType();
        if (currDataType.getId() != newestDataType.getId()) {
            //cannot change datatype if field was initialized
            if (current.isStored()) {
                throw new BadRequestException("You cannot to change DataType of a initialized Field!");
            }
            //cannot change datatype if field has aggregates
            if (current.hasAggregates()) {
                throw new BadRequestException("You cannot to change DataType because Field has aggregates!");
            }

            //check if newest update has converter and if converter is compatible with new datatype 
            if (newest.hasConverter()) {
                DataConverter converter = newest.getConverter();
                if (!converter.getOutputDataType().equals(newestDataType)) {
                    throw new BadRequestException("You cannot set incompatibles converter and datatype!");
                }
            
            //check if current field has converter and if converter is compatible with new datatype     
            } else if (current.hasConverter()) {
                DataConverter converter = current.getConverter();
                if (!converter.getOutputDataType().equals(newestDataType)) {
                    throw new BadRequestException("You cannot set a datatype incompatible with present converter!");
                }
            }

            current.setDataType(newestDataType);
            isChanged = true;
        }

        //check converter
        if (newest.hasConverter()) {
            try {
                current.setConverter(newest.getConverter());
                isChanged = true;
            } catch (Exception ex) {
                throw new BadRequestException(ex.getMessage());
            }
        } else {
            current.removeConverter();
        }
        
        //set new reference name to field
        if(!current.getReferenceName().equals(newest.getReferenceName())){
            current.setReferenceName(newest.getReferenceName());
            isChanged = true;
        }
    }

    public List<Field> getInsertedFields() {
        return insertedFields;
    }

    public List<Field> getRemovedFields() {
        return removedFields;
    }
    //create cascade
    //update cascade(em alguns casos)
    /**
     * nasce vinculado a um virtualsensor deve ser persistido quando um
     * virtualsensor atualiza seus valores quando agregado, na remoção o field
     * precisará ser atualizado de forma independente quando adicionado como
     * agregado para um virtualsensor, a atualização deve ser cascade fields
     * podem ser buscados por suas ids
     *
     * fields so podem ser removidos se não possuirem valores e/ou agregados
     */
}
