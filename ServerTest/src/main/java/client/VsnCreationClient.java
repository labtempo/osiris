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
package client;

import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.omcp.client.OmcpClientBuilder;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.StatusCode;
import br.uff.labtempo.omcp.common.utils.ResponsePacket;
import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import br.uff.labtempo.osiris.to.virtualsensornet.ConverterVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.DataTypeVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.LinkVsnTo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class VsnCreationClient {

    public static void main(String[] args) throws Exception {
        try (OmcpClient connection = new OmcpClientBuilder().host("192.168.0.7").user("admin", "admin").source("generico").build()) {
            populate(connection);
        }
    }

    public static void populate(OmcpClient client) {
        List<DataTypeVsnTo> dtvts = new ArrayList<>();
        dtvts.add(new DataTypeVsnTo("tempCelsius", ValueType.NUMBER, "celsius", "°C"));
        dtvts.add(new DataTypeVsnTo("tempFahrenheit", ValueType.NUMBER, "fahrenheit ", "°F"));
        dtvts.add(new DataTypeVsnTo("tempKelvin", ValueType.NUMBER, "kelvin", "°K"));

        Map<String, Long> ids = new HashMap<>();

        int i = 1;
        for (DataTypeVsnTo dtvt : dtvts) {
            Response r = client.doPost("omcp://virtualsensornet/datatype/", dtvt);
            if (r.getStatusCode() == StatusCode.CREATED) {
                System.out.print("Created: " + r.getLocation());
                r = client.doGet(r.getLocation());
                dtvt = r.getContent(DataTypeVsnTo.class);
                System.out.println(" ID: " + dtvt.getId());
                ids.put("dt" + String.valueOf(i++), dtvt.getId());
            } else {
                throw new RuntimeException(r.getStatusCode().toString());
            }
        }

        List<ConverterVsnTo> cvts = new ArrayList<>();
        cvts.add(new ConverterVsnTo("celsiusToFahrenheit", "value = value * 9/5 + 32;", ids.get("dt1"), ids.get("dt2")));
        cvts.add(new ConverterVsnTo("celsiusToKelvin", "value = value + 273.15;", ids.get("dt1"), ids.get("dt3")));
        cvts.add(new ConverterVsnTo("FahrenheitToCelsius", "value = (value - 32) * 5/9;", ids.get("dt2"), ids.get("dt1")));
        cvts.add(new ConverterVsnTo("kelvinToFahrenheit", "value = value * 9/5 - 459.67;", ids.get("dt3"), ids.get("dt2")));

        i = 1;
        for (ConverterVsnTo cvt : cvts) {
            Response r = client.doPost("omcp://virtualsensornet/converter/", cvt);
            if (r.getStatusCode() == StatusCode.CREATED) {
                System.out.print("Created: " + r.getLocation());
                r = client.doGet(r.getLocation());
                cvt = r.getContent(ConverterVsnTo.class);
                System.out.println(" ID: " + cvt.getId());
                ids.put("cv" + String.valueOf(i++), cvt.getId());
            } else {
                throw new RuntimeException(r.getStatusCode().toString());
            }
        }

        LinkVsnTo linkVsnTo = new LinkVsnTo("1", "labpos", "labtempo");
        linkVsnTo.createField("temperature", ids.get("dt2"), ids.get("cv1"));
        linkVsnTo.createField("temperature", ids.get("dt1"));

        Response r = client.doPost("omcp://virtualsensornet/link/", linkVsnTo);
        if (r.getStatusCode() == StatusCode.CREATED) {
            System.out.print("Created: " + r.getLocation());
            r = client.doGet(r.getLocation());
            linkVsnTo = r.getContent(LinkVsnTo.class);
            System.out.println(" ID: " + linkVsnTo.getId());
        } else {
            throw new RuntimeException(r.getStatusCode().toString());
        }
    }
}
