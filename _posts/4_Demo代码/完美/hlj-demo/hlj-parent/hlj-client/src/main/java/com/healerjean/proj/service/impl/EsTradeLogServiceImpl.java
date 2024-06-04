package com.healerjean.proj.service.impl;

import com.healerjean.proj.data.bo.EsOrderLogQueryBO;
import com.healerjean.proj.data.bo.EsTradeLogBO;
import com.healerjean.proj.data.converter.TradeLogConverter;
import com.healerjean.proj.data.manager.es.EsTradeLogManager;
import com.healerjean.proj.data.po.EsTradeLog;
import com.healerjean.proj.service.EsTradeLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * EsTradeLogServiceImpl
 *
 * @author zhangyujin
 * @date 2024/4/26
 */

@Slf4j
@Service
public class EsTradeLogServiceImpl implements EsTradeLogService {


    /**
     * esTradeLogManager
     */
    @Resource
    private EsTradeLogManager esTradeLogManager;

    /**
     * ckOrderLogQuery
     *
     * @param ckOrderLogQuery ckOrderLogQuery
     * @return CkTradeLogBO
     */
    @Override
    public List<EsTradeLogBO> listTradeLog(EsOrderLogQueryBO ckOrderLogQuery) {
        return esTradeLogManager.queryOrderLogList(ckOrderLogQuery);
    }

    /**
     * deleteTradeLog
     *
     * @param ckOrderLogQuery ckOrderLogQuery
     */
    @Override
    public void batchDeleteTradeLog(EsOrderLogQueryBO ckOrderLogQuery) {
        esTradeLogManager.batchDeleteTradeLog(ckOrderLogQuery);
    }


    @Override
    public void batchSavenTradeLog(List<EsTradeLogBO> tradeLogs) {
        List<EsTradeLog> esTradeLogs = TradeLogConverter.INSTANCE.convertTradeLogBoToPoList(tradeLogs);
        esTradeLogManager.batchInsertDocument(esTradeLogs);
    }
}
