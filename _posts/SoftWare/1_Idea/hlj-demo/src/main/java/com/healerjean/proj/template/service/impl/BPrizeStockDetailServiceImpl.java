package com.healerjean.proj.template.service.impl;

import com.healerjean.proj.template.po.BPrizeStockDetail;
import com.healerjean.proj.template.bo.BPrizeStockDetailQueryBO;
import com.healerjean.proj.template.bo.BPrizeStockDetailBO;
import com.healerjean.proj.template.manager.BPrizeStockDetailManager;
import com.healerjean.proj.template.service.BPrizeStockDetailService;
import com.healerjean.proj.template.converter.BPrizeStockDetailConverter;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * 权益奖品增减库存明细表(BPrizeStockDetail)Service
 *
 * @author zhangyujin
 * @date 2023-12-12 11:07:29
 */
@Service
public class BPrizeStockDetailServiceImpl implements BPrizeStockDetailService {

    /**
     * bPrizeStockDetailManager
     */
    @Resource
    private BPrizeStockDetailManager bPrizeStockDetailManager;

    /**
     * 保存-BPrizeStockDetail
     *
     * @param bo bo
     * @return boolean
     */
    @Override
    public boolean saveBPrizeStockDetail(BPrizeStockDetailBO bo) {
        BPrizeStockDetail po = BPrizeStockDetailConverter.INSTANCE.convertBPrizeStockDetailBoToPo(bo);
        return bPrizeStockDetailManager.saveBPrizeStockDetail(po);
    }

    /**
     * 删除-BPrizeStockDetail
     *
     * @param bo bo
     * @return boolean
     */
    @Override
    public boolean deleteBPrizeStockDetail(BPrizeStockDetailBO bo) {
        return bPrizeStockDetailManager.deleteBPrizeStockDetailById(bo.getId());
    }

    /**
     * 更新-BPrizeStockDetail
     *
     * @param bo bo
     * @return boolean
     */
    @Override
    public boolean updateBPrizeStockDetail(BPrizeStockDetailBO bo) {
        BPrizeStockDetail po = BPrizeStockDetailConverter.INSTANCE.convertBPrizeStockDetailBoToPo(bo);
        return bPrizeStockDetailManager.updateBPrizeStockDetail(po);
    }

    /**
     * 单条查询-BPrizeStockDetail
     *
     * @param queryBo queryBo
     * @return BPrizeStockDetailBO
     */
    @Override
    public BPrizeStockDetailBO queryBPrizeStockDetailSingle(BPrizeStockDetailQueryBO queryBo) {
        return bPrizeStockDetailManager.queryBPrizeStockDetailSingle(queryBo);
    }

    /**
     * 列表查询-BPrizeStockDetail
     *
     * @param queryBo queryBo
     * @return List<BPrizeStockDetailBO>
     */
    @Override
    public List<BPrizeStockDetailBO> queryBPrizeStockDetailList(BPrizeStockDetailQueryBO queryBo) {
        return bPrizeStockDetailManager.queryBPrizeStockDetailList(queryBo);
    }

}

