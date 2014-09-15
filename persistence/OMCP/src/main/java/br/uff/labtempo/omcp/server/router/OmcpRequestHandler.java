/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.server.router;

import br.uff.labtempo.omcp.server.router.annotations.RequestMapping;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.RequestWrapper;

/**
 *
 * @author Felipe
 */
public class OmcpRequestHandler {

    public void call(Object instance, String method, String requestPath) {
        try {
            WrapperInvocation wrap = null;
            Class klass = instance.getClass();

            Method[] declaredMethods = klass.getDeclaredMethods();

            for (Method declaredMethod : declaredMethods) {
                if (declaredMethod.getName().equals(method)
                        && declaredMethod.getAnnotation(RequestMapping.class) != null) {
                    RequestMapping rm = declaredMethod.getAnnotation(RequestMapping.class);
                    String regex = createRegex(rm.value());
                    wrap = new WrapperInvocation(instance, declaredMethod, regex);
                    break;
                }
            }

            if (wrap != null && wrap.match(requestPath)) {                
                wrap.invoke(requestPath);
            }

        } catch (SecurityException ex) {
            Logger.getLogger(OmcpRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(OmcpRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(OmcpRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(OmcpRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * transformar para regex acima method params
     */
    public String createRegex(String value) {

        String[] resources = value.split("/");
        StringBuilder sb = new StringBuilder();

        sb.append("^");

        for (String resource : resources) {

            if (resource.length() == 0) {
                continue;
            }

            sb.append("/");
            if (resource.contains(":")) {
                sb.append("(.[^/]*)");
            } else {
                sb.append(resource);
            }

        }

        sb.append("/?$");
        return sb.toString();

    }
}
