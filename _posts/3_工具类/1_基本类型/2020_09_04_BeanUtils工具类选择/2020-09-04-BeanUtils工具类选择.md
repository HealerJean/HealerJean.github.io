---
title: BeanUtils工具类选择
date: 2020-09-04 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: BeanUtils工具类选择
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 1、BeanUtils 复制字段值



## 1.1、`org.springframework.beans.BeanUtils`

> a拷贝到b

```java
//source 源文件，target 目标文件 
public static void copyProperties(Object  source, Object target)  throws BeansException    {      
    copyProperties(source,  target, null, (String[])null);    
}  
```



```java
AppsApp appsApp = appsAppDAO.findOne(dockedValidate.getTrackId());
AppsAppData data = new AppsAppData();

String[] ignore = new String[]{"userId"};

BeanUtils.copyProperties(appsApp,data,ignore);
```

 

 

## 1.2、`org.apache.commons.beanutils.BeanUtils ` 

> b拷贝到a





# 2、`MapStruct`

## 2.1、`pom`依赖

```xml
<org.mapstruct.version>1.3.0.Final</org.mapstruct.version>

<dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct-jdk8</artifactId>
    <version>${org.mapstruct.version}</version>
</dependency>
<dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct-processor</artifactId>
    <version>${org.mapstruct.version}</version>
</dependency>
```



## 2.2、案例分析

### 2.2.1、`pojo`：`DictionaryType`

```java
@Data
public class DictionaryType implements Serializable {

    /**  主键 */
    private Long dictionaryTypeId;
    /**  字典类型 */
    private String typeKey;
    /**  字典类型 描述 */
    private String typeDesc;
    /**  状态 */
    private String status;
    /**  创建时间 */
    private Date createTime;
    /** 性别 */
    private Integer sex ;

}

```





### 2.2.2、`DTO`：`DictionaryTypeDTO`

```java
@Data
@Accessors(chain = true)
public class DictionaryTypeDTO {

    /** 字典类型Id  */
    private Long id;
    /**  字典类型键 */
    private String typeKey;
    /** 字典类型描述 */
    private String typeDesc;
    // /**  是否分页 true，分页 false 不分页 ,默认分页  */
    private Boolean flag;
    /** 状态   */
    private String status;
    /**  创建时间 */
    private LocalDateTime createTime;
    // 性别
    private SystemEmum.SexEnum sexEnum;
}

```



### 2.2.3、枚举

```java
public interface SystemEmum {


    enum SexEnum implements SystemEmum {

        man(1, "男"),
        woman(0, "女");

        private Integer code;
        private String name;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        SexEnum(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public static SexEnum to(Integer code) {
            for (SexEnum sexEnum : SexEnum.values()) {
                if (sexEnum.code.equals(code)) {
                    return sexEnum;
                }
            }
            return null;
        }
    }
}
```



### 2.2.4、字段转化



```java
public interface BeanTransfer {

    /**
     * Date和LocaDateTime互转
     */
    @Named(BeanUtils.TRANSFER_OF_DATE_AND_LOCAL_DATE_TIME)
    class TransferDateAndLocalDateTime implements BeanTransfer {

        public static final String DateToLocalDateTime = "DateToLocalDateTime";
        public static final String LocalDateTimeToDate = "LocalDateTimeToDate";

        @Named(DateToLocalDateTime)
        public static LocalDateTime toLocalDateTime(Date date) {
            if (date == null) {
                return null;
            }
            Instant instant = date.toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            return instant.atZone(zoneId).toLocalDateTime();
        }


        @Named(LocalDateTimeToDate)
        public static Date toDate(LocalDateTime localDateTime) {
            if (localDateTime == null) {
                return null;
            }
            ZoneId zone = ZoneId.systemDefault();
            Instant instant = localDateTime.atZone(zone).toInstant();
            return Date.from(instant);
        }
    }


    /**
     * Code和枚举互转
     */
    @Named(BeanUtils.TRANSFER_OF_SEX_ENUM)
     class TransferSexEnum  implements BeanTransfer{
        public static final String CODE_TO_SEX_ENUM = "CodeToSexEnum";
        public static final String SEX_ENUM_TO_CODE = "SexEnumToCode";

        @Named(CODE_TO_SEX_ENUM)
        public SystemEmum.SexEnum codeToSexEnum(Integer code) {
            return SystemEmum.SexEnum.to(code);
        }

        @Named(SEX_ENUM_TO_CODE)
        public Integer sexEnumToCode(SystemEmum.SexEnum sexEnum) {
            return sexEnum.getCode();
        }
    }


}
```



### 2.3.5、BeanUtils工具类

```java
@Mapper(uses = {
        BeanTransfer.TransferSexEnum.class,
        BeanTransfer.TransferDateAndLocalDateTime.class
})
public interface BeanUtils {

    BeanUtils MAPPER = Mappers.getMapper(BeanUtils.class);
    String TRANSFER_OF_SEX_ENUM = "sexEnumTransfer";
    String TRANSFER_OF_DATE_AND_LOCAL_DATE_TIME = "DateAndLocalDateTimeTransfer";


    /**
     * 方法名称可任意
     *
     * @param dictionaryType 入参对应要被转化的对象
     * @return 返回值对应转化后的对象
     */
    @Mappings({
            /** 名字不同转化 */
            @Mapping(source = "dictionaryTypeId", target = "id"),
            /** 类型和名字都不同转化 */
            @Mapping(source = "sex", 
                     target = "sexEnum", 
                     qualifiedByName = {TRANSFER_OF_SEX_ENUM, 
                                        BeanTransfer.TransferSexEnum.CODE_TO_SEX_ENUM}),
            @Mapping(source = "createTime", 
                     target = "createTime", 
                     qualifiedByName = {TRANSFER_OF_DATE_AND_LOCAL_DATE_TIME, 
                                        BeanTransfer.TransferDateAndLocalDateTime.DateToLocalDateTime}),
            @Mapping(source = "status", target = "status", ignore = true),
    })
    DictionaryTypeDTO dictionaryType2DTO(DictionaryType dictionaryType);

}

```



### 2.3.6、测试



```java
@Test
public void test() {
    DictionaryType dictionaryType = new DictionaryType();
    dictionaryType.setDictionaryTypeId(1L);
    dictionaryType.setTypeDesc("Loan");
    dictionaryType.setStatus("10");
    dictionaryType.setCreateTime(new Date());
    dictionaryType.setSex(1);
    DictionaryTypeDTO dto = BeanUtils.MAPPER.dictionaryType2DTO(dictionaryType);
    System.out.println(dto);
}
```



#### 2.3.7、`MapStrut`生成的类

```java
package com.healerjean.proj.beanmap;

import com.healerjean.proj.beanmap.transfer.BeanTransfer.TransferSexEnum;
import com.healerjean.proj.dto.DictionaryType;
import com.healerjean.proj.pojo.DictionaryTypeDTO;
import java.time.LocalDateTime;
import java.time.ZoneId;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-04T15:23:22+0800",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_201-2-redhat (Oracle Corporation)"
)
public class BeanUtilsImpl implements BeanUtils {

    private final TransferSexEnum transferSexEnum = new TransferSexEnum();

    @Override
    public DictionaryTypeDTO dictionaryType2DTO(DictionaryType dictionaryType) {
        if ( dictionaryType == null ) {
            return null;
        }

        DictionaryTypeDTO dictionaryTypeDTO = new DictionaryTypeDTO();

        dictionaryTypeDTO.setId( dictionaryType.getDictionaryTypeId() );
        dictionaryTypeDTO.setSexEnum( transferSexEnum.codeToSexEnum( dictionaryType.getSex() ) );
        if (dictionaryType.getCreateTime() != null ) {
            dictionaryTypeDTO.setCreateTime( LocalDateTime.ofInstant( dictionaryType.getCreateTime().toInstant(), 
                                                                     ZoneId.of( "UTC" ) ) );
        }
        dictionaryTypeDTO.setTypeKey( dictionaryType.getTypeKey() );
        dictionaryTypeDTO.setTypeDesc( dictionaryType.getTypeDesc() );

        return dictionaryTypeDTO;
    }
}

```



# 3、代码生成Set、Get



## 3.1、工具类

```java
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



```



## 3.2、Main测试

```java

public static void main(String[] args) {
    CodeAutoUtils.beanCopy(ContractDTO.class, ScfContract.class);
}





target.setCreateTime(source.getCreateTime());
target.setContractNo(source.getContractNo());
target.setHistorySysFiles(source.getHistorySysFiles());
target.setRefNo(source.getRefNo());
target.setType(source.getType());
target.setStatus(source.getStatus());
target.setSponsorRefCompanyId(source.getSponsorRefCompanyId());
target.setDescription(source.getDescription());
target.setName(source.getName());
target.setUserSignStatus(source.getUserSignStatus());
```





![ContactAuthor](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/artical_bottom.jpg)





<link rel="stylesheet" href="https://unpkg.com/gitalk/dist/gitalk.css">

<script src="https://unpkg.com/gitalk@latest/dist/gitalk.min.js"></script> 
<div id="gitalk-container"></div>    
 <script type="text/javascript">
    var gitalk = new Gitalk({
		clientID: `1d164cd85549874d0e3a`,
		clientSecret: `527c3d223d1e6608953e835b547061037d140355`,
		repo: `HealerJean.github.io`,
		owner: 'HealerJean',
		admin: ['HealerJean'],
		id: 'tp76wR2h91lsBXMU',
    });
    gitalk.render('gitalk-container');
</script> 

