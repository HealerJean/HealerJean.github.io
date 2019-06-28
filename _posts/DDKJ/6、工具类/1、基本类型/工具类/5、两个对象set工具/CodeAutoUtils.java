package com.fintech.client.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName CodeAutoUtils
 * @date 2019/6/28  13:38.
 * @Description
 */
public class CodeAutoUtils {


    // /**
    //  * 使用样例
    //  * @param args
    //  */
    // public static void main(String[] args) {
    //     DepartmentLimitLogDTO departmentLimitLogDTO = new DepartmentLimitLogDTO();
    //     DepartmentLimitDTO departmentLimitDTO = new DepartmentLimitDTO();
    //
    //     System.out.println( beanUtilsCopy(departmentLimitDTO,departmentLimitLogDTO,"departmentLimitDTO","departmentLimitLogDTO"));
    //
    // }


    /**
     * 对象A赋值到对象B
     */
    public static String beanUtilsCopy(Object a,Object b,String aObjectName,String bObjectName ){
        StringBuilder stringBuilder = new StringBuilder();
        Set<Field> fields = SetEqualsFileds(a, b);
        for(Field field :fields){
            String fieldName = field.getName();
            String methodName = getMethodName(fieldName);
            String setMethodName = ".set"+methodName;
            String getMethodNmae = ".get"+methodName+"()";
            stringBuilder.append(aObjectName+setMethodName+"("+bObjectName+getMethodNmae+");\n");
        }
        return stringBuilder.toString();
    }

    /**
     * 获取两个对象相同的 字段名称
     * @param a
     * @param b
     * @return
     */
    private static Set<Field> SetEqualsFileds(Object a, Object b) {
        Set<Field> aFields = getField(a);
        Set<Field> bFields = getField(b);
        Set<Field> fields = new HashSet<>();
        for(Field fieldA: aFields){
            for (Field fieldB :bFields){
                if(fieldA.getName().equals(fieldB.getName())){
                    fields.add(fieldA);
                }
            }
        }
        return fields;
    }


    /**
     * 获取所有字段名
     * @param object
     * @return
     */
    public static  Set<Field>   getField(Object object ){
        Set<Field> declaredFields = new HashSet<>();
        Class tempClass = object .getClass() ;
        //反射获取父类里面的属性
        while (tempClass != null && !tempClass.getName().toLowerCase().equals("java.lang.object")) {
            declaredFields.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass();
        }
        return declaredFields ;
    }



    /**
     * 根据字段获取方法名字（不包括set和get）
     * @param fildeName
     * @return
     */
    private static String getMethodName(String fildeName) {
        byte[] items = fildeName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }

}

