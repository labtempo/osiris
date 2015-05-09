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
import br.uff.labtempo.omcp.common.utils.ResponsePacket;
import br.uff.labtempo.osiris.to.common.data.FieldTo;
import br.uff.labtempo.osiris.to.common.data.ValueTo;
import br.uff.labtempo.osiris.to.common.definitions.ValueType;
import br.uff.labtempo.osiris.to.sensornet.SensorSnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.BlendingVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.CompositeVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.ConverterVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.DataTypeVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.FunctionVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.LinkVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.VirtualSensorVsnTo;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class VsnGetAllClient {

    public static void main(String[] args) throws Exception {
        try (OmcpClient client = new OmcpClientBuilder().host("192.168.0.7").user("admin", "admin").source("generico").build()) {
            Response r = client.doGet("omcp://virtualsensornet/datatype/");
            if (r.getStatusCode() == StatusCode.OK) {
                DataTypeVsnTo[] dtvts = r.getContent(DataTypeVsnTo[].class);
                for (DataTypeVsnTo dtvt : dtvts) {
                    System.out.println("GOT(datatype): " + dtvt.getDisplayName());
                }
            } else {
                throw new RuntimeException(r.getStatusCode().toString());
            }
            r = client.doGet("omcp://virtualsensornet/converter/");
            if (r.getStatusCode() == StatusCode.OK) {
                ConverterVsnTo[] cvts = r.getContent(ConverterVsnTo[].class);
                for (ConverterVsnTo cvt : cvts) {
                    System.out.println("GOT(converter): " + cvt.getDisplayName());
                }
            } else {
                throw new RuntimeException(r.getStatusCode().toString());
            }
            r = client.doGet("omcp://virtualsensornet/function/");
            if (r.getStatusCode() == StatusCode.OK) {
                FunctionVsnTo[] cvts = r.getContent(FunctionVsnTo[].class);
                for (FunctionVsnTo cvt : cvts) {
                    System.out.println("GOT(function): " + cvt.getName());
                }
            } else {
                throw new RuntimeException(r.getStatusCode().toString());
            }
             r = client.doGet("omcp://virtualsensornet/vsensor/");
            if (r.getStatusCode() == StatusCode.OK) {
                VirtualSensorVsnTo[] vsvts = r.getContent(VirtualSensorVsnTo[].class);
                for (VirtualSensorVsnTo vsvt : vsvts) {
                    System.out.println("GOT(vsensor): " + vsvt.getId()+" - " + vsvt.getLabel());
                    List<? extends ValueTo> vts = vsvt.getValuesTo();
                    for (ValueTo vt : vts) {
                        System.out.print("\t"+vt.getName()+"("+vt.getUnit()+")");
                        System.out.print(": "+vt.getValue());
                        System.out.println(" "+vt.getSymbol());
                    }                    
                }
            } else {
                throw new RuntimeException(r.getStatusCode().toString());

            }
            r = client.doGet("omcp://virtualsensornet/link/");
            if (r.getStatusCode() == StatusCode.OK) {
                LinkVsnTo[] vsvts = r.getContent(LinkVsnTo[].class);
                for (LinkVsnTo vsvt : vsvts) {
                    System.out.println("GOT(link): " + vsvt.getId());
                }
            } else {
                throw new RuntimeException(r.getStatusCode().toString());

            }   
            r = client.doGet("omcp://virtualsensornet/composite/");
            if (r.getStatusCode() == StatusCode.OK) {
                CompositeVsnTo[] vsvts = r.getContent(CompositeVsnTo[].class);
                for (CompositeVsnTo vsvt : vsvts) {
                    System.out.println("GOT(composite): " + vsvt.getId());
                }
            } else {
                throw new RuntimeException(r.getStatusCode().toString());

            }   
            r = client.doGet("omcp://virtualsensornet/blending/");
            if (r.getStatusCode() == StatusCode.OK) {
                BlendingVsnTo[] vsvts = r.getContent(BlendingVsnTo[].class);
                for (BlendingVsnTo vsvt : vsvts) {
                    System.out.println("GOT(blending): " + vsvt.getId());
                }
            } else {
                throw new RuntimeException(r.getStatusCode().toString());

            }
        }
    }
}
