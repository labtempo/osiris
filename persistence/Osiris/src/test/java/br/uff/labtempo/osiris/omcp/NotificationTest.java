/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.omcp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Felipe
 */
public class NotificationTest {

    @Test
    public void testString() {
        System.out.println("String");
        String t = "teste";
        Notification instance = new Notification(Notification.Type.Created, t, t.getClass().getSimpleName());
        String result = instance.toString();
        Notification copy = Notification.parse(result);
        assertEquals(copy, instance);
    }

    @Test
    public void testList() {
        System.out.println("List");

        List<String> list = new ArrayList<>();

        list.add("fulano");
        list.add("ciclano");

        Notification instance = new Notification(Notification.Type.Created, list, list.getClass().getSimpleName());
        String result = instance.toString();
        Notification copy = Notification.parse(result);
        assertEquals(copy, instance);
    }

    @Test
    public void testMap() {
        System.out.println("Map");

        Map<String, Integer> map = new HashMap<>();

        map.put("fulano", 1);
        map.put("ciclano", 2);

        Notification instance = new Notification(Notification.Type.Created, map, map.getClass().getSimpleName());
        String result = instance.toString();
        Notification copy = Notification.parse(result);

        assertEquals(copy, instance);
    }

    @Test
    public void testMapNull() {
        System.out.println("MapNull");

        TestClass t = new TestClass();
        t.nulled();
        Notification instance = new Notification(Notification.Type.Created, t, t.getClass().getSimpleName());
        String result = instance.toString();
        Notification copy = Notification.parse(result);

        TestClass t1 = instance.getData(t.getClass());
        TestClass t2 = copy.getData(t.getClass());

        assertEquals(t1, t2);
    }

    @Test
    public void testObjectFromJson() {
        System.out.println("ObjectFromJson");
        TestClass t = new TestClass();
        Notification instance = new Notification(Notification.Type.Created, t, t.getClass().getSimpleName());
        String result = instance.toString();
        Notification copy = Notification.parse(result);

        TestClass t1 = instance.getData(t.getClass());
        TestClass t2 = copy.getData(t.getClass());

        assertEquals(t1, t2);
    }

    private class TestClass {

        private Map<String, Integer> map = new HashMap<>();

        public TestClass() {
            map.put("fulano", 1);
            map.put("ciclano", 2);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)//mesma instancia  
            {
                return true;
            }
            if (obj == null)//objeto null sempre igual a false  
            {
                return false;
            }
            if (getClass() != obj.getClass())//são de de classes diferentes  
            {
                return false;
            }

            TestClass other = (TestClass) obj;

            return map.equals(other.map);
        }

        private void nulled() {
            map = new HashMap<>();
        }

    }

}
