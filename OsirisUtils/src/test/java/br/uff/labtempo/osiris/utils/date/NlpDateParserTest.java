/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.utils.date;

import br.uff.labtempo.omcp.common.utils.DateUtil;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class NlpDateParserTest {

    public NlpDateParserTest() {
    }

    @Test
    public void testParse_ShouldPass() {
        DateUtil util = new DateUtil();
        String now = util.generate(Calendar.getInstance());
        Calendar calendar = NlpDateParser.parser(now);
        String after = util.generate(calendar);
        assertEquals(now, after);
    }

}
