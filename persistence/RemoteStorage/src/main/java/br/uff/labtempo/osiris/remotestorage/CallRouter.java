/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.remotestorage;

import br.uff.labtempo.osiris.remotestorage.amqp.OnCallListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe
 */
public class CallRouter implements OnCallListener {

    private final String RESOURCE_DELIMITER = "\\?";
    private final String PARAM_DELIMITER = "&";
    private final String ARRAY_DELIMITER = ";";
    private final String NULLED_RETURN = "<<null>>";

    private Storage storage;

    public CallRouter(Storage storage) {
        this.storage = storage;
    }

    public String onCall(String message) {
        System.out.println(">>incoming: " + message);

        String result = execute(extractMessage(message));
        System.out.println("<<outgoing: " + result);
        return result;
    }

    private Message extractMessage(String msg) {
        Message message = new Message();

        String[] levelA = msg.split(RESOURCE_DELIMITER);

        message.method = levelA[0];

        if (levelA.length > 1) {
            for (String param : levelA[1].split(PARAM_DELIMITER)) {
                message.addParam(param);
            }
        }

        return message;
    }

    private String execute(Message message) {

        try {
            Method method = getMethod(storage, message.method);

            Object ret = method.invoke(storage, message.getParams());

            if (method.getReturnType().equals(Void.TYPE)) {
                return NULLED_RETURN;
            }
            if (method.getReturnType().equals(Boolean.TYPE)) {
                return String.valueOf((boolean) ret);
            }
            if (method.getReturnType().equals(String.class)) {
                return (String) ret;
            }
            if (method.getReturnType().equals(String[].class)) {
                String[] params = (String[]) ret;
                StringBuilder builder = new StringBuilder();
                for (String s : params) {
                    builder.append(s);
                    builder.append(ARRAY_DELIMITER);
                }
                return builder.toString();
            }

        } catch (IllegalAccessException ex) {
            Logger.getLogger(CallRouter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(CallRouter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(CallRouter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(CallRouter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(CallRouter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return NULLED_RETURN;
    }

    private Method getMethod(Object obj, String target) throws NoSuchMethodException {
        for (Method m : obj.getClass().getMethods()) {
            if (target.equals(m.getName())) {
                Class[] paramsType = m.getParameterTypes();
                return obj.getClass().getMethod(target, paramsType);
            }
        }
        return null;
    }

    private class Message {

        String method;
        List<String> params;

        public Message() {
            params = new ArrayList<String>();
        }

        void addParam(String param) {
            if (param != null) {
                params.add(param);
            }
        }

        boolean hasParam() {
            return params.size() > 0;
        }

        boolean isValid() {
            if (method != null && method.length() > 0) {
                return true;
            }
            return false;
        }

        String[] getParams() {
            return params.toArray(new String[params.size()]);
        }
    }
}
