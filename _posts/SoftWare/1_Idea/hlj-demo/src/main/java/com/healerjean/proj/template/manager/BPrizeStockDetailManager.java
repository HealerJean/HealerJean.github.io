package com.healerjean.proj.template.manager;

import com.healerjean.proj.template.po.BPrizeStockDetail;
import com.healerjean.proj.template.bo.BPrizeStockDetailBO;
import com.healerjean.proj.template.bo.PageBO;
import com.healerjean.proj.template.bo.BPrizeStockDetailQueryBO;

import java.util.List;

/**
 * 权益奖品增减库存明细表(BPrizeStockDetail)Manager接口
 *
 * @author zhangyujin
 * @date 2023-12-12 11:07:29
 */
public interface BPrizeStockDetailManager {

    /**
     * 保存-BPrizeStockDetail
     *
     * @param po po
     * @return boolean
     */
    boolean saveBPrizeStockDetail(BPrizeStockDetail po);

    /**
     * 删除-BPrizeStockDetail
     *
     * @param id id
     * @return boolean
     */
    boolean deleteBPrizeStockDetailById(Long id);

    /**
     * 更新-BPrizeStockDetail
     *
     * @param po po
     * @return boolean
     */
    boolean updateBPrizeStockDetail(BPrizeStockDetail po);

    /**
     * 单条主键查询-BPrizeStockDetail
     *
     * @param id id
     * @return BPrizeStockDetail
     */
    BPrizeStockDetailBO queryBPrizeStockDetailById(Long id);


    /**
     * 单条查询-BPrizeStockDetail
     *
     * @param queryBo queryBo
     * @return BPrizeStockDetail
     */
    BPrizeStockDetailBO queryBPrizeStockDetailSingle(BPrizeStockDetailQueryBO queryBo);

    /**
     * 列表查询-BPrizeStockDetail
     *
     * @param queryBo queryBo
     * @return List<BPrizeStockDetail>
     */
    List<BPrizeStockDetailBO> queryBPrizeStockDetailList(BPrizeStockDetailQueryBO queryBo);

    /**
     * 分页查询-BPrizeStockDetail
     *
     * @param queryBo queryBo
     * @return Page<BPrizeStockDetail>
     */
    PageBO<BPrizeStockDetailBO> queryBPrizeStockDetailPage(BPrizeStockDetailQueryBO queryBo);

}

