package cn.wenhaha.security.tool;

import cn.wenhaha.security.controller.LoginController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: Wyndem
 * @Description: 基于Parameter的工具包
 * @Date: Created in  2018-11-01 21:47
 * @Modified By:
 */

public class ParameterUtils {


    private final Class clazz;


    public ParameterUtils(String className) throws ClassNotFoundException {

        this.clazz= Class.forName(className);
    }



    public List<String> analysisParameters(String methodName){

        List<String> parametes=new ArrayList<>();

        List<Method> methods = findbyMethodName(methodName);


        for (Method m : methods) {

            if (m.isAnnotationPresent(cn.wenhaha.security.Parameter.class)) {
                cn.wenhaha.security.Parameter annotation = m.getAnnotation(cn.wenhaha.security.Parameter.class);
                if (annotation.parameters().length == 0 &&! "".equals(annotation.value())) {
                    parametes.add(annotation.value());
                } else {
                    Arrays.stream(annotation.parameters()).forEach(p->{
                        parametes.add(p);
                    });
                }
                break;
            }
        }
        return parametes;

    }





    private   List<Method> findbyMethodName(String name){

        List<Method> methods=new ArrayList<>(5);
        Method[] declaredMethods = clazz.getDeclaredMethods();

        for (Method m : declaredMethods) {
            if (m.getName().equals(name))
                methods.add(m);
        }
        return methods;

    }






    public Object invoke(String methodName,String ... parametes){

        Object reset=null;
        for (Method m : findbyMethodName(methodName)) {
            try {
                reset = m.invoke(clazz.newInstance(), parametes);
            } catch (Exception e) {

            }
        }
        return reset;
    }



}