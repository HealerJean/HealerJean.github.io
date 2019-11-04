package com.healerjean.proj.a_test.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author HealerJean
 * @ClassName Group
 * @date 2019/10/29  22:20.
 * @Description
 */
@Data
// @JacksonXmlRootElement：指定生成xml根标签的名字；
@JacksonXmlRootElement(localName = "Class")
public class Group {

    private Teacher teacher;

    // @JacksonXmlElementWrapper：可用于指定List等集合类，外围标签名；
    @JacksonXmlElementWrapper(localName = "Students")
    // @JacksonXmlProperty：指定包装标签名，或者指定标签内部属性名；
    @JacksonXmlProperty(localName = "Stu")
    private List<Student> student;

    @Data
    @AllArgsConstructor
    public static class TeacherType {
        @JacksonXmlProperty(isAttribute = true, localName = "type")
        private String type;
        //@JacksonXmlText：指定当前这个值，没有xml标签包裹。
        // 这个值意义在于,一般这个值所在的类只有这一个正常属性，或者其他属性全部为  @JacksonXmlProperty(isAttribute = true,
        @JacksonXmlText
        private String grade;

    }

    @Data
    public static class Teacher {
        @JacksonXmlProperty(localName = "TypeCode")
        private TeacherType teacherTypeCode;
        private String name;
    }

    @Data
    public static class Student {
        @JacksonXmlProperty(isAttribute = true, localName = "stu_id")
        private String id;
        private String name;
        private Integer age;
    }

}
