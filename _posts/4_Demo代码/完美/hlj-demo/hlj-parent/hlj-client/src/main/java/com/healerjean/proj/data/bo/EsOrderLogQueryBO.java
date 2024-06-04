package com.healerjean.proj.data.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 订单险种投保日志(CkOrderLog)QueryBO对象
 *
 * @author zhangyujin
 * @date 2024-03-20 15:56:50
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public class EsOrderLogQueryBO extends EsBaseQueryBO<EsTradeLogBO> implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;


    /**
     * 失效时间 >= inValidateTimeStart
     */
    private String inValidateTimeStart;


    /**
     * 失效时间 <= inValidateTimeEnd
     */
    private String inValidateTimeEnd;



    /**
     * 失效时间 >= createTimeStart
     */
    private String createTimeStart;


    /**
     * 失效时间 >= createTimeEnd
     */
    private String createTimeEnd;







}

