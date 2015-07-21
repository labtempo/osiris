/*
 * Copyright 2015 Felipe Santos <feliperalph at hotmail.com>.
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
package br.uff.labtempo.osiris.to.common.definitions;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public enum FunctionOperation {

    /**
     * Synchronous operation works with GET request method
     */
    SYNCHRONOUS("sync"),
    /**
     * Asynchronous operation works with POST request method and it needs an
     * callback's address for send the result
     */
    ASYNCHRONOUS("async");

    private final String mode;

    private FunctionOperation(String mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return mode;
    }

    public static FunctionOperation getByMode(String mode) {
        for (FunctionOperation functionOperation : values()) {
            if (functionOperation.mode.equalsIgnoreCase(mode)) {
                return functionOperation;
            }
        }
        throw new IllegalArgumentException("No enum constant " + FunctionOperation.class.getName());
    }
}
