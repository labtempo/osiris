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

import br.uff.labtempo.osiris.to.common.definitions.ValueType;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class DataBase {

    public final static String NETWORK_ID = "test";//NETWORK_ID
    public final static String RESULT_FOLDER = "results";
    public final static String DATA_NAME = "message";
    public final static String DATA_UNIT = "identifier";
    public final static String DATA_SYMBOL = "ID";
    public final static ValueType DATA_TYPE = ValueType.NUMBER;
    public final static String RESOURCE_DATATYPE = "omcp://virtualsensornet/datatype/";
    public final static String RESOURCE_CONVERTER = "omcp://virtualsensornet/converter/";
    public final static String RESOURCE_VSENSOR = "omcp://virtualsensornet/vsensor/";
    public final static String RESOURCE_LINK = "omcp://virtualsensornet/link/";
    public final static String RESOURCE_COMPOSITE = "omcp://virtualsensornet/composite/";
    public final static String RESOURCE_BLENDING = "omcp://virtualsensornet/blending/";
    public final static String RESOURCE_FUNCTION = "omcp://virtualsensornet/function/";
    public final static String RESOURCE_UPDATE_VSN = "omcp://update.messagegroup/virtualsensornet/#";
}
