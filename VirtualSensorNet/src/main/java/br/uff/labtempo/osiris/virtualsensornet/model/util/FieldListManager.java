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

import br.uff.labtempo.osiris.virtualsensornet.model.util.field.UpdateFieldListHelper;
import br.uff.labtempo.osiris.virtualsensornet.model.util.field.UpgradeFieldListHelper;
import br.uff.labtempo.osiris.virtualsensornet.model.Field;
import br.uff.labtempo.osiris.virtualsensornet.model.util.field.FieldListHelper;
import java.util.List;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class FieldListManager {

    private boolean processed;
    private final List<Field> newerFields;
    private List<Field> included;
    private List<Field> excluded;
    private List<Field> modified;

    public FieldListManager(List<Field> newerFields) {
        this.newerFields = newerFields;
    }

    public boolean doAddRemove(List<Field> fields) {        
        UpdateFieldListHelper helper = new UpdateFieldListHelper();
        return process(helper, fields);
    }

    public boolean doCreateModifyDelete(List<Field> fields) {
        UpgradeFieldListHelper helper = new UpgradeFieldListHelper();
        return process(helper, fields);
    }

    public List<Field> getIncluded() {
        return included;
    }

    public List<Field> getExcluded() {
        return excluded;
    }

    public List<Field> getModified() {
        return modified;
    }

    public boolean isProcessed() {
        return processed;
    }

    private void clear() {
        included = null;
        excluded = null;
        modified = null;
        processed = false;
    }

    private boolean process(FieldListHelper helper, List<Field> fields) {
        clear();
        helper.process(fields, newerFields);
        included = helper.getInsertedFields();
        excluded = helper.getRemovedFields();
        modified = helper.getModifiedFields();
        processed = true;
        return helper.isChanged();
    }
}
