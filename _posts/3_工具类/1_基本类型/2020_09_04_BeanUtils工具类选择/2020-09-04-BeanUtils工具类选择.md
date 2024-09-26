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



# 一、`BeanUtils` 复制字段值



## 1、`org.springframework.beans.BeanUtils`

> `a` 拷贝到 `b`

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

 

 

## 2、`org.apache.commons.beanutils.BeanUtils ` 

> `b` 拷贝到 `a 





# 二、`MapStruct`



## 1、对象构建

### 1）、`pom`依赖

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



### 2）`pojo`：`DictionaryType`

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





### 3）`DTO`：`DictionaryTypeDTO`

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



### 4）枚举 `SystemEmum`

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



## 2、使用说明

### 1）、字段映射

#### a、嵌套字段映射

```java
@Mapper
public interface FishTankMapper {
  @Mapping(target = "fish.kind", source = "fish.type")
  @Mapping(target = "fish.name", ignore = true)
  @Mapping(target = "ornament", source = "interior.ornament")
  @Mapping(target = "material.materialType", source = "material")
  @Mapping(target = "quality.report.organisation.name", source = "quality.report.organisationName")
  FishTankDto map(FishTank source );
  
}
```



#### b、忽略字段映射

```java
@Mapping(target = "id", ignore = true)
@Mapping(target = "ruleId", source = "rulesDO.id")
MessageDO voRule2Do(MessageVO messageVO, RulesDO rulesDO);
```



### 2）自定义映射 `@Named`

#### a、日期&枚举

**1、转化类 `BeanTransfer`**

```java
package com.healerjean.proj.beanmap.transfer;

import com.healerjean.proj.enmus.MapperNamedConstant;
import com.healerjean.proj.enmus.SystemEmum;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * BeanTransfer
 *
 * @author HealerJean
 * @date 2023-06-19 06:06:56
 */
public interface BeanTransfer {

    /**
     * Date和LocalDateTime互转
     */
    class TransferDateAndLocalDateTime implements BeanTransfer {

        @Named(MapperNamedConstant.METHOD_DATE_TO_LOCAL_DATE_TIME)
        public static LocalDateTime toLocalDateTime(Date date) {
            if (date == null) {
                return null;
            }
            Instant instant = date.toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            return instant.atZone(zoneId).toLocalDateTime();
        }


        @Named(MapperNamedConstant.METHOD_LOCAL_DATE_TIME_TO_DATE)
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
     class TransferSexEnum  implements BeanTransfer{

        @Named(MapperNamedConstant.METHOD_SEX_CODE_TO_ENUM)
        public SystemEmum.SexEnum sexCodeToEnum(Integer code) {
            return SystemEmum.SexEnum.to(code);
        }

        @Named(MapperNamedConstant.METHOD_SEX_ENUM_TO_CODE)
        public Integer sexEnumToCode(SystemEmum.SexEnum sexEnum) {
            return sexEnum.getCode();
        }
    }


}

```



**2、转化常亮定义`MapperNamedConstant`**

```java
package com.healerjean.proj.enmus;

/**
 * MapperNamedConstant
 *
 * @author HealerJean
 * @date 2023-06-19 06:06:51
 */
public interface MapperNamedConstant {

    /**
     * METHOD_SEX_CODE_TO_ENUM
     */
    String METHOD_SEX_CODE_TO_ENUM = "sexCodeToEnumMethod";
    /**
     * METHOD_SEX_ENUM_TO_CODE
     */
    String METHOD_SEX_ENUM_TO_CODE = "sexEnumToCodeMethod";


    /**
     * METHOD_DATE_TO_LOCAL_DATE_TIME
     */
    String METHOD_DATE_TO_LOCAL_DATE_TIME = "dateToLocalDateTimeMethod";
    /**
     * METHOD_LOCAL_DATE_TIME_TO_DATE
     */
    String METHOD_LOCAL_DATE_TIME_TO_DATE = "localDateTimeToDateMethod";

}

```

**3、`BeanUtils` 工具类**

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

**4、`MapStrut`生成的类**

```java
package com.healerjean.proj.beanmap;

import com.healerjean.proj.beanmap.transfer.BeanTransfer.TransferDateAndLocalDateTime;
import com.healerjean.proj.beanmap.transfer.BeanTransfer.TransferSexEnum;
import com.healerjean.proj.dto.DictionaryType;
import com.healerjean.proj.pojo.DictionaryTypeDTO;

import javax.annotation.Generated;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2024-09-10T13:26:21+0800",
        comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_271 (Oracle Corporation)"
)
public class BeanUtilsImpl implements BeanUtils {

    private final TransferSexEnum transferSexEnum = new TransferSexEnum();

    @Override
    public DictionaryTypeDTO dictionaryType2DTO(DictionaryType dictionaryType) {
        if (dictionaryType == null) {
            return null;
        }

        DictionaryTypeDTO dictionaryTypeDTO = new DictionaryTypeDTO();

        dictionaryTypeDTO.setId(dictionaryType.getDictionaryTypeId());
        dictionaryTypeDTO.setSexEnum(transferSexEnum.sexCodeToEnum(dictionaryType.getSex()));
      
        dictionaryTypeDTO
          .setCreateTime(TransferDateAndLocalDateTime.toLocalDateTime(dictionaryType.getCreateTime()));
        dictionaryTypeDTO.setTypeKey(dictionaryType.getTypeKey());
        dictionaryTypeDTO.setTypeDesc(dictionaryType.getTypeDesc());

        return dictionaryTypeDTO;
    }
}

```





### 3）常数 `constant`

```java
@Mapper
public interface SourceTargetMapper {
    SourceTargetMapper INSTANCE = Mappers.getMapper( SourceTargetMapper.class );
    @Mapping(target = "stringConstant", constant = "Constant Value")
    @Mapping(target = "integerConstant", constant = "14")
    @Mapping(target = "longWrapperConstant", constant = "3001")
    @Mapping(target = "dateConstant", dateFormat = "dd-MM-yyyy", constant = "09-01-2014")
    Target sourceToTarget(Source s);
}
```





### 4）默认值 `defaultValue`

> 当 `source` 对象的对应字段为 `null` 时，`defaultValue` 指定的默认值会被放入 `target `的对应字段。

```java
@Mapper
public interface SourceTargetMapper {
    SourceTargetMapper INSTANCE = Mappers.getMapper( SourceTargetMapper.class );
    @Mapping(target = "stringProperty", source = "stringProp", defaultValue = "undefined")
    @Mapping(target = "longProperty", source = "longProp", defaultValue = "-1")
    @Mapping(target = "booleanProperty", defaultValue = "false")
    Target sourceToTarget(Source s);
}
```



### 5）更新 不是新生成

> 如果不想自动生成一个新的 `target` 实例，而是更新参数传入的 `target` 实例，可以给 `target` 增加 `@MappingTarget` 注解，此时返回的是传入的 target 对象

```java
@Mapper
interface DeliveryAddressMapper {
    @Mapping(source = "address.postalcode", target = "postalcode")
    @Mapping(source = "address.county", target = "county")
    DeliveryAddress updateAddress(@MappingTarget DeliveryAddress deliveryAddress, Address address);
}
```





## 3、注意事项

### 1）浅拷贝非深拷贝

> `MapStruct` 在进行对象拷贝时，主要执行的是**浅拷贝**操作。浅拷贝意味着它会创建目标对象的一个新实例，并复制源对象的所有属性值。然而，如果源对象的属性值是引用类型（如对象、数组等），则 `MapStruct` 会复制这个引用地址，而不是引用的对象本身。因此，如果源对象中的引用类型属性发生了变化，那么这些变化也会反映到通过 `MapStruct` 拷贝得到的目标对象中。   
>
> > 1、具体来说，`MapStruct通` 过注解处理器在编译时自动生成映射代码，这些代码会实现将源对象的属性值复制到目标对象的相应属性中。对于基本数据类型（如 `int`、`double` 等），`MapStruct` 会进行值传递；而对于引用数据类型，则进行的是引用传递，即复制引用地址。      
> >
> > 2、虽然 `MapStruct` 本身主要执行浅拷贝，但开发者可以通过自定义转换器或转换逻辑来实现深拷贝的效果。例如，在映射接口中使用 `@AfterMapping` 注解来执行额外的逻辑，或者在映射方法中直接调用深拷贝方法。



**反例：**

```java
public static void main(String[] args) {
      OrderMessage orderMessage  = new OrderMessage();
      ContractSignDTO contractSign = new ContractSignDTO();
      contractSign.setContractNo("ooooooo");
      orderMessage.setContractSign(contractSign);
      OrderMessage newOrderMessage = OrderMessageConverter.INSTANCE.convertToOrderMessage(orderMessage);

      ContractSignDTO newContractSign = newOrderMessage.getContractSign();
      newContractSign.setContractNo("newnewnewnew");
      System.out.println(JSON.toJSONString(orderMessage));
      System.out.println(JSON.toJSONString(newOrderMessage));
      // 二者结果一模一样
  		
  }
```

**正例：**

> `SerializationUtils` 是  `Apache` `Commons` `Lang` 库中的一个工具类，它提供了基于 Java 序列化机制的对象序列化和反序列化功能。虽 然 `SerializationUtils` 本质上是通过序列化/反序列化来实现对象的深拷贝，但它为开发者提供了一个简洁的 API 来完成这一任务，而无需直接处理 `ObjectOutputStream` 和 `ObjectInputStream`。    
>
> > 注意：`SerializationUtils.clone(Object)` 方法在内部使用了 `Java` 的序列化机制来创建对象的深拷贝。因此，它要求被拷贝的对象及其所有非瞬态（`non`-`transient`）和非静态（`non`-`static`）字段都必须实现了 `Serializable` 接口。如果对象图中有任何字段没有实现 `Serializable` 接口，或者包含了不支持序列化的类型（如 `java.io.File`），那么序列化过程将会失败，并抛出 `NotSerializableException` 或其他异常。

```java
    public static void main(String[] args) {
        OrderMessage orderMessage  = new OrderMessage();
        ContractSignDTO contractSign = new ContractSignDTO();
        contractSign.setContractNo("ooooooo");
        contractSign.setOrderIds(Lists.newArrayList("1","2"));
        orderMessage.setContractSign(contractSign);
        OrderMessage deepCopyOrderMessage = SerializationUtils.clone(orderMessage);

        ContractSignDTO deepCopyContractSign = deepCopyOrderMessage.getContractSign();
        deepCopyContractSign.setContractNo("deepCopydeepCopy");

        System.out.println(JSON.toJSONString(orderMessage));
        //{"contractSign":{"contractNo":"ooooooo","orderIds":["1","2"]},"factoryShipFlag":false,"paymentType":0,"productsNum":0}
        System.out.println(JSON.toJSONString(deepCopyContractSign));
       // {"contractNo":"deepCopydeepCopy","orderIds":["1","2"]} 会看到上面基本类型没有考虑过来     


    }
```



**说明：**

```java
public class ExampleClass implements Serializable {  
    private static final long serialVersionUID = 1L;  
  
    // 静态字段，不会被序列化  
    private static String staticField = "Static Value";  
  
    // 瞬态字段，不会被序列化  
    private transient String transientField = "Transient Value";  
  
    // 非瞬态且非静态字段，会被序列化  
    private String nonTransientNonStaticField = "Non-Transient, Non-Static Value";  
  
    // 不会被序列化
    private int baseInt; 
  
    // ... 其他代码，如getter和setter ...  
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
