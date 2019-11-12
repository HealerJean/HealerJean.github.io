package com.healerjean.proj.reflect.field;

import com.healerjean.proj.reflect.FatherDTO;
import com.healerjean.proj.reflect.ReflectDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @author HealerJean
 * @ClassName D01_FieldMain
 * @date 2019/11/12  11:14.
 * @Description
 */
@Slf4j
public class D01_FieldMain {



    /**
     * 1、getFields()
     * + 只能访问类中声明为public的字段
     * + 能访问从其它类继承来的public的字段
     * + 父亲(包括祖父)和自己内部有同一个字段名，都能获取到，并且会在数组中出现多次
     */
    @Test
    public void testGetField() {
        Class reflectDTOClass = ReflectDTO.class;

        Field[] fields = reflectDTOClass.getFields();
        Arrays.stream(fields).forEach(field -> log.info(field.getName()));

        // publicid
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


}
