/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.utils.date;

import com.joestelmach.natty.Parser;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public abstract class NlpDateParser {

    public static Calendar parser(String expression) {
        Parser parser = new Parser();
        List<Date> dates = parser.parse(expression).get(0).getDates();
        Date date = dates.get(0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
}
