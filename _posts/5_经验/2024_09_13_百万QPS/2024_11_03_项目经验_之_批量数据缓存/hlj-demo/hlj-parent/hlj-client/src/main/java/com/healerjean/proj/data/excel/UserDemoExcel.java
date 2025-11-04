package com.healerjean.proj.data.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * UserDemoBO
 *
 * @author zhangyujin
 * @date 2023/6/14  10:57.
 */
@Accessors(chain = true)
@Data
public class UserDemoExcel implements Serializable {


    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -4105795350093398821L;


    /**
     * Id
     */
    @ExcelProperty("id")
    private Long id;
    /**
     * 名字
     */
    @ExcelProperty("name")
    private String name;
    /**
     * 年龄
     */
    @ExcelProperty("age")
    private Integer age;
    /**
     * 电话
     */
    @ExcelProperty("phone")
    private String phone;

}

