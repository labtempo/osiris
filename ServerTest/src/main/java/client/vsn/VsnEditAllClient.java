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
package client.vsn;

import br.uff.labtempo.omcp.client.OmcpClient;
import br.uff.labtempo.omcp.client.OmcpClientBuilder;
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.omcp.common.StatusCode;
import br.uff.labtempo.osiris.to.virtualsensornet.ConverterVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.DataTypeVsnTo;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class VsnEditAllClient {

    public static void main(String[] args) throws Exception {
        try (OmcpClient client = new OmcpClientBuilder().host("192.168.0.7").user("admin", "admin").source("generico").build()) {
            Response r;

            r = client.doGet("omcp://virtualsensornet/converter/");
            if (r.getStatusCode() == StatusCode.OK) {
                ConverterVsnTo[] cvts = r.getContent(ConverterVsnTo[].class);
                for (ConverterVsnTo cvt : cvts) {
                    ConverterVsnTo neo = new ConverterVsnTo(cvt.getDisplayName() + cvt.getId(), cvt.getExpression(), cvt.getInputDataTypeId(), cvt.getOutputDataTypeId());
                    r = client.doPut("omcp://virtualsensornet/converter/" + cvt.getId(), neo);
                    if (r.getStatusCode() == StatusCode.OK) {
                        r = client.doGet("omcp://virtualsensornet/converter/" + cvt.getId());
                        if (r.getStatusCode() == StatusCode.OK) {
                            cvt = r.getContent(ConverterVsnTo.class);
                            System.out.print(" NAME: " + cvt.getDisplayName());
                            System.out.println(" MODIFIED: " + "omcp://virtualsensornet/converter/" + cvt.getId());
                        } else {
                            throw new RuntimeException(r.getStatusCode().toString() + ":" + r.getErrorMessage());
                        }
                    } else {
                        throw new RuntimeException(r.getStatusCode().toString() + ":" + r.getErrorMessage());
                    }
                }
            } else {
                throw new RuntimeException(r.getStatusCode().toString());
            }

            r = client.doGet("omcp://virtualsensornet/datatype/");
            if (r.getStatusCode() == StatusCode.OK) {
                DataTypeVsnTo[] dtvts = r.getContent(DataTypeVsnTo[].class);
                for (DataTypeVsnTo dtvt : dtvts) {
                    DataTypeVsnTo neo = new DataTypeVsnTo(dtvt.getDisplayName() + dtvt.getId(), dtvt.getType(), dtvt.getUnit(), dtvt.getSymbol());
                    r = client.doPut("omcp://virtualsensornet/datatype/" + dtvt.getId(),neo);
                    if (r.getStatusCode() == StatusCode.OK) {
                         r = client.doGet("omcp://virtualsensornet/datatype/" + dtvt.getId());
                        if (r.getStatusCode() == StatusCode.OK) {
                            dtvt = r.getContent(DataTypeVsnTo.class);
                            System.out.print(" NAME: " + dtvt.getDisplayName());
                            System.out.println(" MODIFIED: " + "omcp://virtualsensornet/datatype/" + dtvt.getId());
                        } else {
                            throw new RuntimeException(r.getStatusCode().toString() + ":" + r.getErrorMessage());
                        }
                    } else {
                        throw new RuntimeException(r.getStatusCode().toString() + ":" + r.getErrorMessage());
                    }
                }
            } else {
                throw new RuntimeException(r.getStatusCode().toString());
            }
        }
    }
}
