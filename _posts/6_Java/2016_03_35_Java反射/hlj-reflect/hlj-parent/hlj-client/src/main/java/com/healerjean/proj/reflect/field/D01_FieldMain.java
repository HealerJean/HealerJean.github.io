package com.healerjean.proj.reflect.field;

import com.healerjean.proj.common.exception.BusinessException;
import com.healerjean.proj.reflect.ReflectDTO;
import com.healerjean.proj.util.EmptyUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author HealerJean
 * @ClassName D01_FieldMain
 * @date 2019/11/12  11:14.
 * @Description
 */
@Slf4j
public class D01_FieldMain {

    /** 1、getFields()
     * 1、能访问类中声明为public的字段**
     * 2、能访问其它类继承来的public的字段**
     *（父亲(包括祖父)和自己内部有同一个字段名，都能获取到）,**
     */
    @Test
    public void testGetFields() {
        Class reflectDTOClass = ReflectDTO.class;

        Field[] fields = reflectDTOClass.getFields();
        Arrays.stream(fields).forEach(field -> log.info(field.getName()));

        // publicId
        // publicName
        // publicAge
        // publicMoney
        // publicDate
        // publicId
        // publicName
        // publicFatherVar
        // publicId
        // publicName
        // publicGrandVar
    }


    /**
     * 1.2、getDeclaredFields ：能够访问类中所有的字段，不能方位其他类继承的字段
     */
    @Test
    public void testGetDeclaredFields() {
        Class reflectDTOClass = ReflectDTO.class;

        Field[] fields = reflectDTOClass.getDeclaredFields();
        Arrays.stream(fields).forEach(field -> log.info(field.getName()));

        // privateId
        // privateName
        // privateAge
        // privateMoney
        // privateDate
        // publicid
        // publicName
        // publicAge
        // publicMoney
        // publicDate
    }


    /**
     * 1.3、反射获取该类所有字段属性的值（包括从父类继承来的）
     */
    @Test
    public void getAllFiled() throws IllegalAccessException {
        ReflectDTO obj = new ReflectDTO();
        obj.setPrivateId(0L);
        obj.setPrivateName("");
        obj.setPrivateAge(0);
        obj.setPrivateMoney(new BigDecimal("0"));
        obj.setPrivateDate(new Date());
        obj.setPublicName("");
        obj.setPublicAge(0);
        obj.setPublicMoney(new BigDecimal("0"));
        obj.setPublicDate(new Date());
        obj.setPrivatefatherVar("father");
        obj.setPublicFatherVar("");
        obj.setPrivateGrandVar("grandVar");
        obj.setPublicGrandVar("");

        Set<Field> allFields = new HashSet<>();
        Class tempClass = obj.getClass();
        while (tempClass != null && !tempClass.getName().toLowerCase().equals("java.lang.object")) {
            allFields.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass();
        }
        Set<String> nameSet = new HashSet<>();
        if (!EmptyUtil.isEmpty(allFields)) {
            for (Field field : allFields) {
                field.setAccessible(true);
                if (nameSet.add(field.getName())) {
                    if (!"serialVersionUID".equals(field.getName()) && field.get(obj) != null) {
                        log.info("字段名：【{}】；值：【{}】", field.getName(), field.get(obj));
                    }
                }
            }
        }
    }


    /**
     * 2、getField() getDeclaredField()
     * getField 只能取的共有属性，不能取得私有属性
     * getDeclaredField 通过暴力反射可以获取私有属性
     */
    @Test
    public void test4() throws NoSuchFieldException, IllegalAccessException {
        ReflectDTO obj = new ReflectDTO();
        obj.setPrivateName("privateName_VALUE");
        obj.setPublicName("publicName_VALUE");

        Field publicFieldName = obj.getClass().getField("publicName");
        log.info("getField获取公有属性：【{}】", publicFieldName.get(obj));//publicName_VALUE

        Field privateFieldName = obj.getClass().getDeclaredField("privateName");
        //提示有，但是取不出来，报错，所以下面加上暴力反射 java.lang.IllegalAccessException: Class com.healerjean.proj.reflect.field.D01_FieldMain can not access a member of class com.healerjean.proj.reflect.ReflectDTO with modifiers "private"
        privateFieldName.setAccessible(true);
        log.info("getDeclaredField获取私有属性：【{}】", privateFieldName.get(obj));//privateName_VALUE


        // Field fieldName = obj.getClass().getField("privateName");
        // 下面抛出异常getField 不能获取私有属性 java.lang.NoSuchFieldException: privateName
        // log.info("getField获取私有属性：【{}】", fieldName.get(obj));

    }


    /**
     * 3、获取字段类型
     * 1、getType()： 返回class类型
     * 2、getGenericType() ： 返回Type类型
     * 如果属性是一个泛型，从getType（）只能得到这个属性的接口类型。但从getGenericType（）还能得到这个泛型的参数类型。
     * 所以一般情况下使用 getGenericType
     */
    @Test
    public void getType() throws NoSuchFieldException {
        ReflectDTO obj = new ReflectDTO();

        Field nameField = obj.getClass().getField("publicName");
        log.info("String类型：getType :" + nameField.getType().toString());//class java.lang.String
        log.info("String类型：getGenericType :" + nameField.getGenericType().toString());//class java.lang.String

        Field dataField = obj.getClass().getField("publicDate");
        log.info("Date类型：getType :" + dataField.getType());//:class java.util.Date
        log.info("Date类型：getGenericType :" + dataField.getGenericType());//class java.util.Date


        Field listFile = obj.getClass().getField("list");
        log.info("List类型：getType :" + listFile.getType().toString());//interface java.util.List
        log.info("List类型：getGenericType :" + listFile.getGenericType().toString());//java.util.List<java.lang.String>

    }


    /**
     * 4、给对象赋值
     */
    @Test
    public void setValue() {
        ReflectDTO obj = new ReflectDTO();
        Field[] fields = obj.getClass().getDeclaredFields();
        Map<String, String> map = new HashMap<>();
        map.put("publicName", "publicName_value");
        map.put("privateName", "privateName_value");
        Set<String> fieldNames = map.keySet();
        for (String fieldName : fieldNames) {
            for (Field field : fields) {
                if (field.getName().equals(fieldName)) {
                    setFieldValue(field, obj, map.get(fieldName));
                }
            }
        }
        log.info("obj：{}", obj);
    }

    /**
     * 为属性赋值
     *
     * @param field 要赋值的属性
     * @param obj   属性所属对象
     * @param value 值
     */
    private void setFieldValue(Field field, Object obj, String value) {
        try {
            if (field == null || obj == null || value == null || "".equals(value)) {
                return;
            }
            field.setAccessible(true);

            String fieldType = field.getGenericType().toString();
            if ("class java.lang.String".equals(fieldType)) {
                field.set(obj, value);
            }
            if ("class java.lang.Integer".equals(fieldType)) {
                Integer val = Integer.valueOf(value);
                field.set(obj, val);
            }
            if ("class java.math.BigDecimal".equals(fieldType)) {
                BigDecimal bde = new BigDecimal(value);
                field.set(obj, bde);
            }
            if ("class java.util.Date".equals(fieldType)) {
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
                Date date = df.parse(value);
                field.set(obj, date);
            }
        } catch (Exception ex) {
            log.error("对象赋值失败", ex);
            throw new BusinessException("处理失败");
        }
    }


    @Test
    public void oebjectGetField() {
        ReflectDTO obj = new ReflectDTO();
        obj.setPrivateId(0L);
        obj.setPrivateName("");
        obj.setPrivateAge(0);

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            System.out.println(field.getName());
        }
    }

}
