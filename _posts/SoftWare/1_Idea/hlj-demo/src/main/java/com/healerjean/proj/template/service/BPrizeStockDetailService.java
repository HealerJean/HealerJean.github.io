package com.healerjean.proj.template.service;

import com.healerjean.proj.template.bo.BPrizeStockDetailQueryBO;
import com.healerjean.proj.template.bo.BPrizeStockDetailBO;

import java.util.List;

/**
 * 权益奖品增减库存明细表(BPrizeStockDetail)Service接口
 *
 * @author zhangyujin
 * @date 2023-12-12 11:07:29
 */
public interface BPrizeStockDetailService {

    /**
     * 保存-BPrizeStockDetail
     *
     * @param bo bo
     * @return boolean
     */
    boolean saveBPrizeStockDetail(BPrizeStockDetailBO bo);

    /**
     * 删除-BPrizeStockDetail
     *
     * @param bo bo
     * @return boolean
     */
    boolean deleteBPrizeStockDetail(BPrizeStockDetailBO bo);

    /**
     * 更新-BPrizeStockDetail
     *
     * @param bo bo
     * @return boolean
     */
    boolean updateBPrizeStockDetail(BPrizeStockDetailBO bo);

    /**
     * 单条查询-BPrizeStockDetail
     *
     * @param queryBo queryBo
     * @return BPrizeStockDetailBO
     */
    BPrizeStockDetailBO queryBPrizeStockDetailSingle(BPrizeStockDetailQueryBO queryBo);

    /**
     * 列表查询-BPrizeStockDetail
     *
     * @param queryBo queryBo
     * @return List<BPrizeStockDetailBO>
     */
    List<BPrizeStockDetailBO> queryBPrizeStockDetailList(BPrizeStockDetailQueryBO queryBo);

}

