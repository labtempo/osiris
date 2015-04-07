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
package br.uff.labtempo.osiris.to.function;

import br.uff.labtempo.omcp.common.utils.Serializer;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class ValueFnToTest {

    public ValueFnToTest() {
    }

    @Test
    public void testSerializationMultiValueFnTo_ShouldPass() {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");

        MultiValueFnTo mvft = new MultiValueFnTo("1", list);

        Serializer serializer = new Serializer();

        String json = serializer.toJson(mvft);

        MultiValueFnTo mvft2 = serializer.fromJson(json, MultiValueFnTo.class);

        Assert.assertEquals(mvft.getValues(), mvft2.getValues());
        Assert.assertEquals(mvft.getName(), mvft2.getName());
        Assert.assertEquals(mvft.isCollection(), mvft2.isCollection());

    }

    @Test
    public void testSerializationSingleValueFnTo_ShouldPass() {

        SingleValueFnTo svft = new SingleValueFnTo("1", "1");

        Serializer serializer = new Serializer();

        String json = serializer.toJson(svft);

        SingleValueFnTo svft2 = serializer.fromJson(json, SingleValueFnTo.class);

        Assert.assertEquals(svft.getValue(), svft2.getValue());
        Assert.assertEquals(svft.getName(), svft2.getName());
        Assert.assertEquals(svft.isCollection(), svft2.isCollection());

    }

    @Test
    public void testSerializationResponseFnTo_ShouldPass() {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");

        RequestFnTo requestFnTo = new RequestFnTo();
        requestFnTo.addValue("1", "1");
        requestFnTo.addValue("2", "1");
        requestFnTo.addValue("3", "1");
        requestFnTo.addValue("4", list);
        requestFnTo.addValue("5", list);

        Serializer serializer = new Serializer();

        String json = serializer.toJson(requestFnTo);

        RequestFnTo requestFnTo2 = serializer.fromJson(json, RequestFnTo.class);

        List<ValueFnTo> l1 = requestFnTo.getValues();
        List<ValueFnTo> l2 = requestFnTo2.getValues();

        Assert.assertEquals(requestFnTo.getValues(), requestFnTo.getValues());

    }

}
