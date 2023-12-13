package com.healerjean.proj.template.service.impl;

import com.healerjean.proj.template.po.BPrizeExchangeDetail;
import com.healerjean.proj.template.bo.BPrizeExchangeDetailQueryBO;
import com.healerjean.proj.template.bo.BPrizeExchangeDetailBO;
import com.healerjean.proj.template.manager.BPrizeExchangeDetailManager;
import com.healerjean.proj.template.service.BPrizeExchangeDetailService;
import com.healerjean.proj.template.converter.BPrizeExchangeDetailConverter;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * 权益奖品兑换明细(BPrizeExchangeDetail)Service
 *
 * @author zhangyujin
 * @date 2023-12-12 11:07:29
 */
@Service
public class BPrizeExchangeDetailServiceImpl implements BPrizeExchangeDetailService {

    /**
     * bPrizeExchangeDetailManager
     */
    @Resource
    private BPrizeExchangeDetailManager bPrizeExchangeDetailManager;

    /**
     * 保存-BPrizeExchangeDetail
     *
     * @param bo bo
     * @return boolean
     */
    @Override
    public boolean saveBPrizeExchangeDetail(BPrizeExchangeDetailBO bo) {
        BPrizeExchangeDetail po = BPrizeExchangeDetailConverter.INSTANCE.convertBPrizeExchangeDetailBoToPo(bo);
        return bPrizeExchangeDetailManager.saveBPrizeExchangeDetail(po);
    }

    /**
     * 删除-BPrizeExchangeDetail
     *
     * @param bo bo
     * @return boolean
     */
    @Override
    public boolean deleteBPrizeExchangeDetail(BPrizeExchangeDetailBO bo) {
        return bPrizeExchangeDetailManager.deleteBPrizeExchangeDetailById(bo.getId());
    }

    /**
     * 更新-BPrizeExchangeDetail
     *
     * @param bo bo
     * @return boolean
     */
    @Override
    public boolean updateBPrizeExchangeDetail(BPrizeExchangeDetailBO bo) {
        BPrizeExchangeDetail po = BPrizeExchangeDetailConverter.INSTANCE.convertBPrizeExchangeDetailBoToPo(bo);
        return bPrizeExchangeDetailManager.updateBPrizeExchangeDetail(po);
    }

    /**
     * 单条查询-BPrizeExchangeDetail
     *
     * @param queryBo queryBo
     * @return BPrizeExchangeDetailBO
     */
    @Override
    public BPrizeExchangeDetailBO queryBPrizeExchangeDetailSingle(BPrizeExchangeDetailQueryBO queryBo) {
        return bPrizeExchangeDetailManager.queryBPrizeExchangeDetailSingle(queryBo);
    }

    /**
     * 列表查询-BPrizeExchangeDetail
     *
     * @param queryBo queryBo
     * @return List<BPrizeExchangeDetailBO>
     */
    @Override
    public List<BPrizeExchangeDetailBO> queryBPrizeExchangeDetailList(BPrizeExchangeDetailQueryBO queryBo) {
        return bPrizeExchangeDetailManager.queryBPrizeExchangeDetailList(queryBo);
    }

}

