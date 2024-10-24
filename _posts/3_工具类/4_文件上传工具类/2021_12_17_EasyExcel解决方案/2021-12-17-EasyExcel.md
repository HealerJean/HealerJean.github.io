---
title: EasyExcel
date: 2021-12-17 00:00:00
tags: 
- Java
category: 
- Java
description: EasyExcel
---

# **前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          

`EasyExcel` 的 `github` 地址是：https://github.com/alibaba/easyexcel

# 1、`Read`

```java
private static final String PATH = "/Users/healerjean/Desktop/data/";
private static final String DEMO_EXCEL_FILE = PATH + "demo.xlsx";
```

## 1.1、最简单的读

### 1.1.1、样例

#### 1.1.1.1、`DemoData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class DemoData {
    private String string;
    private Date date;
    private Double doubleData;
}
```

#### 1.1.1.2、`DemoDataListener`

```java
@Service
@Slf4j
public class DemoDataListener implements ReadListener<DemoData> {

    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    /**
     * 缓存的数据
     */
    private List<DemoData> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    /**
     * 这个每一条数据解析都会来调用
     */
    @Override
    public void invoke(DemoData data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JsonUtils.toJsonString(data));
        cachedDataList.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        // TODO
        log.info("存储数据库成功！");
    }
}
```

### 1.1.2、测试

```java
/**
 * 1、最简单的读
 * 1. 创建excel对应的实体对象 参照{@link DemoData}
 * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link DemoDataListener}
 * 3. 直接读即可
 */
public void simpleRead(String filePath) {
  filePath = StringUtils.isBlank(filePath) ? DEMO_EXCEL_FILE : filePath;
  // 1、默认读取一地个sheet
  EasyExcel.read(filePath, DemoData.class, new DemoDataListener()).sheet().doRead();
}
```

#### 1.1.2.1、源文件

![image-20211221145103597](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221145103597.png)



#### 1.1.2.2、输出日志

```
解析到一条数据:{"string":"字符串0","date":1577811661000,"doubleData":1.0}
解析到一条数据:{"string":"字符串1","date":1577898061000,"doubleData":2.0}
解析到一条数据:{"string":"字符串2","date":1577984461000,"doubleData":3.0}
解析到一条数据:{"string":"字符串3","date":1578070861000,"doubleData":4.0}
解析到一条数据:{"string":"字符串4","date":1578157261000,"doubleData":5.0}
解析到一条数据:{"string":"字符串5","date":1578243661000,"doubleData":6.0}
解析到一条数据:{"string":"字符串6","date":1578330061000,"doubleData":7.0}
解析到一条数据:{"string":"字符串7","date":1578416461000,"doubleData":8.0}
解析到一条数据:{"string":"字符串8","date":1578502861000,"doubleData":9.0}
解析到一条数据:{"string":"字符串9","date":1578589261000,"doubleData":10.0}
10条数据，开始存储数据库！
存储数据库成功！
所有数据解析完成！
```



## 1.2、指定列的下标或者列名

### 1.2.1、样例

#### 1.2.1.1、`IndexOrNameData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class IndexOrNameData {
    /**
     * 用名字去匹配，这里需要注意，如果名字重复，会导致只有一个字段读取到数据
     */
    @ExcelProperty("字符串标题")
    private String string;

    /**
     * 不建议 index 和 name 同时用，要么一个对象只用index，要么一个对象只用name去匹配
     */
    @ExcelProperty(index = 2)
    private Double doubleData;

    @ExcelProperty("日期标题")
    private Date date;
}
```

#### 1.2.1.2、`IndexOrNameDataListener`

```java
@Slf4j
public class IndexOrNameDataListener extends AnalysisEventListener<IndexOrNameData> {
    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;
    private List<IndexOrNameData> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    @Override
    public void invoke(IndexOrNameData data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JsonUtils.toJsonString(data));
        cachedDataList.add(data);
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        log.info("存储数据库成功！");
    }
}
```

### 1.2.2、测试

```java
/**
 * 2、指定列的下标或者列名
 */
@Override
public void indexOrNameRead(String filePath) {
    filePath = StringUtils.isBlank(filePath) ? DEMO_EXCEL_FILE : filePath;
    // 这里默认读取第一个sheet
    EasyExcel.read(filePath, IndexOrNameData.class, new IndexOrNameDataListener()).sheet().doRead();
}
```

#### 1.2.2.1、源文件

![image-20211221145103597](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221145103597.png)

#### 1.2.2.2、输出日志

```
解析到一条数据:{"string":"字符串0","doubleData":1.0,"date":1577811661000}
解析到一条数据:{"string":"字符串1","doubleData":2.0,"date":1577898061000}
解析到一条数据:{"string":"字符串2","doubleData":3.0,"date":1577984461000}
解析到一条数据:{"string":"字符串3","doubleData":4.0,"date":1578070861000}
解析到一条数据:{"string":"字符串4","doubleData":5.0,"date":1578157261000}
5条数据，开始存储数据库！
存储数据库成功！
解析到一条数据:{"string":"字符串5","doubleData":6.0,"date":1578243661000}
解析到一条数据:{"string":"字符串6","doubleData":7.0,"date":1578330061000}
解析到一条数据:{"string":"字符串7","doubleData":8.0,"date":1578416461000}
解析到一条数据:{"string":"字符串8","doubleData":9.0,"date":1578502861000}
解析到一条数据:{"string":"字符串9","doubleData":10.0,"date":1578589261000}
5条数据，开始存储数据库！
存储数据库成功！
0条数据，开始存储数据库！
存储数据库成功！
```



## 1.3、读多个或者全部 `sheet`

### 1.3.1、样例

#### 1.3.1.1、`DemoData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class DemoData {
    private String string;
    private Date date;
    private Double doubleData;
}
```

#### 1.2.1.2、`DemoDataListener`

```java
@Service
@Slf4j
public class DemoDataListener implements ReadListener<DemoData> {

    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    /**
     * 缓存的数据
     */
    private List<DemoData> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    /**
     * 这个每一条数据解析都会来调用
     */
    @Override
    public void invoke(DemoData data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JsonUtils.toJsonString(data));
        cachedDataList.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        // TODO
        log.info("存储数据库成功！");
    }
}

```

### 1.3.2、测试

```java
/**
 * 3、读多个或者全部sheet
 */
@Override
public void repeatedRead() {
    // 3.1、读取全部sheet
    // 注意： DemoDataListener的doAfterAllAnalysed 会在每个sheet读取完毕后调用一次。然后所有sheet都会往同一个DemoDataListener里面写
    EasyExcel.read(DEMO_EXCEL_FILE, DemoData.class, new DemoDataListener()).doReadAll();

    // 3.2、读取部分sheet
    ExcelReader excelReader = null;
    try {
        excelReader = EasyExcel.read(DEMO_EXCEL_FILE).build();
        ReadSheet readSheet1 =   EasyExcel.readSheet(0).head(DemoData.class)
          .registerReadListener(new DemoDataListener()).build();
      
        ReadSheet readSheet2 =   EasyExcel.readSheet(1).head(DemoData.class).
          registerReadListener(new DemoDataListener()).build();
        excelReader.read(readSheet1, readSheet2);
    } finally {
        if (excelReader != null) {
            // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
            excelReader.finish();
        }
    }
}
```

#### 1.3.2.1、源文件

![image-20211221145103597](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221145103597.png)

![image-20211221150034294](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221150034294.png)

#### 1.3.2.2、输出日志

```
解析到一条数据:{"string":"字符串0","date":1577811661000,"doubleData":1.0}
解析到一条数据:{"string":"字符串1","date":1577898061000,"doubleData":2.0}
解析到一条数据:{"string":"字符串2","date":1577984461000,"doubleData":3.0}
解析到一条数据:{"string":"字符串3","date":1578070861000,"doubleData":4.0}
解析到一条数据:{"string":"字符串4","date":1578157261000,"doubleData":5.0}
解析到一条数据:{"string":"字符串5","date":1578243661000,"doubleData":6.0}
解析到一条数据:{"string":"字符串6","date":1578330061000,"doubleData":7.0}
解析到一条数据:{"string":"字符串7","date":1578416461000,"doubleData":8.0}
解析到一条数据:{"string":"字符串8","date":1578502861000,"doubleData":9.0}
解析到一条数据:{"string":"字符串9","date":1578589261000,"doubleData":10.0}
10条数据，开始存储数据库！
存储数据库成功！
所有数据解析完成！
解析到一条数据:{"string":"表2字符串0","date":1577811661000,"doubleData":1.0}
解析到一条数据:{"string":"表2字符串1","date":1577898061000,"doubleData":2.0}
解析到一条数据:{"string":"表2字符串2","date":1577984461000,"doubleData":3.0}
解析到一条数据:{"string":"表2字符串3","date":1578070861000,"doubleData":4.0}
解析到一条数据:{"string":"表2字符串4","date":1578157261000,"doubleData":5.0}
解析到一条数据:{"string":"表2字符串5","date":1578243661000,"doubleData":6.0}
解析到一条数据:{"string":"表2字符串6","date":1578330061000,"doubleData":7.0}
解析到一条数据:{"string":"表2字符串7","date":1578416461000,"doubleData":8.0}
解析到一条数据:{"string":"表2字符串8","date":1578502861000,"doubleData":9.0}
解析到一条数据:{"string":"表2字符串9","date":1578589261000,"doubleData":10.0}
20条数据，开始存储数据库！
存储数据库成功！
所有数据解析完成！
解析到一条数据:{"string":"字符串0","date":1577811661000,"doubleData":1.0}
解析到一条数据:{"string":"字符串1","date":1577898061000,"doubleData":2.0}
解析到一条数据:{"string":"字符串2","date":1577984461000,"doubleData":3.0}
解析到一条数据:{"string":"字符串3","date":1578070861000,"doubleData":4.0}
解析到一条数据:{"string":"字符串4","date":1578157261000,"doubleData":5.0}
解析到一条数据:{"string":"字符串5","date":1578243661000,"doubleData":6.0}
解析到一条数据:{"string":"字符串6","date":1578330061000,"doubleData":7.0}
解析到一条数据:{"string":"字符串7","date":1578416461000,"doubleData":8.0}
解析到一条数据:{"string":"字符串8","date":1578502861000,"doubleData":9.0}
解析到一条数据:{"string":"字符串9","date":1578589261000,"doubleData":10.0}
10条数据，开始存储数据库！
存储数据库成功！
所有数据解析完成！
解析到一条数据:{"string":"表2字符串0","date":1577811661000,"doubleData":1.0}
解析到一条数据:{"string":"表2字符串1","date":1577898061000,"doubleData":2.0}
解析到一条数据:{"string":"表2字符串2","date":1577984461000,"doubleData":3.0}
解析到一条数据:{"string":"表2字符串3","date":1578070861000,"doubleData":4.0}
解析到一条数据:{"string":"表2字符串4","date":1578157261000,"doubleData":5.0}
解析到一条数据:{"string":"表2字符串5","date":1578243661000,"doubleData":6.0}
解析到一条数据:{"string":"表2字符串6","date":1578330061000,"doubleData":7.0}
解析到一条数据:{"string":"表2字符串7","date":1578416461000,"doubleData":8.0}
解析到一条数据:{"string":"表2字符串8","date":1578502861000,"doubleData":9.0}
解析到一条数据:{"string":"表2字符串9","date":1578589261000,"doubleData":10.0}
10条数据，开始存储数据库！
存储数据库成功！
所有数据解析完成！
```

## 1.4、日期、数字、自定义格式转化

### 1.4.1、样例

#### 1.4.2.1、`ConverterData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class ConverterData {
    /**
     * 我自定义 转换器，不管数据库传过来什么 。我给他加上“自定义：”
     */
    @ExcelProperty(converter = CustomStringStringConverter.class)
    private String string;
    /**
     * 这里用string 去接日期才能格式化。我想接收年月日格式
     */
    @DateTimeFormat("yyyy年MM月dd日HH时mm分ss秒")
    private String date;
    /**
     * 我想接收百分比的数字 (1,2,3 转化为 100%)
     */
    @NumberFormat("#.##%")
    private String doubleData;
}

```

#### 1.4.2.2、`CustomStringStringConverter`

```java
public class CustomStringStringConverter implements Converter<String> {
    @Override
    public Class<?> supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    /**
     * 这里读的时候会调用
     *
     * @param context
     * @return
     */
    @Override
    public String convertToJavaData(ReadConverterContext<?> context) {
        return "自定义：" + context.getReadCellData().getStringValue();
    }

    /**
     * 这里是写的时候会调用 不用管
     *
     * @return
     */
    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<String> context) {
        return new WriteCellData<>(context.getValue());
    }

}

```

#### 1.4.2.3、`ConverterDataListener`

```java
@Slf4j
public class ConverterDataListener implements ReadListener<ConverterData> {

    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;
    private List<ConverterData> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    @Override
    public void invoke(ConverterData data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JsonUtils.toJsonString(data));
        cachedDataList.add(data);
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        log.info("存储数据库成功！");
    }
}

```

### 1.4.2、测试

```java
/**
 * 4、日期、数字、自定义格式转化
 */
public void converterRead() {
    // 这里 需要指定读用哪个class去读，然后读取第一个sheet
    EasyExcel.read(DEMO_EXCEL_FILE, ConverterData.class, new ConverterDataListener()).sheet().doRead();
}

```



#### 1.4.2.1、源文件

![image-20211221145103597](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221145103597.png)

#### 1.4.2.2、输出日志

```
解析到一条数据:{"string":"自定义：字符串0","date":"2020年01月01日01时01分01秒","doubleData":"100%"}
解析到一条数据:{"string":"自定义：字符串1","date":"2020年01月02日01时01分01秒","doubleData":"200%"}
解析到一条数据:{"string":"自定义：字符串2","date":"2020年01月03日01时01分01秒","doubleData":"300%"}
解析到一条数据:{"string":"自定义：字符串3","date":"2020年01月04日01时01分01秒","doubleData":"400%"}
解析到一条数据:{"string":"自定义：字符串4","date":"2020年01月05日01时01分01秒","doubleData":"500%"}
5条数据，开始存储数据库！
存储数据库成功！
解析到一条数据:{"string":"自定义：字符串5","date":"2020年01月06日01时01分01秒","doubleData":"600%"}
解析到一条数据:{"string":"自定义：字符串6","date":"2020年01月07日01时01分01秒","doubleData":"700%"}
解析到一条数据:{"string":"自定义：字符串7","date":"2020年01月08日01时01分01秒","doubleData":"800%"}
解析到一条数据:{"string":"自定义：字符串8","date":"2020年01月09日01时01分01秒","doubleData":"900%"}
解析到一条数据:{"string":"自定义：字符串9","date":"2020年01月10日01时01分01秒","doubleData":"1000%"}
5条数据，开始存储数据库！
存储数据库成功！
0条数据，开始存储数据库！
存储数据库成功！
所有数据解析完成！
```

## 1.5、多行头 (默认1行，这里指定有2行标题)

### 1.5.1、样例

#### 1.5.1.1、`DemoData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class DemoData {
    private String string;
    private Date date;
    private Double doubleData;
}
```

#### 1.5.1.1、`DemoDataListener`

```java
@Service
@Slf4j
public class DemoDataListener implements ReadListener<DemoData> {

    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    /**
     * 缓存的数据
     */
    private List<DemoData> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    /**
     * 这个每一条数据解析都会来调用
     */
    @Override
    public void invoke(DemoData data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JsonUtils.toJsonString(data));
        cachedDataList.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        // TODO
        log.info("存储数据库成功！");
    }
}

```



### 1.5.2、测试

```java
/**
 * 5、多行头 (默认1行，这里指定有2行标题)
 */
public void complexHeaderRead() {
    EasyExcel.read(DEMO_EXCEL_FILE, DemoData.class, 
    new DemoDataListener()).sheet().headRowNumber(2).doRead();
}
```

#### 1.5.2.1、源文件

![image-20211221145103597](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221145103597.png)

#### 1.5.2.2、输出日志

```
解析到一条数据:{"string":"字符串1","date":1577898061000,"doubleData":2.0}
解析到一条数据:{"string":"字符串2","date":1577984461000,"doubleData":3.0}
解析到一条数据:{"string":"字符串3","date":1578070861000,"doubleData":4.0}
解析到一条数据:{"string":"字符串4","date":1578157261000,"doubleData":5.0}
解析到一条数据:{"string":"字符串5","date":1578243661000,"doubleData":6.0}
解析到一条数据:{"string":"字符串6","date":1578330061000,"doubleData":7.0}
解析到一条数据:{"string":"字符串7","date":1578416461000,"doubleData":8.0}
解析到一条数据:{"string":"字符串8","date":1578502861000,"doubleData":9.0}
解析到一条数据:{"string":"字符串9","date":1578589261000,"doubleData":10.0}
9条数据，开始存储数据库！
存储数据库成功！
所有数据解析完成！
```

## 1.6、读取表头数据

### 1.6.1、样例

#### 1.6.1.1、`DemoData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class DemoData {
    private String string;
    private Date date;
    private Double doubleData;
}

```

#### 1.6.1.2、`DemoHeadDataListener`

```java
@Slf4j
public class DemoHeadDataListener implements ReadListener<DemoData> {
    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;
    private List<ExceptionDemoData> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    /**
     * 在转换异常 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
     *
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) {
        log.error("解析失败，但是继续解析下一行:{}", exception.getMessage());
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException)exception;
            log.error("第{}行，第{}列解析异常，数据为:{}", excelDataConvertException.getRowIndex(),
                excelDataConvertException.getColumnIndex(), excelDataConvertException.getCellData());
        }
    }

    /**
     * 这里会一行行的返回头
     * {"0":{"type":"STRING","stringValue":"字符串标题","dataFormatData":{"index":0,"format":"General"}},"1":{"type":"STRING","stringValue":"日期标题","dataFormatData":{"index":0,"format":"General"}},"2":{"type":"STRING","stringValue":"数字标题","dataFormatData":{"index":0,"format":"General"}}}
     *
     */
    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        log.info("解析到一条头数据:{}", JsonUtils.toJsonString(headMap));
        // 如果想转成成 Map<Integer,String>
        // 方案1： 不要implements ReadListener 而是 extends AnalysisEventListener
        // 方案2： 调用 ConverterUtils.convertToStringMap(headMap, context) 自动会转换
    }

    @Override
    public void invoke(DemoData data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JsonUtils.toJsonString(data));
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        log.info("存储数据库成功！");
    }
}
```

### 1.6.2、测试

```java
/**
 * 6、读取表头数据
 */
@Override
public void headerRead() {
    EasyExcel.read(DEMO_EXCEL_FILE, DemoData.class, new DemoHeadDataListener()).sheet().doRead();
}

```

#### 1.6.2.1、源文件

![image-20211221145103597](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221145103597.png)

#### 1.6.2.2、输出日志

```
解析到一条头数据:{"0":{"type":"STRING","stringValue":"字符串标题","dataFormatData":{"index":0,"format":"General"}},"1":{"type":"STRING","stringValue":"日期标题","dataFormatData":{"index":0,"format":"General"}},"2":{"type":"STRING","stringValue":"数字标题","dataFormatData":{"index":0,"format":"General"}}}
解析到一条数据:{"string":"字符串0","date":1577811661000,"doubleData":1.0}
解析到一条数据:{"string":"字符串1","date":1577898061000,"doubleData":2.0}
解析到一条数据:{"string":"字符串2","date":1577984461000,"doubleData":3.0}
解析到一条数据:{"string":"字符串3","date":1578070861000,"doubleData":4.0}
解析到一条数据:{"string":"字符串4","date":1578157261000,"doubleData":5.0}
解析到一条数据:{"string":"字符串5","date":1578243661000,"doubleData":6.0}
解析到一条数据:{"string":"字符串6","date":1578330061000,"doubleData":7.0}
解析到一条数据:{"string":"字符串7","date":1578416461000,"doubleData":8.0}
解析到一条数据:{"string":"字符串8","date":1578502861000,"doubleData":9.0}
解析到一条数据:{"string":"字符串9","date":1578589261000,"doubleData":10.0}
0条数据，开始存储数据库！
存储数据库成功！
所有数据解析完成！
```



## 1.7、额外信息（批注、超链接、合并单元格信息读取）

### 1.7.1、样例

#### 1.7.1.1、`DemoExtraData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class DemoExtraData {

    private String row1;

    private String row2;
}

```

#### 1.7.1.2、`DemoExtraListener`

```java
@Slf4j
public class DemoExtraListener implements ReadListener<DemoExtraData> {

  @Override
  public void invoke(DemoExtraData data, AnalysisContext context) {}

  @Override
  public void doAfterAllAnalysed(AnalysisContext context) {}

  @Override
  public void extra(CellExtra extra, AnalysisContext context) {
    log.info("读取到了一条额外信息:{}", JsonUtils.toJsonString(extra));
    switch (extra.getType()) {
      case COMMENT:
        log.info("额外信息是批注,在rowIndex:{},columnIndex;{},内容是:{}", 
                 extra.getRowIndex(), extra.getColumnIndex(),
                 extra.getText());
        break;
      case HYPERLINK:
        if ("Sheet1!A1".equals(extra.getText())) {
          log.info("额外信息是超链接,在rowIndex:{},columnIndex;{},内容是:{}", 
                   extra.getRowIndex(),
                   extra.getColumnIndex(), extra.getText());
        } else if ("Sheet2!A1".equals(extra.getText())) {
          log.info(
            "额外信息是超链接,而且覆盖了一个区间,在firstRowIndex:{},firstColumnIndex;{},lastRowIndex:{},lastColumnIndex:{},"
            + "内容是:{}",
            extra.getFirstRowIndex(), 
            extra.getFirstColumnIndex(), extra.getLastRowIndex(),
            extra.getLastColumnIndex(),
            extra.getText());
        } else {
          Assert.fail("Unknown hyperlink!");
        }
        break;
      case MERGE:
        log.info(
          "额外信息是超链接,而且覆盖了一个区间,在firstRowIndex:{},firstColumnIndex;{},lastRowIndex:{},lastColumnIndex:{}",
          extra.getFirstRowIndex(), extra.getFirstColumnIndex(), extra.getLastRowIndex(),
          extra.getLastColumnIndex());
        break;
      default:
    }
  }
}

```



### 1.7.2、测试

#### 1.7.2.1、源文件

![image-20211221150941215](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221150941215.png)

#### 1.7.2.2、输出日志

```
读取到了一条额外信息:{"rowIndex":5,"columnIndex":0,"type":"MERGE","firstRowIndex":5,"lastRowIndex":6,"firstColumnIndex":0,"lastColumnIndex":1}
额外信息是超链接,而且覆盖了一个区间,在firstRowIndex:5,firstColumnIndex;0,lastRowIndex:6,lastColumnIndex:1
读取到了一条额外信息:{"rowIndex":1,"columnIndex":0,"type":"HYPERLINK","text":"Sheet1!A1","firstRowIndex":1,"lastRowIndex":1,"firstColumnIndex":0,"lastColumnIndex":0}
额外信息是超链接,在rowIndex:1,columnIndex;0,内容是:Sheet1!A1
读取到了一条额外信息:{"rowIndex":2,"columnIndex":0,"type":"HYPERLINK","text":"Sheet2!A1","firstRowIndex":2,"lastRowIndex":3,"firstColumnIndex":0,"lastColumnIndex":1}
额外信息是超链接,而且覆盖了一个区间,在firstRowIndex:2,firstColumnIndex;0,lastRowIndex:3,lastColumnIndex:1,内容是:Sheet2!A1
读取到了一条额外信息:{"rowIndex":4,"columnIndex":0,"type":"COMMENT","text":"批注的内容","firstRowIndex":4,"lastRowIndex":4,"firstColumnIndex":0,"lastColumnIndex":0}
额外信息是批注,在rowIndex:4,columnIndex;0,内容是:批注的内容
```



## 1.8、数据转化等异常处理

### 1.8.1、样例

#### 1.8.1.1、`ExceptionDemoData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class ExceptionDemoData {
    /**
     * 用日期去接字符串 肯定报错
     */
    private Date date;
}
```

#### 1.8.1.2、`DemoExceptionListener`

```java
@Slf4j
public class DemoExceptionListener implements ReadListener<ExceptionDemoData> {
    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;

    private List<ExceptionDemoData> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    /**
     * 在转换异常 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
     *
     * @param exception
     * @param context
     * @throws Exception
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) {
        log.error("解析失败，但是继续解析下一行:{}", exception.getMessage());
        // 如果是某一个单元格的转换异常 能获取到具体行号
        // 如果要获取头的信息 配合invokeHeadMap使用
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException)exception;
            log.error("第{}行，第{}列解析异常，数据为:{}", excelDataConvertException.getRowIndex(), excelDataConvertException.getColumnIndex(), excelDataConvertException.getCellData());
        }
    }

    /**
     * 这里会一行行的返回头
     *
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        log.info("解析到一条头数据:{}", JsonUtils.toJsonString(headMap));
    }

    @Override
    public void invoke(ExceptionDemoData data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JsonUtils.toJsonString(data));
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        log.info("存储数据库成功！");
    }
}
```



### 1.8.2、测试

```java
/**
 * 8、数据转化等异常处理
 */
@Override
public void exceptionRead() {
    // 这里 需要指定读用哪个class去读，然后读取第一个sheet
    EasyExcel.read(DEMO_EXCEL_FILE, ExceptionDemoData.class, 
                   new DemoExceptionListener()).sheet().doRead();
}
```



#### 1.8.2.1、源文件

![image-20211221145103597](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221145103597.png)

#### 1.8.2.2、输出日志

```
解析到一条头数据:{"0":{"type":"STRING","stringValue":"字符串标题","dataFormatData":{"index":0,"format":"General"}},"1":{"type":"STRING","stringValue":"日期标题","dataFormatData":{"index":0,"format":"General"}},"2":{"type":"STRING","stringValue":"数字标题","dataFormatData":{"index":0,"format":"General"}}}
解析失败，但是继续解析下一行:Convert data com.alibaba.excel.metadata.data.ReadCellData@75a0fc2c to class java.util.Date error 
第1行，第0列解析异常，数据为:{"type":"STRING","stringValue":"字符串0","dataFormatData":{"index":0,"format":"General"}}
解析失败，但是继续解析下一行:Convert data com.alibaba.excel.metadata.data.ReadCellData@75a41e6f to class java.util.Date error 
第2行，第0列解析异常，数据为:{"type":"STRING","stringValue":"字符串1","dataFormatData":{"index":0,"format":"General"}}
解析失败，但是继续解析下一行:Convert data com.alibaba.excel.metadata.data.ReadCellData@75a740b2 to class java.util.Date error 
第3行，第0列解析异常，数据为:{"type":"STRING","stringValue":"字符串2","dataFormatData":{"index":0,"format":"General"}}
解析失败，但是继续解析下一行:Convert data com.alibaba.excel.metadata.data.ReadCellData@75aa62f5 to class java.util.Date error 
第4行，第0列解析异常，数据为:{"type":"STRING","stringValue":"字符串3","dataFormatData":{"index":0,"format":"General"}}
解析失败，但是继续解析下一行:Convert data com.alibaba.excel.metadata.data.ReadCellData@75ad8538 to class java.util.Date error 
第5行，第0列解析异常，数据为:{"type":"STRING","stringValue":"字符串4","dataFormatData":{"index":0,"format":"General"}}
解析失败，但是继续解析下一行:Convert data com.alibaba.excel.metadata.data.ReadCellData@75b0a77b to class java.util.Date error 
第6行，第0列解析异常，数据为:{"type":"STRING","stringValue":"字符串5","dataFormatData":{"index":0,"format":"General"}}
解析失败，但是继续解析下一行:Convert data com.alibaba.excel.metadata.data.ReadCellData@75b3c9be to class java.util.Date error 
第7行，第0列解析异常，数据为:{"type":"STRING","stringValue":"字符串6","dataFormatData":{"index":0,"format":"General"}}
解析失败，但是继续解析下一行:Convert data com.alibaba.excel.metadata.data.ReadCellData@75b6ec01 to class java.util.Date error 
第8行，第0列解析异常，数据为:{"type":"STRING","stringValue":"字符串7","dataFormatData":{"index":0,"format":"General"}}
解析失败，但是继续解析下一行:Convert data com.alibaba.excel.metadata.data.ReadCellData@75ba0e44 to class java.util.Date error 
第9行，第0列解析异常，数据为:{"type":"STRING","stringValue":"字符串8","dataFormatData":{"index":0,"format":"General"}}
解析失败，但是继续解析下一行:Convert data com.alibaba.excel.metadata.data.ReadCellData@75bd3087 to class java.util.Date error 
第10行，第0列解析异常，数据为:{"type":"STRING","stringValue":"字符串9","dataFormatData":{"index":0,"format":"General"}}
0条数据，开始存储数据库！
存储数据库成功！
所有数据解析完成！
```



## 1.9、同步的返回

> 不推荐使用，如果数据量大会把数据放到内存里面

### 1.9.1、样例

#### 1.9.1.1、`DemoData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class DemoData {
    private String string;
    private Date date;
    private Double doubleData;
}
```

### 1.9.2、测试

```java
/**
 * 9、同步的返回，不推荐使用，如果数据量大会把数据放到内存里面
 */
@Override
public void synchronousRead() {

    // 这里 需要指定读用哪个class去读，然后读取第一个sheet 同步读取会自动finish
    List<DemoData> list = EasyExcel.read(DEMO_EXCEL_FILE).head(DemoData.class).sheet().doReadSync();
    for (DemoData data : list) {
        log.info("读取到数据:{}", JsonUtils.toJsonString(data));
    }

    // 这里 也可以不指定class，返回一个list，然后读取第一个sheet 同步读取会自动finish
    List<Map<Integer, String>> listMap = EasyExcel.read(DEMO_EXCEL_FILE).sheet().doReadSync();
    for (Map<Integer, String> data : listMap) {
        // 返回每条数据的键值对 表示所在的列 和所在列的值
        log.info("读取到数据:{}", JsonUtils.toJsonString(data));
    }
}
```



#### 1.9.2.1、源文件

![image-20211221145103597](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221145103597.png)



#### 1.9.2.2、输出日志

```
读取到数据:{"string":"字符串0","date":1577811661000,"doubleData":1.0}
读取到数据:{"string":"字符串1","date":1577898061000,"doubleData":2.0}
读取到数据:{"string":"字符串2","date":1577984461000,"doubleData":3.0}
读取到数据:{"string":"字符串3","date":1578070861000,"doubleData":4.0}
读取到数据:{"string":"字符串4","date":1578157261000,"doubleData":5.0}
读取到数据:{"string":"字符串5","date":1578243661000,"doubleData":6.0}
读取到数据:{"string":"字符串6","date":1578330061000,"doubleData":7.0}
读取到数据:{"string":"字符串7","date":1578416461000,"doubleData":8.0}
读取到数据:{"string":"字符串8","date":1578502861000,"doubleData":9.0}
读取到数据:{"string":"字符串9","date":1578589261000,"doubleData":10.0}
读取到数据:{"0":"字符串0","1":"2020-1-1 1:01","2":"1"}
读取到数据:{"0":"字符串1","1":"2020-1-2 1:01","2":"2"}
读取到数据:{"0":"字符串2","1":"2020-1-3 1:01","2":"3"}
读取到数据:{"0":"字符串3","1":"2020-1-4 1:01","2":"4"}
读取到数据:{"0":"字符串4","1":"2020-1-5 1:01","2":"5"}
读取到数据:{"0":"字符串5","1":"2020-1-6 1:01","2":"6"}
读取到数据:{"0":"字符串6","1":"2020-1-7 1:01","2":"7"}
读取到数据:{"0":"字符串7","1":"2020-1-8 1:01","2":"8"}
读取到数据:{"0":"字符串8","1":"2020-1-9 1:01","2":"9"}
读取到数据:{"0":"字符串9","1":"2020-1-10 1:01","2":"10"}
```

## 1.10、不创建对象的读

### 1.10.1、样例

#### 1.10.1.1、`NoModelDataListener`

```java
@Slf4j
public class NoModelDataListener extends AnalysisEventListener<Map<Integer, String>> {
    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;
    // Map<KEY(列索引)>
    private List<Map<Integer, String>> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JsonUtils.toJsonString(data));
        cachedDataList.add(data);
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        log.info("存储数据库成功！");
    }
}
```



### 1.10.2、测试

```java
/**
 * 10、不创建对象的读
 */
@Override
public void noModelRead() {
    EasyExcel.read(DEMO_EXCEL_FILE, new NoModelDataListener()).sheet().doRead();
}
```



#### 1.10.2.1、源文件

![image-20211221145103597](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221145103597.png)

#### 1.10.2.2、输出日志

```
解析到一条数据:{"0":"字符串0","1":"2020-1-1 1:01","2":"1"}
解析到一条数据:{"0":"字符串1","1":"2020-1-2 1:01","2":"2"}
解析到一条数据:{"0":"字符串2","1":"2020-1-3 1:01","2":"3"}
解析到一条数据:{"0":"字符串3","1":"2020-1-4 1:01","2":"4"}
解析到一条数据:{"0":"字符串4","1":"2020-1-5 1:01","2":"5"}
5条数据，开始存储数据库！
存储数据库成功！
解析到一条数据:{"0":"字符串5","1":"2020-1-6 1:01","2":"6"}
解析到一条数据:{"0":"字符串6","1":"2020-1-7 1:01","2":"7"}
解析到一条数据:{"0":"字符串7","1":"2020-1-8 1:01","2":"8"}
解析到一条数据:{"0":"字符串8","1":"2020-1-9 1:01","2":"9"}
解析到一条数据:{"0":"字符串9","1":"2020-1-10 1:01","2":"10"}
5条数据，开始存储数据库！
存储数据库成功！
0条数据，开始存储数据库！
存储数据库成功！
所有数据解析完成！
```



# 2、`Fill`

```java

private static final String PATH = "/Users/healerjean/Desktop/data/fill/";
private static final String CURRENT_TIME_MILLIS_NAME = "CURRENT_TIME_MILLIS";

private static final String FILL_SIMPLE_EXCEL_FILE = PATH + "simple.xlsx";
private static final String FILL_SIMPLE_EXCEL_RESULT_FILE = PATH + "simpleFill_" + CURRENT_TIME_MILLIS_NAME + ".xlsx";

private static final String FILL_LIST_EXCEL_FILE = PATH + "list.xlsx";
private static final String FILL_LIST_EXCEL_RESULT_FILE = PATH + "listFill_" + CURRENT_TIME_MILLIS_NAME + ".xlsx";

private static final String FILL_COMPLEX_EXCEL_FILE = PATH + "complex.xlsx";
private static final String FILL_COMPLEX_EXCEL_RESULT_FILE = PATH + "complexFill_" + CURRENT_TIME_MILLIS_NAME + ".xlsx";

private static final String FILL_COMPLEXFILLWITHTABLE_EXCEL_FILE = PATH + "complexFillWithTable.xlsx";
private static final String FILL_COMPLEXFILLWITHTABLE_EXCEL_RESULT_FILE = PATH + "complexFillWithTableFill_" + CURRENT_TIME_MILLIS_NAME + ".xlsx";

private static final String FILL_HORIZONTAL_EXCEL_FILE = PATH + "horizontal.xlsx";
private static final String FILL_HORIZONTAL_EXCEL_RESULT_FILE = PATH + "horizontalFill_" + CURRENT_TIME_MILLIS_NAME + ".xlsx";

private static final String FILL_COMPOSITE_EXCEL_FILE = PATH + "composite.xlsx";
private static final String FILL_COMPOSITE_EXCEL_RESULT_FILE = PATH + "compositeFill_" + CURRENT_TIME_MILLIS_NAME + ".xlsx";



```

## 2.1、最简单的填充（所有字段都填充）

### 2.1.1、样例

#### 2.1.1.1、`FillData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class FillData {
    private String name;
    private double number;
    private Date date;
}

```

### 2.2.2、测试

```JAVA
/**
 * 1、最简单的填充（所有字段都填充）
 * 注意: 用{} 来表示你要用的变量 如果本来就有"{","}" 特殊字符 用"\{","\}"代替
 */
@Override
public void simpleFill() {
    // 方案1 根据对象填充
    String newFileName = FILL_SIMPLE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
    FillData fillData = new FillData();
    fillData.setName("张三");
    fillData.setNumber(5.2);
    EasyExcel.write(newFileName).withTemplate(FILL_SIMPLE_EXCEL_FILE).sheet().doFill(fillData);

    // 方案2 根据Map填充
    newFileName = FILL_SIMPLE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
    Map<String, Object> map = MapUtils.newHashMap();
    map.put("name", "张三");
    map.put("number", 5.2);
    EasyExcel.write(newFileName).withTemplate(FILL_SIMPLE_EXCEL_FILE).sheet().doFill(map);
}
```

#### 2.2.2.1、源文件

![image-20211221164135747](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221164135747.png)

#### 2.2.2.1、方案一

![image-20211221164600921](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221164600921.png)

#### 2.2.2.2、方案2

![image-20211221164640213](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221164640213.png)

## 2.2、填充列表

### 2.2.1、样例

#### 2.2.2.1、`FillData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class FillData {
    private String name;
    private double number;
    private Date date;
}
```



### 2.2.2、测试

```java
/**
 * 2、填充列表
 * 注意： 填充list 的时候还要注意 模板中{.} 多了个点 表示list
 */
@Override
public void listFill() {
    // 方案1 一下子全部放到内存里面 并填充
    String newFileName = FILL_LIST_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, 
                                                             String.valueOf(System.currentTimeMillis()));
    EasyExcel.write(newFileName).withTemplate(FILL_LIST_EXCEL_FILE).sheet().doFill(data());

    // 方案2 分多次 填充 会使用文件缓存（省内存）
    newFileName = FILL_LIST_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, 
                                                      String.valueOf(System.currentTimeMillis()));
    EasyExcel.write(newFileName).withTemplate(FILL_LIST_EXCEL_FILE).sheet().doFill(() -> data());

    // 方案3 分多次 填充 会使用文件缓存（省内存）
    newFileName = FILL_LIST_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, 
                                                      String.valueOf(System.currentTimeMillis()));
    ExcelWriter excelWriter = EasyExcel.write(newFileName).withTemplate(FILL_LIST_EXCEL_FILE).build();
    WriteSheet writeSheet = EasyExcel.writerSheet().build();
    excelWriter.fill(data(), writeSheet);
    excelWriter.fill(data(), writeSheet);
    // 千万别忘记关闭流
    excelWriter.finish();
}

private List<FillData> data() {
  List<FillData> list = Lists.newArrayList();
  for (int i = 0; i < 10; i++) {
    FillData fillData = new FillData();
    list.add(fillData);
    fillData.setName("张三");
    fillData.setNumber(5.2);
    fillData.setDate(new Date());
  }
  return list;
}
```

#### 2.2.2.1、源文件

![image-20211221164349940](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221164349940.png)

#### 2.2.2.2、方案一

![image-20211221165036435](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221165036435.png)

#### 2.2.2.3、方案二

![image-20211221165052604](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221165052604.png)

#### 2.2.2.4、方案三

![image-20211221165111088](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221165111088.png)



## 2.3、复杂的填充

### 2.3.1、样例

#### 2.3.1.1、`FillData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class FillData {
    private String name;
    private double number;
    private Date date;
}

```

### 2.3.2、测试

```java
/**
  * 3、复杂的填充
  *  注意： 1.入参用了forceNewRow 代表在写入list的时候不管list下面有没有空行 都会创建一行，然后下面的数据往后移动。
  *        2.默认 是false，会直接使用下一行，如果没有则创建。
  *        3.forceNewRow 如果设置了true,有个缺点 就是他会把所有的数据都放到内存了，所以慎用
  *        4.简单的说 如果你的模板有list,且list不是最后一行，下面还有数据需要填充 就必须设置 forceNewRow=true 但是这个就会把所有数据放到内存 会很耗内存
  *        5.如果数据量大 list不是最后一行 参照下一个用例方法
  */
 @Override
 public void complexFill() {
     String newFileName = FILL_COMPLEX_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
     ExcelWriter excelWriter = EasyExcel.write(newFileName).withTemplate(FILL_COMPLEX_EXCEL_FILE).build();
     WriteSheet writeSheet = EasyExcel.writerSheet().build();

     FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
     excelWriter.fill(data(), fillConfig, writeSheet);
     excelWriter.fill(data(), fillConfig, writeSheet);

     Map<String, Object> map = MapUtils.newHashMap();
     map.put("date", "2019年10月9日13:28:28");
     map.put("total", 1000);
     excelWriter.fill(map, writeSheet);
     excelWriter.finish();
 }


private List<FillData> data() {
  List<FillData> list = Lists.newArrayList();
  for (int i = 0; i < 10; i++) {
    FillData fillData = new FillData();
    list.add(fillData);
    fillData.setName("张三");
    fillData.setNumber(5.2);
    fillData.setDate(new Date());
  }
  return list;
}
```

#### 2.3.2.1、源文件

![image-20211221165222686](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221165222686.png)

#### 2.3.2.2、输出结果

![image-20211221165712988](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221165712988.png)



## 2.4、数据量大的复杂填充

### 2.4.1、测试

```java
/**
 * 4、数据量大的复杂填充
 * 说明：这里模板 删除了list以后的数据，也就是统计的这一行，所以和3中的模板本身就不一样，也不知道解决了个啥
 * 作者说明：总体上写法比较复杂 但是也没有想到好的版本 异步的去写入excel 不支持行的删除和移动，也不支持备注这种的写入，所以也排除了可以
 *          新建一个 然后一点点复制过来的方案，最后导致list需要新增行的时候，后面的列的数据没法后移，后续会继续想想解决方案
 */
@Override
public void complexFillWithTable() {
    String newFileName = FILL_COMPLEXFILLWITHTABLE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
    ExcelWriter excelWriter = EasyExcel.write(newFileName).withTemplate(FILL_COMPLEXFILLWITHTABLE_EXCEL_FILE).build();
    WriteSheet writeSheet = EasyExcel.writerSheet().build();
    excelWriter.fill(data(), writeSheet);
    excelWriter.fill(data(), writeSheet);
    // 写入list之前的数据
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("date", "2019年10月9日13:28:28");
    excelWriter.fill(map, writeSheet);

    List<List<String>> totalListList = ListUtils.newArrayList();
    List<String> totalList = ListUtils.newArrayList();
    totalListList.add(totalList);
    totalList.add(null);
    totalList.add(null);
    totalList.add(null);
    totalList.add("统计:1000");
    // 这里是write 别和fill 搞错了
    excelWriter.write(totalListList, writeSheet);
    excelWriter.finish();
}



private List<FillData> data() {
  List<FillData> list = Lists.newArrayList();
  for (int i = 0; i < 10; i++) {
    FillData fillData = new FillData();
    list.add(fillData);
    fillData.setName("张三");
    fillData.setNumber(5.2);
    fillData.setDate(new Date());
  }
  return list;
}
```

#### 2.4.2.1、源文件

![image-20211221165812005](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221165812005.png)

#### 2.4.2.2、输出结果

![image-20211221165935133](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221165935133.png)



## 2.5、横向填充

### 2.5.1、样例

#### 2.5.1.1、`FillData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class FillData {
    private String name;
    private double number;
    private Date date;
}

```

### 2.5.2、测试

```JAVA
/**
 * 5、横向填充
 */
@Override
public void horizontalFill() {
    String newFileName = FILL_HORIZONTAL_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
    ExcelWriter excelWriter = EasyExcel.write(newFileName).withTemplate(FILL_HORIZONTAL_EXCEL_FILE).build();
    WriteSheet writeSheet = EasyExcel.writerSheet().build();
    FillConfig fillConfig = FillConfig.builder().direction(WriteDirectionEnum.HORIZONTAL).build();
    excelWriter.fill(data(), fillConfig, writeSheet);
    excelWriter.fill(data(), fillConfig, writeSheet);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("date", "2019年10月9日13:28:28");
    excelWriter.fill(map, writeSheet);
    excelWriter.finish();
}

private List<FillData> data() {
  List<FillData> list = Lists.newArrayList();
  for (int i = 0; i < 10; i++) {
    FillData fillData = new FillData();
    list.add(fillData);
    fillData.setName("张三");
    fillData.setNumber(5.2);
    fillData.setDate(new Date());
  }
  return list;
}
```



#### 2.5.2.1、源文件

![image-20211221170352232](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221170352232.png)



#### 2.5.2.2、输出文件

![image-20211221170842096](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221170842096.png)

## 2.6、多列表组合填充

### 2.6.1、样例

#### 2.6.1.1、`FillData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class FillData {
  private String name;
  private double number;
  private Date date;
}

```

### 2.6.2、测试

```java
/**
 * 6、多列表组合填充
 * 说明： {} 代表普通变量 {.} 代表是list的变量 {前缀.} 前缀可以区分不同的list
 */
@Override
public void compositeFill() {
    String newFileName = FILL_COMPOSITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
    ExcelWriter excelWriter = EasyExcel.write(newFileName).withTemplate(FILL_COMPOSITE_EXCEL_FILE).build();
    WriteSheet writeSheet = EasyExcel.writerSheet().build();
    FillConfig fillConfig = FillConfig.builder().direction(WriteDirectionEnum.HORIZONTAL).build();
    excelWriter.fill(new FillWrapper("data1", data()), fillConfig, writeSheet);
    excelWriter.fill(new FillWrapper("data1", data()), fillConfig, writeSheet);
    excelWriter.fill(new FillWrapper("data2", data()), writeSheet);
    excelWriter.fill(new FillWrapper("data2", data()), writeSheet);
    excelWriter.fill(new FillWrapper("data3", data()), writeSheet);
    excelWriter.fill(new FillWrapper("data3", data()), writeSheet);

    Map<String, Object> map = new HashMap<String, Object>();
    //map.put("date", "2019年10月9日13:28:28");
    map.put("date", new Date());
    excelWriter.fill(map, writeSheet);
    // 别忘记关闭流
    excelWriter.finish();
}   

private List<FillData> data() {
  List<FillData> list = Lists.newArrayList();
  for (int i = 0; i < 10; i++) {
    FillData fillData = new FillData();
    list.add(fillData);
    fillData.setName("张三");
    fillData.setNumber(5.2);
    fillData.setDate(new Date());
  }
  return list;
}
```

#### 2.6.2.1、源文件

![image-20211221170750462](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221170750462.png)

#### 2.6.2.2、输出结果

![image-20211221170949005](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221170949005.png)



# 3、`write`

```java
private static final String PATH = "/Users/healerjean/Desktop/data/write/";
private static final String CURRENT_TIME_MILLIS_NAME = "CURRENT_TIME_MILLIS";

private static final String WRITE_SIMPLE_EXCEL_RESULT_FILE = PATH + "simpleWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
private static final String WRITE_EXCLUDEORINCLUDEWRITE_EXCEL_RESULT_FILE = PATH + "excludeOrIncludeWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
private static final String WRITE_INDEXWRITE_EXCEL_RESULT_FILE = PATH + "indexWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
private static final String WRITE_COMPLEXHEADWRITE_EXCEL_RESULT_FILE = PATH + "complexHeadWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
private static final String WRITE_REPEATEDWRITE_EXCEL_RESULT_FILE = PATH + "repeatedWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
private static final String WRITE_CONVERTERWRITE_EXCEL_RESULT_FILE = PATH + "converterWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
private static final String WRITE_IMAGEWRITE_EXCEL_RESULT_FILE = PATH + "imageWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
private static final String WRITE_IMAGEWRITE_IMAGE_FILE = "/Users/healerjean/Desktop/data/write/image/img.jpg";

private static final String WRITE_DEMO_EXCEL_RESULT_FILE = PATH + "demo.xlsx";
private static final String WRITE_TEMPLATEWRITE_EXCEL_RESULT_FILE = PATH + "templateWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
private static final String WRITE_WIDTHANDHEIGHTWRITE_EXCEL_RESULT_FILE = PATH + "widthAndHeightWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
private static final String WRITE_ANNOTATIONSTYLEWRITE_EXCEL_RESULT_FILE = PATH + "annotationStyleWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
private static final String WRITE_MERGEWRITE_EXCEL_RESULT_FILE = PATH + "mergeWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
private static final String WRITE_LONGESTMATCHCOLUMNWIDTHWRITE_EXCEL_RESULT_FILE = PATH + "longestMatchColumnWidthWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
private static final String WRITE_DYNAMICHEADWRITE_EXCEL_RESULT_FILE = PATH + "dynamicHeadWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";
private static final String WRITE_NOMODELWRITE_EXCEL_RESULT_FILE = PATH + "noModelWrite" + CURRENT_TIME_MILLIS_NAME + ".xlsx";

```



## 3.1、最简单的写

### 3.1.1、样例

#### 3.1.1.1、`DemoData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class DemoData {
    @ExcelProperty("字符串标题")
    private String string;
    @ExcelProperty("日期标题")
    private Date date;
    @ExcelProperty("数字标题")
    private Double doubleData;

    /**  忽略这个字段   */
    @ExcelIgnore
    private String ignore;
}

```

### 3.1.2、测试

```java

/**
 * 1、最简单的写
 */
@Override
public void simpleWrite() {
    // 写法1
    String newFileName = WRITE_SIMPLE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
    EasyExcel.write(newFileName, DemoData.class).sheet("模板").doWrite(() -> data());

    // 写法2
    newFileName = WRITE_SIMPLE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, 
                                                         String.valueOf(System.currentTimeMillis()));
    EasyExcel.write(newFileName, DemoData.class).sheet("模板").doWrite(data());

    // 写法3
    newFileName = WRITE_SIMPLE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, 
                                                         String.valueOf(System.currentTimeMillis()));
    // 这里 需要指定写用哪个class去写
    ExcelWriter excelWriter = null;
    try {
        excelWriter = EasyExcel.write(newFileName, DemoData.class).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
        excelWriter.write(data(), writeSheet);
    } finally {
        // 千万别忘记finish 会帮忙关闭流
        if (excelWriter != null) {
            excelWriter.finish();
        }
    }
}
```

#### 3.1.2.1、写法1结果

![image-20211221171325423](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221171325423.png)

#### 3.1.2.2、写法2结果

![image-20211221171342541](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221171342541.png)

#### 3.1.2.3、写法3结果

![image-20211221171400969](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221171400969.png)



## 3.2、根据参数只导出指定列

### 3.2.1、样例

#### 3.2.1.1、`DemoData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class DemoData {
    @ExcelProperty("字符串标题")
    private String string;
    @ExcelProperty("日期标题")
    private Date date;
    @ExcelProperty("数字标题")
    private Double doubleData;

    /**  忽略这个字段   */
    @ExcelIgnore
    private String ignore;
}

```

### 3.2.2、测试

```java
/**
 * 2、根据参数只导出指定列
 * 2.1、根据用户传入字段 假设我们要忽略 date
 * 2.2、根据用户传入字段 假设我们只要导出 date
 */
@Override
public void excludeOrIncludeWrite() {
    String newFileName = WRITE_EXCLUDEORINCLUDEWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
    // 这里需要注意 在使用ExcelProperty注解的使用，如果想不空列则需要加入order字段，而不是index,order会忽略空列，然后继续往后，而index，不会忽略空列，在第几列就是第几列。

    // 2.1、根据用户传入字段 假设我们要忽略 date
    Set<String> excludeColumnFiledNames = Sets.newHashSet("date");
    EasyExcel.write(newFileName, 
                    DemoData.class).excludeColumnFiledNames(excludeColumnFiledNames)
      .sheet("模板").doWrite(data());

    // 2.2、根据用户传入字段 假设我们只要导出 date
    newFileName = WRITE_EXCLUDEORINCLUDEWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
    Set<String> includeColumnFiledNames = Sets.newHashSet("date");
    EasyExcel.write(newFileName, DemoData.class).includeColumnFiledNames(includeColumnFiledNames).sheet("模板").doWrite(data());
}

```

#### 3.2.2.1、根据用户传入字段 假设我们要忽略 date

![image-20211221171656514](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221171656514.png)

#### 3.2.2.2、根据用户传入字段 假设我们只要导出 date

![image-20211221171746664](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221171746664.png)



## 3.3、指定写入的列

### 3.3.1、样例

#### 3.3.1.1、`IndexData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class IndexData {
    @ExcelProperty(value = "字符串标题", index = 0)
    private String string;
    @ExcelProperty(value = "日期标题", index = 1)
    private Date date;
    /**
     * 这里设置3 会导致第二列空的
     */
    @ExcelProperty(value = "数字标题", index = 3)
    private Double doubleData;
}

```

#### 3.3.1.2、`DemoData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class DemoData {
    @ExcelProperty("字符串标题")
    private String string;
    @ExcelProperty("日期标题")
    private Date date;
    @ExcelProperty("数字标题")
    private Double doubleData;

    /**  忽略这个字段   */
    @ExcelIgnore
    private String ignore;
}


```



### 3.3.2、测试

```java
/**
 * 3、指定写入的列
 */
@Override
public void indexWrite() {
    String newFileName = WRITE_INDEXWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
    EasyExcel.write(newFileName, IndexData.class).sheet("模板").doWrite(data());
}

```

#### 3.3.2.1、输出文件

![image-20211221172001560](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221172001560.png)



## 3.4、复杂头写入

### 3.4.1、样例

#### 3.4.1.1、`ComplexHeadData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class ComplexHeadData {
    @ExcelProperty({"主标题", "字符串标题"})
    private String string;
    @ExcelProperty({"主标题", "日期标题"})
    private Date date;
    @ExcelProperty({"主标题", "数字标题"})
    private Double doubleData;
}

```

#### 3.4.1.2、`DemoData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class DemoData {
    @ExcelProperty("字符串标题")
    private String string;
    @ExcelProperty("日期标题")
    private Date date;
    @ExcelProperty("数字标题")
    private Double doubleData;

    /**  忽略这个字段   */
    @ExcelIgnore
    private String ignore;
}


```



### 3.4.2、测试

```java
/**
  * 4、复杂头写入
  * 注意：本示例是合并了标题头的单元格
  */
 @Override
 public void complexHeadWrite() {
     String newFileName = WRITE_COMPLEXHEADWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
     EasyExcel.write(newFileName, ComplexHeadData.class).sheet("模板").doWrite(data());
 }
```



#### 3.4.2.1、输出文件

![image-20211221174552459](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221174552459.png)



## 3.5、重复多次写入

### 3.5.1、样例

#### 3.5.1.1、`DemoData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class DemoData {
    @ExcelProperty("字符串标题")
    private String string;
    @ExcelProperty("日期标题")
    private Date date;
    @ExcelProperty("数字标题")
    private Double doubleData;

    /**  忽略这个字段   */
    @ExcelIgnore
    private String ignore;
}

```

#### 3.5.1.2、`DemoData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class DemoData {
    @ExcelProperty("字符串标题")
    private String string;
    @ExcelProperty("日期标题")
    private Date date;
    @ExcelProperty("数字标题")
    private Double doubleData;

    /**  忽略这个字段   */
    @ExcelIgnore
    private String ignore;
}


```



### 3.5.2、测试

```java

/**
 * 5、重复多次写入
 */
@Override
public void repeatedWrite() {
    // 方法1
    String newFileName = WRITE_REPEATEDWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
    ExcelWriter excelWriter = null;
    try {
        excelWriter = EasyExcel.write(newFileName, DemoData.class).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
        // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来
        for (int i = 0; i < 5; i++) {
            List<DemoData> data = data();
            excelWriter.write(data, writeSheet);
        }
    } finally {
        // 千万别忘记finish 会帮忙关闭流
        if (excelWriter != null) {
            excelWriter.finish();
        }
    }

    // 方法2 如果写到不同的sheet 同一个对象
    try {
        newFileName = WRITE_REPEATEDWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
        // 这里 指定文件
        excelWriter = EasyExcel.write(newFileName, DemoData.class).build();
        // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来。这里最终会写到5个sheet里面
        for (int i = 0; i < 5; i++) {
            // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样
            WriteSheet writeSheet = EasyExcel.writerSheet(i, "模板" + i).build();
            // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
            List<DemoData> data = data();
            excelWriter.write(data, writeSheet);
        }
    } finally {
        if (excelWriter != null) {
            excelWriter.finish();
        }
    }

    // 方法3 如果写到不同的sheet 不同的对象
    try {
        newFileName = WRITE_REPEATEDWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
        // 这里 指定文件
        excelWriter = EasyExcel.write(newFileName).build();
        // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来。这里最终会写到5个sheet里面
        for (int i = 0; i < 5; i++) {
            // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样。这里注意DemoData.class 可以每次都变，我这里为了方便 所以用的同一个class
            // 实际上可以一直变
            WriteSheet writeSheet = EasyExcel.writerSheet(i, "模板" + i).head(DemoData.class).build();
            // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
            List<DemoData> data = data();
            excelWriter.write(data, writeSheet);
        }
    } finally {
        if (excelWriter != null) {
            excelWriter.finish();
        }
    }
}

```

#### 3.5.2.1、方法一结果

![image-20211221174820475](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221174820475.png)



## 3.6、日期、数字或者自定义格式转换

### 3.6.1、样例

#### 3.6.1.1、`ConverterData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class ConverterData {
    /**
     * 我想所有的 字符串起前面加上"自定义："三个字
     */
    @ExcelProperty(value = "字符串标题", converter = CustomStringStringConverter.class)
    private String string;
    /**
     * 我想写到excel 用年月日的格式
     */
    @DateTimeFormat("yyyy年MM月dd日HH时mm分ss秒")
    @ExcelProperty("日期标题")
    private Date date;
    /**
     * 我想写到excel 用百分比表示
     */
    @NumberFormat("#.##%")
    @ExcelProperty(value = "数字标题")
    private Double doubleData;
}

```

#### 3.6.1.2、`DemoData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class DemoData {
    @ExcelProperty("字符串标题")
    private String string;
    @ExcelProperty("日期标题")
    private Date date;
    @ExcelProperty("数字标题")
    private Double doubleData;

    /**  忽略这个字段   */
    @ExcelIgnore
    private String ignore;
}


```



### 3.6.2、测试

```java
/**
 * 6、日期、数字或者自定义格式转换
 */
@Override
public void converterWrite() {
    String newFileName = WRITE_CONVERTERWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
    EasyExcel.write(newFileName, ConverterData.class).sheet("模板").doWrite(data());
}
```

#### 3.6.2.1、输出文件

![image-20211221175052108](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221175052108.png)



## 3.7、图片导出

### 3.7.1、样例

#### 3.7.1.1、`ImageDemoData`

```java
@Getter
@Setter
@EqualsAndHashCode
@ContentRowHeight(100) //行高
@ColumnWidth(100 / 8) //列宽
public class ImageDemoData {
    private File file;

    private InputStream inputStream;
    /** 如果string类型 必须指定转换器，string默认转换成string  */
    @ExcelProperty(converter = StringImageConverter.class)
    private String string;

    private byte[] byteArray;
    /**  根据url导出  */
    private URL url;

}
```



### 3.7.2、测试

```java
/**
 * 7、图片导出
 */
@Override
public void imageWrite() throws IOException {
    String newFileName = WRITE_IMAGEWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
    String imagePath = WRITE_IMAGEWRITE_IMAGE_FILE;
    try (InputStream inputStream = FileUtils.openInputStream(new File(imagePath))) {
        List<ImageDemoData> list = ListUtils.newArrayList();
        ImageDemoData imageDemoData = new ImageDemoData();
        // 放入五种类型的图片 实际使用只要选一种即可
        imageDemoData.setByteArray(FileUtils.readFileToByteArray(new File(imagePath)));
        imageDemoData.setFile(new File(imagePath));
        imageDemoData.setString(imagePath);
        imageDemoData.setInputStream(inputStream);
        imageDemoData.setUrl(new URL( "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Farticle-fd.zol-img.com.cn%2Ft_s998x562c5%2Fg5%2FM00%2F0A%2F02%2FChMkJltpVKGIQENcAAKaC93UFtUAAqi5QPdcOwAApoj403.jpg&refer=http%3A%2F%2Farticle-fd.zol-img.com.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1642583011&t=619dc95b408c5d7bb168e66210be6519"));
        list.add(imageDemoData);
        // 写入数据
        EasyExcel.write(newFileName, ImageDemoData.class).sheet().doWrite(list);
    }
}

```

#### 3.7.2.1、输出文件

![image-20211221175238032](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221175238032.png)



## 3.8、根据模板写入

### 3.8.1、样例

#### 3.8.1.1、`DemoData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class DemoData {
  @ExcelProperty("字符串标题")
  private String string;
  @ExcelProperty("日期标题")
  private Date date;
  @ExcelProperty("数字标题")
  private Double doubleData;

  /**  忽略这个字段   */
  @ExcelIgnore
  private String ignore;
}

```

#### 3.8.1.2、`DemoData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class DemoData {
    @ExcelProperty("字符串标题")
    private String string;
    @ExcelProperty("日期标题")
    private Date date;
    @ExcelProperty("数字标题")
    private Double doubleData;

    /**  忽略这个字段   */
    @ExcelIgnore
    private String ignore;
}


```



### 3.8.2、测试

```java
 /**
   * 8、根据模板写入
   * 说明：模板个球，和Fill模板是不一样的
   */
  @Override
  public void templateWrite() {
      String newFileName = WRITE_TEMPLATEWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
      EasyExcel.write(newFileName, DemoData.class).withTemplate(WRITE_DEMO_EXCEL_RESULT_FILE).sheet().doWrite(data());
  }

```

#### 3.8.2.1、模板文件

![image-20211221175426003](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221175426003.png)

#### 3.8.2.2、输出结果

![image-20211221175451224](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221175451224.png)



## 3.9、列宽、行高

### 3.9.1、样例

#### 3.9.1.1、`WidthAndHeightData`

```java
@Getter
@Setter
@EqualsAndHashCode
@ContentRowHeight(10)
@HeadRowHeight(20)
@ColumnWidth(25)
public class WidthAndHeightData {

    @ExcelProperty("字符串标题")
    private String string;

    @ExcelProperty("日期标题")
    private Date date;

    /** 宽度为50  */
    @ColumnWidth(50)
    @ExcelProperty("数字标题")
    private Double doubleData;

}

```

#### 3.9.1.2、`DemoData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class DemoData {
    @ExcelProperty("字符串标题")
    private String string;
    @ExcelProperty("日期标题")
    private Date date;
    @ExcelProperty("数字标题")
    private Double doubleData;

    /**  忽略这个字段   */
    @ExcelIgnore
    private String ignore;
}


```



### 3.9.2、测试

```java
/**
 * 9、 列宽、行高
 */
@Override
public void widthAndHeightWrite() {
    String newFileName = WRITE_WIDTHANDHEIGHTWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
    EasyExcel.write(newFileName, WidthAndHeightData.class).sheet("模板").doWrite(data());
}
```

#### 3.9.2.1、输出文件

![image-20211221175651324](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221175651324.png)



## 3.10、注解形式自定义样式

### 3.10.1、样例

#### 3.10.1.1、`DemoStyleData`

```java
@Getter
@Setter
@EqualsAndHashCode
// 头背景设置成红色 IndexedColors.RED.getIndex()
@HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 10)
// 头字体设置成20
@HeadFontStyle(fontHeightInPoints = 20)

// 内容的背景设置成绿色 IndexedColors.GREEN.getIndex()
@ContentStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 17)
// 内容字体设置成20
@ContentFontStyle(fontHeightInPoints = 20)
public class DemoStyleData {
    // 字符串的头背景设置成粉红 IndexedColors.PINK.getIndex()
    @HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 14)
    // 字符串的头字体设置成20
    @HeadFontStyle(fontHeightInPoints = 30)
    // 字符串的内容的背景设置成天蓝 IndexedColors.SKY_BLUE.getIndex()
    @ContentStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 40)
    // 字符串的内容字体设置成20
    @ContentFontStyle(fontHeightInPoints = 30)
    @ExcelProperty("字符串标题")

    private String string;
    @ExcelProperty("日期标题")
    private Date date;
    @ExcelProperty("数字标题")
    private Double doubleData;
}
```

#### 3.10.1.2、`DemoData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class DemoData {
    @ExcelProperty("字符串标题")
    private String string;
    @ExcelProperty("日期标题")
    private Date date;
    @ExcelProperty("数字标题")
    private Double doubleData;

    /**  忽略这个字段   */
    @ExcelIgnore
    private String ignore;
}


```



### 3.10.2、测试

```java
/**
 * 10、注解形式自定义样式
 */
@Override
public void annotationStyleWrite() {
    String newFileName = WRITE_ANNOTATIONSTYLEWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
    EasyExcel.write(newFileName, DemoStyleData.class).sheet("模板").doWrite(data());
}

```

![image-20211221175838666](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221175838666.png)

## 3.11、合并单元格

### 3.11.1、样例

#### 3.11.1.1、`DemoMergeData`

```java
@Getter
@Setter
@EqualsAndHashCode
// 将第6-7行的2-3列合并成一个单元格
// @OnceAbsoluteMerge(firstRowIndex = 5, lastRowIndex = 6, firstColumnIndex = 1, lastColumnIndex = 2)
public class DemoMergeData {
    // 这一列 每隔2行 合并单元格
    @ContentLoopMerge(eachRow = 2)
    @ExcelProperty("字符串标题")
    private String string;
    @ExcelProperty("日期标题")
    private Date date;
    @ExcelProperty("数字标题")
    private Double doubleData;
}
```

#### 3.11.1.2、`DemoData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class DemoData {
    @ExcelProperty("字符串标题")
    private String string;
    @ExcelProperty("日期标题")
    private Date date;
    @ExcelProperty("数字标题")
    private Double doubleData;

    /**  忽略这个字段   */
    @ExcelIgnore
    private String ignore;
}


```



### 3.11.2、测试

```java
/**
 * 11、合并单元格
 */
@Override
public void mergeWrite() {
    // 方法1 注解
    String newFileName = WRITE_MERGEWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
    EasyExcel.write(newFileName, DemoMergeData.class).sheet("模板").doWrite(data());
}

```



#### 3.11.2.1、输出文件

![image-20211221180024174](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221180024174.png)

## 3.12、自动列宽(不太精确)

### 3.12.1、样例

#### 3.12.1.1、`LongestMatchColumnWidthData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class LongestMatchColumnWidthData {
    @ExcelProperty("字符串标题")
    private String string;
    @ExcelProperty("日期标题很长日期标题很长日期标题很长很长")
    private Date date;
    @ExcelProperty("数字")
    private Double doubleData;
}

```

#### 3.12.1.2、`DemoData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class DemoData {
    @ExcelProperty("字符串标题")
    private String string;
    @ExcelProperty("日期标题")
    private Date date;
    @ExcelProperty("数字标题")
    private Double doubleData;

    /**  忽略这个字段   */
    @ExcelIgnore
    private String ignore;
}


```



### 3.12.2、测试

```java
/**
 * 12、自动列宽(不太精确)
  */
@Override
public void longestMatchColumnWidthWrite() {
    String  newFileName= WRITE_LONGESTMATCHCOLUMNWIDTHWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
    EasyExcel.write(newFileName, LongestMatchColumnWidthData.class).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).sheet("模板").doWrite(dataLong());
}


```

#### 3.12.2.1、输出文件

![image-20211221180307300](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221180307300.png)



## 3.13、动态头，实时生成头写入

### 3.13.1、样例

#### 3.13.1.1、`DemoData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class DemoData {
    @ExcelProperty("字符串标题")
    private String string;
    @ExcelProperty("日期标题")
    private Date date;
    @ExcelProperty("数字标题")
    private Double doubleData;

    /**  忽略这个字段   */
    @ExcelIgnore
    private String ignore;
}

```

### 3.13.2、测试

```java

/**
 * 13、 动态头，实时生成头写入
 */
@Override
public void dynamicHeadWrite() {
  String newFileName = WRITE_DYNAMICHEADWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
  EasyExcel.write(newFileName) .head(head()).sheet("模板").doWrite(data());
}



private List<List<String>> head() {
  List<List<String>> list = ListUtils.newArrayList();
  List<String> head0 = ListUtils.newArrayList();
  head0.add("字符串" + System.currentTimeMillis());
  List<String> head1 = ListUtils.newArrayList();
  head1.add("日期" + System.currentTimeMillis());
  List<String> head2 = ListUtils.newArrayList();
  head2.add("数字" + System.currentTimeMillis());
  list.add(head0);
  list.add(head1);
  list.add(head2);
  return list;
}

private List<DemoData> data() {
  List<DemoData> list = Lists.newArrayList();
  for (int i = 0; i < 10; i++) {
    DemoData data = new DemoData();
    data.setString("字符串" + i);
    data.setDate(new Date());
    data.setDoubleData(0.56);
    list.add(data);
  }
  return list;
}
```

#### 3.13.2.1、输出文件

![image-20211221180548598](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221180548598.png)



## 3.14、不创建对象的写

### 3.14.1、测试

```java
/**
   * 14、不创建对象的写
   */
@Override
public void noModelWrite() {
  String newFileName = WRITE_NOMODELWRITE_EXCEL_RESULT_FILE.replace(CURRENT_TIME_MILLIS_NAME, String.valueOf(System.currentTimeMillis()));
  EasyExcel.write(newFileName).head(head()).sheet("模板").doWrite(dataList());
}



private List<List<String>> head() {
  List<List<String>> list = ListUtils.newArrayList();
  List<String> head0 = ListUtils.newArrayList();
  head0.add("字符串" + System.currentTimeMillis());
  List<String> head1 = ListUtils.newArrayList();
  head1.add("日期" + System.currentTimeMillis());
  List<String> head2 = ListUtils.newArrayList();
  head2.add("数字" + System.currentTimeMillis());
  list.add(head0);
  list.add(head1);
  list.add(head2);
  return list;
}

private List<List<Object>> dataList() {
  List<List<Object>> list = ListUtils.newArrayList();
  for (int i = 0; i < 10; i++) {
    List<Object> data = ListUtils.newArrayList();
    data.add("字符串" + i);
    data.add(new Date());
    data.add(0.56);
    list.add(data);
  }
  return list;
}

```

#### 3.14.1.1、输出文件

![image-20211221182939552](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20211221182939552.png)

# 4、`Web`

## 4.1、文件下载

```java
@ApiOperation(value = "文件下载",
              notes = "文件下载",
              consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
              produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
              response = ResponseBean.class
             )
@GetMapping(value = "download", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public void indexOrNameRead(HttpServletResponse response) throws Exception {
  response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
  response.setCharacterEncoding("utf-8");
  String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
  response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
  EasyExcel.write(response.getOutputStream()).sheet("模板").doWrite(data());
}

```



## 4.2、文件下载失败返回Json

```java
/**
 * 文件下载并且失败的时候返回json（默认失败了会返回一个有部分数据的Excel）
 */
@GetMapping(value = "downloadFailedUsingJson", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public void downloadFailedUsingJson(HttpServletResponse response) throws IOException {
    try {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        // 这里需要设置不关闭流
        EasyExcel.write(response.getOutputStream(), DownloadData.class).autoCloseStream(Boolean.FALSE).sheet("模板").doWrite(data());
    } catch (Exception e) {
        // 重置response
        response.reset();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        Map<String, String> map = MapUtils.newHashMap();
        map.put("status", "failure");
        map.put("message", "下载文件失败" + e.getMessage());
        response.getWriter().println(JsonUtils.toJsonString(map));
    }
}
```

## 4.3、上传

```java
@ApiOperation(value = "上传",
              notes = "上传",
              consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
              produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
              response = ResponseBean.class
             )
@PostMapping(value = "upload", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public ResponseBean upload(MultipartFile file) throws Exception {
  EasyExcel.read(file.getInputStream(), UploadData.class, 
                 new UploadDataListener()).sheet().doRead();
  return ResponseBean.buildSuccess();
}
```

### 4.3.1、`UploadDataListener`

```java
@Slf4j
public class UploadDataListener implements ReadListener<UploadData> {
    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;
    private List<UploadData> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    public UploadDataListener() {
    }

    /**
     * 这个每一条数据解析都会来调用
     */
    @Override
    public void invoke(UploadData data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JsonUtils.toJsonString(data));
        cachedDataList.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        log.info("存储数据库成功！");
    }
}

```



### 4.3.2、`UploadData`

```java
@Getter
@Setter
@EqualsAndHashCode
public class UploadData {
    private String string;
    private Date date;
    private Double doubleData;
}

```







![ContactAuthor](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/artical_bottom.jpg)



<!-- Gitalk 评论 start  -->

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
		id: 'oly9fB72cQ3beTIJ',
    });
    gitalk.render('gitalk-container');
</script> 




<!-- Gitalk end -->



