package com.fintech.scf.utils;

import com.fintech.scf.service.core.dto.ContractDTO;
import com.xiaomi.utils.conf.FieldName;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName JsonToMarkDownTable
 * @date 2019/6/5  15:38.
 * @Description
 */
public class MarkdownInterUtils {

    private static final String TABLE_HEAD = "|  参数名称  | 参数类型 | 参数长度 | 是否必需 |      说明      |    备注     |\n";
    private static final String TABLE_HEAD_DIVIDING_LINE = "| :--------: | :------: | :------: | :------: | :----------: | :---------: |\n";
    private static final String TITLE = "## 接口名称\n> **说明**\n- 调用地址：URL\n- 调用方式：***METHOD***\n> **请求参数**\n\n";
    private static final String REQUEST_EXAMPLE = "    \n> **请求报文样例**\n\n```\n\n```\n";
    private static final String RESPONSE_PARAMS = "> **响应参数**\n\n\n";
    private static final String RESPONSE_EXAMPLE = "   \n> **响应报文样例**\n\n```\n\n```\n";
    private static final String RESPONSE_CODE = "> **返回码解析**\n\n| 返回码 |含义| 备注 |\n| :----: | :----------------: | :--: |\n|  200 |成功||\n";


    public static void main(String[] args) {
        System.out.println(interMarkdown(ContractDTO.class, ContractDTO.class));
        // System.out.println(interTable(ContractDTO.class));

    }

    /**
     * 制作markdown接口
     *
     * @param requestClass
     * @param responseClass
     * @return
     */
    public static String interMarkdown(Class requestClass, Class responseClass) {
        StringBuilder res = new StringBuilder();
        res.append(TITLE);

        //请求参数
        if (requestClass != null) {
            res.append(TABLE_HEAD);
            res.append(TABLE_HEAD_DIVIDING_LINE);
            String requestTable = table(requestClass);
            res.append(requestTable);
        }
        res.append(REQUEST_EXAMPLE);


        //返回参数
        res.append(RESPONSE_PARAMS);
        res.append(TABLE_HEAD);
        res.append(TABLE_HEAD_DIVIDING_LINE);
        if (responseClass != null) {
            String responseTable = table(responseClass);
            res.append(responseTable);
        }
        res.append("| msg | 字符串 |255| 是| 返回结果 | \n   \n");
        res.append(RESPONSE_EXAMPLE);



        //返回Code
        res.append(RESPONSE_CODE);
        return res.toString();
    }


    /**
     * 制作table表格
     * @param clazz
     * @return
     */
    public static String interTable(Class clazz) {
        StringBuilder res = new StringBuilder();
        res.append(TABLE_HEAD);
        res.append(TABLE_HEAD_DIVIDING_LINE);
        String requestTable = table(clazz);
        res.append(requestTable);
        return res.toString();
    }


    /**
     * 获取所有字段名
     */
    public static Set<Field> getField(Class c) {
        Set<Field> declaredFields = new HashSet<>();
        Class tempClass = c;
        //反射获取父类里面的属性
        while (tempClass != null && !tempClass.getName().toLowerCase().equals("java.lang.object")) {
            declaredFields.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass();
        }
        return declaredFields;
    }

    public static String table(Class clazz) {
        StringBuilder table = new StringBuilder();
        Set<Field> requestFields = getField(clazz);
        for (Field field : requestFields) {
            field.setAccessible(true);
            String fieldType = field.getGenericType().toString();
            //1、参数名称
            table.append("| " + field.getName());
            //2、参数类型
            switch (fieldType) {
                case "class java.lang.Integer":
                case "class java.lang.Long":
                case "class java.math.BigDecimal":
                    table.append("|数字");
                    break;
                case "class java.time.LocalDate":
                case "class java.time.LocalDateTime":
                    table.append("|日期");
                    break;
                case "class java.lang.Boolean":
                    table.append("|布尔");
                    break;
                case "class java.lang.String":
                    table.append("|字符串");
                    break;
                default:
                    if (fieldType.startsWith("java.util.List<java.lang.String>")) {
                        table.append("|字符串集合 ");
                    } else if (fieldType.startsWith("java.util.List<java.lang.Long>") || fieldType.startsWith("java.util.List<java.math.BigDecimal>")) {
                        table.append("|数字集合 ");
                    } else if (fieldType.startsWith("class com.")) {
                        table.append("|对象 ");
                    } else if (fieldType.startsWith("java.util.List<com.")) {
                        table.append("|对象集合 ");
                    } else if (fieldType.startsWith("java.util.List")) {
                        table.append("|集合 ");
                    }
                    break;
            }


            if (field.getAnnotations() != null && field.getAnnotations().length > 0) {
                //3、参数长度
                if (field.isAnnotationPresent(Length.class)) {
                    Length length = field.getAnnotation(Length.class);
                    table.append("|  " + length.max() + "   ");
                } else {
                    table.append("|         ");
                }


                //4、是否必填
                if ((field.isAnnotationPresent(NotBlank.class)) || (field.isAnnotationPresent(NotEmpty.class)) || (field.isAnnotationPresent(NotNull.class))) {
                    table.append("|是");
                } else {
                    table.append("|否");
                }

                //5、说明
                if (field.isAnnotationPresent(FieldName.class)) {
                    FieldName fieldName = field.getAnnotation(FieldName.class);
                    table.append("|  " + fieldName.value() + " |      |\n");
                } else {
                    table.append("|         |      |  \n");
                }
            } else {
                //参数长度，是否必填
                table.append("|      |否   ");
                //说明，备注
                table.append("|         |      |  \n");
            }
        }

        return table.toString();
    }


}


