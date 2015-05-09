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
package br.uff.labtempo.osiris.to.virtualsensornet;

import br.uff.labtempo.osiris.to.virtualsensornet.interfaces.IRevisionVsnTo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class RevisionVsnTo implements IRevisionVsnTo {
    private long creationTimestampInMillis;
    private int creationPrecisionInNano;
    private long acquisitionTimestampInMillis;
    private long storageTimestampInMillis;
    
    private List<Map<String, String>> revisionFields;

    public RevisionVsnTo(long creationTimestampInMillis, int creationPrecisionInNano, long acquisitionTimestampInMillis, long storageTimestampInMillis) {
        this.creationTimestampInMillis = creationTimestampInMillis;
        this.creationPrecisionInNano = creationPrecisionInNano;
        this.acquisitionTimestampInMillis = acquisitionTimestampInMillis;
        this.storageTimestampInMillis = storageTimestampInMillis;

        this.revisionFields = new ArrayList<>();
    }

    @Override
    public long getCreationTimestampInMillis() {
        return creationTimestampInMillis;
    }

    @Override
    public int getCreationPrecisionInNano() {
        return creationPrecisionInNano;
    }

    @Override
    public long getAcquisitionTimestampInMillis() {
        return acquisitionTimestampInMillis;
    }

    @Override
    public long getStorageTimestampInMillis() {
        return storageTimestampInMillis;
    }
    
    @Override
    public void addRevisionField(RevisionFieldVsnTo revisionField){
        revisionFields.add(revisionField.toMap());
    }
    
    @Override
    public List<RevisionFieldVsnTo> getIRevisionFields(){
         List<RevisionFieldVsnTo> fields = new ArrayList<>();
        for (Map<String, String> revisionField : revisionFields) {
            fields.add(new RevisionFieldVsnTo(revisionField));
        }
        return fields;
    }    
}
