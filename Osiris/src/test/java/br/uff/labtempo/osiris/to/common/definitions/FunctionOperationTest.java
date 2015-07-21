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
package br.uff.labtempo.osiris.to.common.definitions;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class FunctionOperationTest {
    
    public FunctionOperationTest() {
    }

    @Test
    public void testSerialization_Sync_ShouldPass() {
        FunctionOperation operation1 = FunctionOperation.SYNCHRONOUS;
        
        String op = operation1.toString();
        
        FunctionOperation operation2 = FunctionOperation.getByMode(op);
        
        String expectedString = "sync";
        assertEquals(operation1, operation2);
        assertEquals(expectedString, op);
    }
    
    @Test
    public void testSerialization_Async_ShouldPass() {
        FunctionOperation operation1 = FunctionOperation.ASYNCHRONOUS;
        
        String op = operation1.toString();
        
        FunctionOperation operation2 = FunctionOperation.getByMode(op);
        
        String expectedString = "async";
        assertEquals(operation1, operation2);
        assertEquals(expectedString, op);
    }
    
}
