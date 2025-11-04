package com.healerjean.proj.data.req;

import com.healerjean.proj.data.dto.EsTradeLogDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * TradeLogReq
 *
 * @author zhangyujin
 * @date 2024/4/26
 */
@Data
public class EsTradeLogReq implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1726510226658874203L;

    /**
     * tradeLog
     */
    private EsTradeLogDTO tradeLogs;

}
