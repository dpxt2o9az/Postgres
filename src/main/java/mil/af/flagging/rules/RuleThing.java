/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.rules;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import mil.af.flagging.model.Intercept;

/**
 *
 * @author Brad
 */
public class RuleThing {

    public static void main(String[] args) {

        Map<String, Method> methods = new LinkedHashMap<>();
        for (Method m : Intercept.class.getMethods()) {
            if (m.getReturnType() == Void.TYPE) {
                continue;
            }
            if (m.getParameterCount() > 0) {
                continue;
            }
            if (m.getName().equals("getClass")) {
                continue;
            }

            if (m.getName().startsWith("get")) {
                methods.put(m.getName().substring(3), m);
            } else if (m.getReturnType() == Boolean.TYPE) {
                methods.put(m.getName(), m);
            }
        }

        for (Entry<String, Method> m : methods.entrySet()) {
            final String propertyName = m.getKey();
            final String methodName = m.getValue().getName();
            final Class<?> returnType = m.getValue().getReturnType();
            final String returnTypeString = returnType.getName().substring(returnType.getName().lastIndexOf(".")+1);
            System.out.println(propertyName + ": " + methodName + " returns " + returnTypeString);
        }
    }
}
