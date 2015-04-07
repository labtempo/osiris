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
package br.uff.labtempo.osiris.virtualsensornet.model.util.field;

import br.uff.labtempo.osiris.virtualsensornet.model.Field;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class UpdateFieldListHelper implements FieldListHelper {

    private boolean isChanged;
    private List<Field> insertedFields;
    private List<Field> removedFields;

    @Override
    public boolean isChanged() {
        return isChanged;
    }

    @Override
    public List<Field> getInsertedFields() {
        return insertedFields;
    }

    @Override
    public List<Field> getRemovedFields() {
        return removedFields;
    }

    @Override
    public List<Field> getModifiedFields() {
        return null;
    }

    @Override
    public void process(List<Field> current, List<Field> newest) {
        isChanged = false;
        /**
         * current - all fields has id
         *
         * newest - all field not has id
         */
        List<Field> currIntersection = new ArrayList<>();
        List<Field> neweIntersection = new ArrayList<>();
        /**
         * SCANNING cannot change datatype from a initialized field
         *
         * can add converters
         */
        for (Field curr : current) {
            for (Field newe : newest) {
                if (curr.getId() == newe.getId()) {
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
    }

    private void deletion(List<Field> current, Field deleted) {
        isChanged = true;
        current.remove(deleted);
    }

}
