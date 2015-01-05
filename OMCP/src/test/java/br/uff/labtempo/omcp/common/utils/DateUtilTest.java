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
 * @author Felipe Santos <fralph at ic.uff.br>
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
