package com.healerjean.proj.data.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * UserDemoBO
 *
 * @author zhangyujin
 * @date 2023/6/14  10:57.
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public class UserDemoImportExcel extends Excel implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -4105795350093398821L;
    /**
     * Id
     */
    @ExcelProperty(value = "主键", index = 0)
    private Long id;
    /**
     * 名字
     */
    @ExcelProperty(value = "名字", index = 1)
    private String name;
    /**
     * 年龄
     */
    @ExcelProperty(value = "年龄", index = 2)
    private Integer age;
    /**
     * 电话
     */
    @ExcelProperty(value = "电话", index = 3)
    private String phone;

}

