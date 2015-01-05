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
package br.uff.labtempo.omcp.common.exceptions;

/**
 * This exception can be designate these problems when calling other module:<br>
 * <ol>
 * <li>not connected to broker</li>
 * <li>not has the declared queue</li>
 * <li>module not exists</li>
 * </ol>
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class UnreachableModuleException extends AbstractClientRuntimeException {

    public UnreachableModuleException() {
        super();
    }

    public UnreachableModuleException(String message) {
        super(message);
    }

    public UnreachableModuleException(Throwable cause) {
        super(cause);
    }

    public UnreachableModuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
