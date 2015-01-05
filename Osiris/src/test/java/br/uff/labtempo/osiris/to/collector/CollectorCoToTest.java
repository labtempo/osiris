/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.to.collector;

import br.uff.labtempo.omcp.common.utils.Serializer;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Felipe
 */
public class CollectorCoToTest {

    private final CollectorCoTo collectorTo;

    public CollectorCoToTest() {
        this.collectorTo = new CollectorCoTo("test", 1, TimeUnit.DAYS);
        collectorTo.addInfo("key", "value");
    }

    @Test
    public void testSomeMethod() {
        CollectorCoTo newCoTo = serializer();
        assertEquals(collectorTo, newCoTo);
    }

    private CollectorCoTo serializer() {
        Serializer serializer = new Serializer();
        String json = serializer.toJson(collectorTo);
        System.out.println(json);
        CollectorCoTo newCoTo = serializer.fromJson(json, CollectorCoTo.class);
        return newCoTo;
    }
}
