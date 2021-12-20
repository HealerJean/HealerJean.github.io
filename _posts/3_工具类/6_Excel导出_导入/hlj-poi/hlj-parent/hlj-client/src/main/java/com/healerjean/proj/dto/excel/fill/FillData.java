package com.healerjean.proj.dto.excel.fill;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author zhangyujin
 * @date 2021/12/20  11:36 上午.
 * @description
 */
@Getter
@Setter
@EqualsAndHashCode
public class FillData {
    private String name;
    private double number;
    private Date date;

}
