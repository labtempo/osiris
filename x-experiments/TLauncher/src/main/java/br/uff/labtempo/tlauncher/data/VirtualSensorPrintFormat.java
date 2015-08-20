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
package br.uff.labtempo.tlauncher.data;

import br.uff.labtempo.osiris.to.virtualsensornet.ValueVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.VirtualSensorVsnTo;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class VirtualSensorPrintFormat {

    private long unpackingTimestamp;
    private VirtualSensorVsnTo virtualSensorVsnTo;
    private String id;
    private final String tuple;
    private final long endToEndInMillis;

    public VirtualSensorPrintFormat(VirtualSensorVsnTo virtualSensorVsnTo, long unpackingTimestamp) {
        this.unpackingTimestamp = unpackingTimestamp;
        this.virtualSensorVsnTo = virtualSensorVsnTo;
        ValueVsnTo valueVsnTo = virtualSensorVsnTo.getValuesTo().get(0);
        this.id = valueVsnTo.getValue();

        long t1 = virtualSensorVsnTo.getAcquisitionTimestampInMillis();
        long t2 = virtualSensorVsnTo.getStorageTimestampInMillis();        
        long t4 = unpackingTimestamp;
        
        long t3 = virtualSensorVsnTo.getPackingTimestampInMillis();
        long ty = virtualSensorVsnTo.getFetchingTimestampInMillis();

        this.endToEndInMillis = (t4 - t1);

        this.tuple = id + "\t" + t1 + "\t" + t2 + "\t" + t4 + "\t" + (t2 - t1) + "\t" + (t3 - t2) + "\t" + (ty) + "\t" + endToEndInMillis + "\t" + (t4 - t2);
    }

    @Override
    public String toString() {
        return tuple;
    }

    public String getId() {
        return id;
    }

    public long getEndToEndInMillis() {
        return endToEndInMillis;
    }

    public long getEndToEndInSeconds() {
        return endToEndInMillis / 1000;
    }

    public static String getHeaders() {
        String header = "ID\tSent(ms)\tStored(ms)\tReceived(ms)\tStoringLatency(ms)\tStoring(ms)\tFetching(ms)\tEndToEndLatency(ms)\tStoringToEndLatency(ms)";
        return header;
    }

}
