package com.healerjean.proj.data.req;

import com.healerjean.proj.data.dto.EsTradeLogDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * TradeLogReq
 *
 * @author zhangyujin
 * @date 2024/4/26
 */

@Data
public class EsTradeLogBatchReq implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -35966549668100639L;

    /**
     * tradeLog
     */
    private List<EsTradeLogDTO> tradeLogs;

}
