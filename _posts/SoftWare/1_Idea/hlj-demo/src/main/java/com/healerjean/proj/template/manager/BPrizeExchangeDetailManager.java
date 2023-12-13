package com.healerjean.proj.template.manager;

import com.healerjean.proj.template.po.BPrizeExchangeDetail;
import com.healerjean.proj.template.bo.BPrizeExchangeDetailBO;
import com.healerjean.proj.template.bo.PageBO;
import com.healerjean.proj.template.bo.BPrizeExchangeDetailQueryBO;

import java.util.List;

/**
 * 权益奖品兑换明细(BPrizeExchangeDetail)Manager接口
 *
 * @author zhangyujin
 * @date 2023-12-12 11:07:29
 */
public interface BPrizeExchangeDetailManager {

    /**
     * 保存-BPrizeExchangeDetail
     *
     * @param po po
     * @return boolean
     */
    boolean saveBPrizeExchangeDetail(BPrizeExchangeDetail po);

    /**
     * 删除-BPrizeExchangeDetail
     *
     * @param id id
     * @return boolean
     */
    boolean deleteBPrizeExchangeDetailById(Long id);

    /**
     * 更新-BPrizeExchangeDetail
     *
     * @param po po
     * @return boolean
     */
    boolean updateBPrizeExchangeDetail(BPrizeExchangeDetail po);

    /**
     * 单条主键查询-BPrizeExchangeDetail
     *
     * @param id id
     * @return BPrizeExchangeDetail
     */
    BPrizeExchangeDetailBO queryBPrizeExchangeDetailById(Long id);


    /**
     * 单条查询-BPrizeExchangeDetail
     *
     * @param queryBo queryBo
     * @return BPrizeExchangeDetail
     */
    BPrizeExchangeDetailBO queryBPrizeExchangeDetailSingle(BPrizeExchangeDetailQueryBO queryBo);

    /**
     * 列表查询-BPrizeExchangeDetail
     *
     * @param queryBo queryBo
     * @return List<BPrizeExchangeDetail>
     */
    List<BPrizeExchangeDetailBO> queryBPrizeExchangeDetailList(BPrizeExchangeDetailQueryBO queryBo);

    /**
     * 分页查询-BPrizeExchangeDetail
     *
     * @param queryBo queryBo
     * @return Page<BPrizeExchangeDetail>
     */
    PageBO<BPrizeExchangeDetailBO> queryBPrizeExchangeDetailPage(BPrizeExchangeDetailQueryBO queryBo);

}

