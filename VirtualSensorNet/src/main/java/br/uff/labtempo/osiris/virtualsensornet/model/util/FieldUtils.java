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

import br.uff.labtempo.osiris.virtualsensornet.model.Field;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Felipe Santos <feliperalph at hotmail.com>
 */
public class FieldUtils {

    public boolean setValuesByReferenceName(List<Field> current, List<Field> newest) {
        boolean isUpdated = false;
        for (Field field : current) {
            for (Field newField : newest) {
                if (field.equalsInputReference(newField)) {
                    field.setValue(newField.getValue());
                    isUpdated = true;
                }
            }
        }
        return isUpdated;
    }

    public List<Field> diffById(List<Field> listA, List<Field> listB) {
        List<Field> cloneA = new ArrayList<>(listA);
        List<Field> cloneB = new ArrayList<>(listB);

        List<Field> intersectionA = new ArrayList<>();

        for (Field a : cloneA) {
            for (Field b : cloneB) {
                if (a.getId() == b.getId()) {
                    intersectionA.add(a);
                    break;
                }
            }
        }

        cloneA.removeAll(intersectionA);
        return cloneA;
    }

    public List<Field> diffByReferenceName(List<Field> listA, List<Field> listB) {
        List<Field> cloneA = new ArrayList<>(listA);
        List<Field> cloneB = new ArrayList<>(listB);

        List<Field> intersectionA = new ArrayList<>();

        for (Field a : cloneA) {
            for (Field b : cloneB) {
                if (a.getReferenceName().equalsIgnoreCase(b.getReferenceName())) {
                    intersectionA.add(a);
                    break;
                }
            }
        }

        cloneA.removeAll(intersectionA);
        return cloneA;
    }

    public List<Field> intersectionByReferenceName(List<Field> listA, List<Field> listB) {
        List<Field> cloneA = new ArrayList<>(listA);
        List<Field> cloneB = new ArrayList<>(listB);

        List<Field> intersectionA = new ArrayList<>();

        for (Field a : cloneA) {
            for (Field b : cloneB) {
                if (a.getReferenceName().equalsIgnoreCase(b.getReferenceName())) {
                    intersectionA.add(a);
                    break;
                }
            }
        }
        return intersectionA;
    }
}
