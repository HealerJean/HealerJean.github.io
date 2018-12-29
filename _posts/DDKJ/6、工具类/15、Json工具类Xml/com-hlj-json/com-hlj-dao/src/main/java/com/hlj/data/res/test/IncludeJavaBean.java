package com.hlj.data.res.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 作者 ：HealerJean
 * 日期 ：2018/12/28  下午7:36.
 * 类描述：
 */

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IncludeJavaBean {

        private Long n_long;
        private String n_string;
        private BigDecimal n_bigDecimal;
        private Date n_date;
        private Integer n_integer;


}
