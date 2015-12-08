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
import br.uff.labtempo.omcp.common.Response;
import br.uff.labtempo.osiris.to.virtualsensornet.LinkVsnTo;
import config.OmcpFactory;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class VsnGetLink {

    public static void main(String[] args) throws Exception {
        try (OmcpClient connection = OmcpFactory.getClient()) {
            Response r = connection.doGet("omcp://virtualsensornet/link/1");
            LinkVsnTo to = r.getContent(LinkVsnTo.class);
            System.out.println("GOT(link): " + to.getId());
        }
        
        try (OmcpClient connection = OmcpFactory.getClient()) {
            Response r = connection.doGet("omcp://virtualsensornet/link/?sensor=1&collector=labpos&network=labtempo");
            LinkVsnTo[] tos = r.getContent(LinkVsnTo[].class);
            for (LinkVsnTo to : tos) {
                System.out.println("GOT(link): " + to.getId());
            }            
        }
    }
}
