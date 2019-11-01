package com.healerjean.proj.a_test.xml.jackson;

import com.healerjean.proj.a_test.xml.*;
import com.healerjean.proj.a_test.xml.Group.*;

import com.healerjean.proj.util.xml.XmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author HealerJean
 * @ClassName D01_JackSon_XML
 * @date 2019/10/29  22:11.
 * @Description
 */
@Slf4j
public class D01_JackSon_XML {

    @Test
    public void test() {
        Group group = new Group();
        Group.Teacher teacher = new Teacher();
        teacher.setTeacherTypeCode(new TeacherType("语文老师", "A"));
        teacher.setName("马老师");
        group.setTeacher(teacher);

        Student student = new Student();
        student.setId("001");
        student.setName("HealerJean");
        student.setAge(25);
        group.setStudent(Arrays.asList(student));

        String xml = XmlUtils.toXml(group);
        log.info("数据原文：【 {} 】", xml);


        /**
         * //XML标签名:使用骆驼命名的属性名，
         * xmlMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
         * <Class>
         *     <Teacher>
         *         <Name>马老师</Name>
         *         <TypeCode type="语文老师">A</TypeCode>
         *     </Teacher>
         *     <Students>
         *         <Stu stu_id="001">
         *             <Name>HealerJean</Name>
         *             <Age>25</Age>
         *         </Stu>
         *     </Students>
         * </Class>
         *
         * 自定义的转化转大写并加下划线，强制命令的不可以，比如下面的Class是强制命名的
         *  xmlMapper.setPropertyNamingStrategy(new UpperCaseSnackNamingStrategy());
         *
         * <Class>
         *     <TEACHER>
         *         <NAME>马老师</NAME>
         *         <TypeCode type="语文老师">A</TypeCode>
         *     </TEACHER>
         *     <Students>
         *         <Stu stu_id="001">
         *             <NAME>HealerJean</NAME>
         *             <AGE>25</AGE>
         *         </Stu>
         *     </Students>
         * </Class>
         */

    }

}
