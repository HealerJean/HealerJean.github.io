package com.healerjean.proj.template.service;

import com.healerjean.proj.template.bo.BPrizeExchangeDetailQueryBO;
import com.healerjean.proj.template.bo.BPrizeExchangeDetailBO;

import java.util.List;

/**
 * 权益奖品兑换明细(BPrizeExchangeDetail)Service接口
 *
 * @author zhangyujin
 * @date 2023-12-12 11:07:29
 */
public interface BPrizeExchangeDetailService {

    /**
     * 保存-BPrizeExchangeDetail
     *
     * @param bo bo
     * @return boolean
     */
    boolean saveBPrizeExchangeDetail(BPrizeExchangeDetailBO bo);

    /**
     * 删除-BPrizeExchangeDetail
     *
     * @param bo bo
     * @return boolean
     */
    boolean deleteBPrizeExchangeDetail(BPrizeExchangeDetailBO bo);

    /**
     * 更新-BPrizeExchangeDetail
     *
     * @param bo bo
     * @return boolean
     */
    boolean updateBPrizeExchangeDetail(BPrizeExchangeDetailBO bo);

    /**
     * 单条查询-BPrizeExchangeDetail
     *
     * @param queryBo queryBo
     * @return BPrizeExchangeDetailBO
     */
    BPrizeExchangeDetailBO queryBPrizeExchangeDetailSingle(BPrizeExchangeDetailQueryBO queryBo);

    /**
     * 列表查询-BPrizeExchangeDetail
     *
     * @param queryBo queryBo
     * @return List<BPrizeExchangeDetailBO>
     */
    List<BPrizeExchangeDetailBO> queryBPrizeExchangeDetailList(BPrizeExchangeDetailQueryBO queryBo);

}

