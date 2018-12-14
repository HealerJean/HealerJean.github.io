package com.hlj.data.res.test;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Desc: json测试实体
 * @Author HealerJean
 * @Date 2018/9/25  上午11:10.
 *
 */
@Data
@Accessors(chain = true)
@ApiModel (description = "json测试实体")
public class TsJsonData {


    /**
     {"error":"0","msg":"操作成功","data":[{"n_long":"3923600074","n_string":"小当","n_bigDecimal":"5.9000","n_date":"2018-12-13 20:31:04","n_integer":1},{"n_long":"3923600074","n_string":"小当","n_bigDecimal":"5.9000","n_date":"2018-12-13 20:31:04","n_integer":1}]}
     * error : 0
     * msg : 操作成功
     * data : [{"n_long":"3923600074","n_string":"小当","n_bigDecimal":"5.9000","n_date":"2018-12-13 20:31:04","n_integer":1},{"n_long":"3923600074","n_string":"小当","n_bigDecimal":"5.9000","n_date":"2018-12-13 20:31:04","n_integer":1}]
     */

    private String error;
    private String msg;
    private List<DataBean> data;

    @Data
    @Accessors(chain = true)
    public static class DataBean {
        /**
         * n_long : 3923600074
         * n_string : name
         * n_bigDecimal : 5.9000
         * n_date : 2018-12-13 20:31:04
         * income_rate : 0.9000
         * n_integer : 1
         */

        private Long n_long;
        private String n_string;
        private BigDecimal n_bigDecimal;
        private Date n_date;
        private Integer n_integer;


    }
}
