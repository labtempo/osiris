/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.labtempo.omcp.common.utils;

import java.util.Calendar;
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
public class DateUtilTest {
    private Calendar date;    
    private String dateString = "Thu, 24 Jul 2014 15:40:03 GMT";
    
    @Before
    public void setUp() {
        this.date = Calendar.getInstance();
        date.clear(Calendar.MILLISECOND);
    }
    
    
    /**
     * Test of parse method, of class DateUtil.
     */
    @Test
    public void testParse() throws Exception {
        Calendar d = new DateUtil().parse(dateString);        
        assertEquals(new DateUtil().generate(d), dateString);
    }

    /**
     * Test of generate method, of class DateUtil.
     */
    @Test
    public void testGenerate() throws Exception{
        String d = new DateUtil().generate(date);        
        assertEquals(new DateUtil().parse(d), date);
    }
    
}
