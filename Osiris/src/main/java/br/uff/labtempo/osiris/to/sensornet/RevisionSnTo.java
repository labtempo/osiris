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
package br.uff.labtempo.osiris.to.sensornet;

import br.uff.labtempo.osiris.to.sensornet.interfaces.IRevisionSnTo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class RevisionSnTo implements IRevisionSnTo {

    private long captureTimestampInMillis;
    private int capturePrecisionInNano;
    private long acquisitionTimestampInMillis;
    private long storageTimestampInMillis;

    private List<Map<String, String>> revisionValues;
    private List<Map<String, Object>> revisionConsumables;

    public RevisionSnTo(long captureTimestampInMillis, int capturePrecisionInNano, long acquisitionTimestampInMillis, long storageTimestampInMillis) {
        this.captureTimestampInMillis = captureTimestampInMillis;
        this.capturePrecisionInNano = capturePrecisionInNano;
        this.acquisitionTimestampInMillis = acquisitionTimestampInMillis;
        this.storageTimestampInMillis = storageTimestampInMillis;

        this.revisionValues = new ArrayList<>();
        this.revisionConsumables = new ArrayList<>();
    }

    @Override
    public long getCaptureTimestampInMillis() {
        return captureTimestampInMillis;
    }

    @Override
    public int getCapturePrecisionInNano() {
        return capturePrecisionInNano;
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
    public void addRevisionValue(RevisionValueSnTo revisionValue) {
        revisionValues.add(revisionValue.toMap());
    }

    @Override
    public void addRevisionConsumable(RevisionConsumableSnTo revisionConsumable) {
        revisionConsumables.add(revisionConsumable.toMap());
    }

    @Override
    public List<RevisionValueSnTo> getIRevisionValues() {
        List<RevisionValueSnTo> valueSnTos = new ArrayList<>();
        for (Map<String, String> revisionField : revisionValues) {
            valueSnTos.add(new RevisionValueSnTo(revisionField));
        }
        return valueSnTos;
    }

    @Override
    public List<RevisionConsumableSnTo> getIRevisionConsumables() {
        List<RevisionConsumableSnTo> consumableSnTos = new ArrayList<>();
        for (Map<String, Object> revisionField : revisionConsumables) {
            consumableSnTos.add(new RevisionConsumableSnTo(revisionField));
        }
        return consumableSnTos;
    }
}
