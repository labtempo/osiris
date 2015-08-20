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

import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.StatusCode;
import br.uff.labtempo.osiris.to.virtualsensornet.CompositeVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.DataTypeVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.LinkVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.ValueVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.VirtualSensorVsnTo;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class VirtualSensorNetDataBuilder {

    private OmcpClient client;
    private long dataTypeId;

    public VirtualSensorNetDataBuilder(OmcpClient client) {
        this.client = client;
    }

    public long createLink(String collectorId, String sensorsId) {
        List<String> l = new ArrayList<>();
        l.add(sensorsId);
        return createLink(collectorId, l).get(0);
    }

    public List<Long> createLink(String collectorId, List<String> sensorsIds) {
        if (dataTypeId == 0) {
            createDataType();
        }
        List<Long> linkIds = new ArrayList<>();
        for (String sensorsId : sensorsIds) {
            LinkVsnTo lvt = generateLink(collectorId, sensorsId, dataTypeId);
            Response r = client.doPost(DataBase.RESOURCE_LINK, lvt);
            if (r.getStatusCode() == StatusCode.CREATED) {
                r = client.doGet(r.getLocation());
                lvt = r.getContent(LinkVsnTo.class);
                linkIds.add(lvt.getId());
            } else {
                throw new RuntimeException(r.getStatusCode().toString() + ":" + r.getErrorMessage());
            }
        }
        return linkIds;
    }

    public List<Long> createNestedComposite(long sensorId, int total) {
        long refSensorId = sensorId;        
        List<Long> linkIds = new ArrayList<>();

        for (int i = 0; i < total; i++) {
            long fieldId = getValuesFromVirtualSensor(refSensorId).get(0).getId();
            CompositeVsnTo lvt = generateComposite(fieldId);
            Response r = client.doPost(DataBase.RESOURCE_COMPOSITE, lvt);
            if (r.getStatusCode() == StatusCode.CREATED) {
                r = client.doGet(r.getLocation());
                lvt = r.getContent(CompositeVsnTo.class);
                linkIds.add(lvt.getId());
                refSensorId = lvt.getId();
            } else {
                throw new RuntimeException(r.getStatusCode().toString() + ":" + r.getErrorMessage());
            }
        }
        return linkIds;
    }

    public List<Long> createComposite(long sensorId, int total) {
        VirtualSensorVsnTo sensor = getVirtualSensor(sensorId);
        List<ValueVsnTo> valuesTo = sensor.getValuesTo();
        long fieldId = valuesTo.get(0).getId();

        List<Long> linkIds = new ArrayList<>();

        for (int i = 0; i < total; i++) {
            CompositeVsnTo lvt = generateComposite(fieldId);
            Response r = client.doPost(DataBase.RESOURCE_COMPOSITE, lvt);
            if (r.getStatusCode() == StatusCode.CREATED) {
                r = client.doGet(r.getLocation());
                lvt = r.getContent(CompositeVsnTo.class);
                linkIds.add(lvt.getId());
            } else {
                throw new RuntimeException(r.getStatusCode().toString() + ":" + r.getErrorMessage());
            }
        }
        return linkIds;
    }

    private void createDataType() {
        DataTypeVsnTo dtvt = generateDataType();
        Response r = client.doPost(DataBase.RESOURCE_DATATYPE, dtvt);
        if (r.getStatusCode() == StatusCode.CREATED) {
            r = client.doGet(r.getLocation());
            dtvt = r.getContent(DataTypeVsnTo.class);
            dataTypeId = dtvt.getId();
        } else {
            throw new RuntimeException(r.getStatusCode().toString() + ":" + r.getErrorMessage());
        }
    }

    private static DataTypeVsnTo generateDataType() {
        DataTypeVsnTo dtvts = new DataTypeVsnTo(DataBase.DATA_NAME, DataBase.DATA_TYPE, DataBase.DATA_UNIT, DataBase.DATA_SYMBOL);
        return dtvts;
    }

    private static LinkVsnTo generateLink(String collectorId, String sensorId, long dataTypeId) {
        LinkVsnTo linkVsnTo = new LinkVsnTo(sensorId, collectorId, DataBase.NETWORK_ID);
        linkVsnTo.createField(DataBase.DATA_NAME, dataTypeId);
        return linkVsnTo;
    }

    private CompositeVsnTo generateComposite(long fieldId) {
        CompositeVsnTo compositeVsnTo = new CompositeVsnTo();
        compositeVsnTo.bindToField(fieldId);
        return compositeVsnTo;
    }
    
    private List<ValueVsnTo> getValuesFromVirtualSensor(long sensorId) {
        Response r = client.doGet(DataBase.RESOURCE_VSENSOR + sensorId + "/");
        if (r.getStatusCode() == StatusCode.OK) {
            VirtualSensorVsnTo sensor = r.getContent(VirtualSensorVsnTo.class);
            return sensor.getValuesTo();
        } else {
            throw new RuntimeException(r.getStatusCode().toString() + ":" + r.getErrorMessage());
        }
    }

    private VirtualSensorVsnTo getVirtualSensor(long sensorId) {
        Response r = client.doGet(DataBase.RESOURCE_VSENSOR + sensorId + "/");
        if (r.getStatusCode() == StatusCode.OK) {
            VirtualSensorVsnTo sensor = r.getContent(VirtualSensorVsnTo.class);
            return sensor;
        } else {
            throw new RuntimeException(r.getStatusCode().toString() + ":" + r.getErrorMessage());
        }
    }
}
