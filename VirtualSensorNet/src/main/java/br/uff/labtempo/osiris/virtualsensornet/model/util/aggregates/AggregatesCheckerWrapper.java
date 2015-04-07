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
package br.uff.labtempo.osiris.virtualsensornet.model.util.aggregates;

import br.uff.labtempo.osiris.virtualsensornet.model.VirtualSensor;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class AggregatesCheckerWrapper implements AggregatesChecker {

    private final AggregatesChecker checker;

    public AggregatesCheckerWrapper(AggregatesChecker checker) {
        this.checker = checker;
    }

    @Override

    public void check(VirtualSensor virtualSensor) {
        if (checker != null) {
            checker.check(virtualSensor);
        }
    }
}
