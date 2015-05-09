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
import br.uff.labtempo.osiris.to.common.definitions.FunctionOperation;
import br.uff.labtempo.osiris.to.virtualsensornet.BlendingVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.ConverterVsnTo;
import br.uff.labtempo.osiris.to.virtualsensornet.LinkVsnTo;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class VsnUpdateBlendingRemoveFunction {

    public static void main(String[] args) throws Exception {
        try (OmcpClient connection = new OmcpClientBuilder().host("192.168.0.7").user("admin", "admin").source("generico").build()) {
            String address = "omcp://virtualsensornet/blending/4";
            Response r = connection.doGet(address);
            BlendingVsnTo to = r.getContent(BlendingVsnTo.class);
            update(to);
            r = connection.doPut(address, to);
            if (r.getStatusCode() == StatusCode.OK) {
                System.out.println(" MODIFIED: " + "omcp://virtualsensornet/blending/4/");
            } else {
                throw new RuntimeException(r.getStatusCode().toString() + ":" + r.getErrorMessage());
            }
        }
    }

    private static void update(BlendingVsnTo to) {
        long fldId1 = 2;
        long fldId2 = to.getFields().get(0).getId();

        to.setFunction(0);
    }
}
