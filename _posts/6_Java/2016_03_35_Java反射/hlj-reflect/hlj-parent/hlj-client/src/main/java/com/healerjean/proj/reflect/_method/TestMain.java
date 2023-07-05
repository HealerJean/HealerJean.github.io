package com.healerjean.proj.reflect._method;

import com.healerjean.proj.reflect.MethodPerson;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author zhangyujin
 * @date 2023/7/3$  09:32$
 */
public class TestMain {


    @Test
    public void getMethods() {
        Class<?> methodPersonClass = MethodPerson.class;
        Method[] methods = methodPersonClass.getMethods();
        for (Method method : methods) {
            System.out.println(method);
            System.out.println(method.getName());
        }
    }

    @Test
    public void test() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<?> methodPersonClass = MethodPerson.class;

        MethodPerson methodPerson = (MethodPerson) methodPersonClass.newInstance();

        Method setNameMethod = methodPersonClass.getMethod("setName", String.class);
        Method getNameMethod = methodPersonClass.getMethod("getName");

        setNameMethod.invoke(methodPerson, "setNameMethod.setName变量");

        System.out.println("调用get方法：" + getNameMethod.invoke(methodPerson));

    }
}
