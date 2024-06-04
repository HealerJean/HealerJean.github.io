package com.healerjean.proj.data.manager.es;


import com.healerjean.proj.data.bo.EsOrderLogQueryBO;
import com.healerjean.proj.data.bo.EsTradeLogBO;
import com.healerjean.proj.data.po.EsTradeLog;

import java.util.List;

/**
 * EsTradeLogManager
 *
 * @author zhangyujin
 * @date 2024/3/22
 */
public interface EsTradeLogManager {



    /**
     * 列表查询-EsTradeLog
     *
     * @param queryBo queryBo
     * @return List<CkOrderLog>
     */
    List<EsTradeLogBO> queryOrderLogList(EsOrderLogQueryBO queryBo);


    /**
     * 批量插入-EsTradeLog
     * @param baseEntities baseEntities
     */
    void batchInsertDocument(List<EsTradeLog> baseEntities);

    /**
     * 删除-EsTradeLog
     *
     * @param ckOrderLogQuery ckOrderLogQuery
     * @return boolean
     */
    void batchDeleteTradeLog(EsOrderLogQueryBO ckOrderLogQuery);

}
