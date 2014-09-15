/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.server.router;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Felipe
 */
public class WrapperInvocation {

    private final Object object;
    private final Method method;
    private final String regex;

    public WrapperInvocation(Object object, Method method, String regex) {
        this.object = object;
        this.method = method;
        this.regex = regex;
    }

    public void invoke(String path) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object[] args = extract(path);
        method.invoke(object, args);
    }

    private String[] extract(String path) {
        Pattern datePatt = Pattern.compile(regex);
        Matcher m = datePatt.matcher(path);
        
        List<String> args = new ArrayList<>();
        
        while (m.find()) {
            for (int i = 1; i <= m.groupCount(); i++) {
                args.add(m.group(i));
            }
        }
        
        return args.toArray(new String[args.size()]);
    }

    public boolean match(String path) {
        if (path != null) {
            return path.matches(regex);
        }
        return false;
    }

}
