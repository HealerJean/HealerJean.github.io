package com.healerjean.proj.dto.excel.read;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author zhangyujin
 * @date 2021/12/17  5:37 下午.
 * @description
 */
@Getter
@Setter
@EqualsAndHashCode
public class DemoData {
    private String string;
    private Date date;
    private Double doubleData;
}
