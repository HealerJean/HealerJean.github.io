package com.healerjean.proj.service;

import com.healerjean.proj.data.bo.EsOrderLogQueryBO;
import com.healerjean.proj.data.bo.EsTradeLogBO;

import java.util.List;

/**
 * EsTradeLogService
 *
 * @author zhangyujin
 * @date 2024/4/26
 */
public interface EsTradeLogService {


    /**
     * ckOrderLogQuery
     *
     * @param ckOrderLogQuery ckOrderLogQuery
     * @return CkTradeLogBO
     */
    List<EsTradeLogBO> listTradeLog(EsOrderLogQueryBO ckOrderLogQuery);


    /**
     * deleteTradeLog
     *
     * @param ckOrderLogQuery ckOrderLogQuery
     */
    void batchDeleteTradeLog(EsOrderLogQueryBO ckOrderLogQuery);

    /**
     * batchSavenTradeLog
     *
     * @param tradeLogs tradeLogs
     */
    void batchSavenTradeLog(List<EsTradeLogBO> tradeLogs);
}
