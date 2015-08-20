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
import client.vsn.deletion.VsnBlending;
import client.vsn.deletion.VsnComposite;
import client.vsn.deletion.VsnConverter;
import client.vsn.deletion.VsnDatatype;
import client.vsn.deletion.VsnFunction;
import client.vsn.deletion.VsnLink;
import config.OmcpFactory;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class VsnDeleteAllClient {

    public static void main(String[] args) throws Exception {
        try (OmcpClient client = OmcpFactory.getClient()) {
            VsnComposite.deleteAll(client);
            VsnBlending.deleteAll(client);
            VsnLink.deleteAll(client);
            VsnFunction.deleteAll(client);
            VsnConverter.deleteAll(client);
            VsnDatatype.deleteAll(client);
        }
    }
}
