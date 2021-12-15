package com.healerjean.proj.util.mail.dto;

import lombok.Data;

/**
 * @author zhangyujin
 * @date 2021/12/15  6:16 下午.
 * @description
 */
@Data
public class ListHtmlTableDTO {

    @ExcelAttribute(name = "系统")
    private String systemName;

    @ExcelAttribute(name = "名称")
    private String name;
}
