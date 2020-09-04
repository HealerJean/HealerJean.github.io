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

