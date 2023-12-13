package com.healerjean.proj.template.manager.impl;

import com.healerjean.proj.template.converter.BPrizeStockDetailConverter;
import com.healerjean.proj.template.po.BPrizeStockDetail;
import com.healerjean.proj.template.bo.BPrizeStockDetailBO;
import com.healerjean.proj.template.bo.PageBO;
import com.healerjean.proj.template.bo.BPrizeStockDetailQueryBO;
import com.healerjean.proj.template.dao.BPrizeStockDetailDao;
import com.healerjean.proj.template.manager.BPrizeStockDetailManager;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import org.springframework.util.CollectionUtils;
import org.springframework.stereotype.Service;


/**
 * 权益奖品增减库存明细表(BPrizeStockDetail)Manager接口
 *
 * @author zhangyujin
 * @date 2023-12-12 11:07:29
 */
@Service
public class BPrizeStockDetailManagerImpl implements BPrizeStockDetailManager {

    /**
     * bPrizeStockDetailDao
     */
    @Resource
    private BPrizeStockDetailDao bPrizeStockDetailDao;

    /**
     * 保存-BPrizeStockDetail
     *
     * @param po po
     * @return boolean
     */
    @Override
    public boolean saveBPrizeStockDetail(BPrizeStockDetail po) {
        return bPrizeStockDetailDao.save(po);
    }

    /**
     * 删除-BPrizeStockDetail
     *
     * @param id id
     * @return boolean
     */
    @Override
    public boolean deleteBPrizeStockDetailById(Long id) {
        //todo
        return false;
    }

    /**
     * 更新-BPrizeStockDetail
     *
     * @param po po
     * @return boolean
     */
    @Override
    public boolean updateBPrizeStockDetail(BPrizeStockDetail po) {
        LambdaUpdateWrapper<BPrizeStockDetail> updateWrapper = Wrappers.lambdaUpdate(BPrizeStockDetail.class)
                .set(BPrizeStockDetail::getId, po.getId())
                .set(BPrizeStockDetail::getCreatedDate, po.getCreatedDate())
                .set(BPrizeStockDetail::getModifiedDate, po.getModifiedDate())
                .set(BPrizeStockDetail::getBenefitsId, po.getBenefitsId())
                .set(BPrizeStockDetail::getChangeType, po.getChangeType())
                .set(BPrizeStockDetail::getChangeNum, po.getChangeNum())
                .set(BPrizeStockDetail::getChangeAfterNum, po.getChangeAfterNum())
                .set(BPrizeStockDetail::getOperator, po.getOperator())

                .eq(BPrizeStockDetail::getId, po.getId())
                .eq(BPrizeStockDetail::getCreatedDate, po.getCreatedDate())
                .eq(BPrizeStockDetail::getModifiedDate, po.getModifiedDate())
                .eq(BPrizeStockDetail::getBenefitsId, po.getBenefitsId())
                .eq(BPrizeStockDetail::getChangeType, po.getChangeType())
                .eq(BPrizeStockDetail::getChangeNum, po.getChangeNum())
                .eq(BPrizeStockDetail::getChangeAfterNum, po.getChangeAfterNum())
                .eq(BPrizeStockDetail::getOperator, po.getOperator());
        //todo 多余删除，不足补齐
        return bPrizeStockDetailDao.update(updateWrapper);
    }

    /**
     * 单条主键查询-BPrizeStockDetail
     *
     * @param id id
     * @return BPrizeStockDetail
     */
    @Override
    public BPrizeStockDetailBO queryBPrizeStockDetailById(Long id) {
        BPrizeStockDetail po = bPrizeStockDetailDao.getById(id);
        return BPrizeStockDetailConverter.INSTANCE.convertBPrizeStockDetailPoToBo(po);
    }


    /**
     * 单条查询-BPrizeStockDetail
     *
     * @param queryBo queryBo
     * @return BPrizeStockDetail
     */
    @Override
    public BPrizeStockDetailBO queryBPrizeStockDetailSingle(BPrizeStockDetailQueryBO queryBo) {
        LambdaQueryWrapper<BPrizeStockDetail> queryWrapper = Wrappers.lambdaQuery(BPrizeStockDetail.class)
                .eq(BPrizeStockDetail::getId, queryBo.getId())
                .eq(BPrizeStockDetail::getCreatedDate, queryBo.getCreatedDate())
                .eq(BPrizeStockDetail::getModifiedDate, queryBo.getModifiedDate())
                .eq(BPrizeStockDetail::getBenefitsId, queryBo.getBenefitsId())
                .eq(BPrizeStockDetail::getChangeType, queryBo.getChangeType())
                .eq(BPrizeStockDetail::getChangeNum, queryBo.getChangeNum())
                .eq(BPrizeStockDetail::getChangeAfterNum, queryBo.getChangeAfterNum())
                .eq(BPrizeStockDetail::getOperator, queryBo.getOperator());
        //todo 多余删除，不足补齐
        List<BPrizeStockDetail> list = bPrizeStockDetailDao.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return BPrizeStockDetailConverter.INSTANCE.convertBPrizeStockDetailPoToBo(list.get(0));
    }

    /**
     * 列表查询-BPrizeStockDetail
     *
     * @param queryBo queryBo
     * @return List<BPrizeStockDetail>
     */
    @Override
    public List<BPrizeStockDetailBO> queryBPrizeStockDetailList(BPrizeStockDetailQueryBO queryBo) {
        LambdaQueryWrapper<BPrizeStockDetail> queryWrapper = Wrappers.lambdaQuery(BPrizeStockDetail.class)
                .eq(BPrizeStockDetail::getId, queryBo.getId())
                .eq(BPrizeStockDetail::getCreatedDate, queryBo.getCreatedDate())
                .eq(BPrizeStockDetail::getModifiedDate, queryBo.getModifiedDate())
                .eq(BPrizeStockDetail::getBenefitsId, queryBo.getBenefitsId())
                .eq(BPrizeStockDetail::getChangeType, queryBo.getChangeType())
                .eq(BPrizeStockDetail::getChangeNum, queryBo.getChangeNum())
                .eq(BPrizeStockDetail::getChangeAfterNum, queryBo.getChangeAfterNum())
                .eq(BPrizeStockDetail::getOperator, queryBo.getOperator());

        List<BPrizeStockDetail> pos = bPrizeStockDetailDao.list(queryWrapper);
        return BPrizeStockDetailConverter.INSTANCE.convertBPrizeStockDetailPoToBoList(pos);
    }

    /**
     * 分页查询-BPrizeStockDetail
     *
     * @param queryBo queryBo
     * @return Page<BPrizeStockDetail>
     */
    @Override
    public PageBO<BPrizeStockDetailBO> queryBPrizeStockDetailPage(BPrizeStockDetailQueryBO queryBo) {
        LambdaQueryWrapper<BPrizeStockDetail> queryWrapper = Wrappers.lambdaQuery(BPrizeStockDetail.class)
                .eq(BPrizeStockDetail::getId, queryBo.getId())
                .eq(BPrizeStockDetail::getCreatedDate, queryBo.getCreatedDate())
                .eq(BPrizeStockDetail::getModifiedDate, queryBo.getModifiedDate())
                .eq(BPrizeStockDetail::getBenefitsId, queryBo.getBenefitsId())
                .eq(BPrizeStockDetail::getChangeType, queryBo.getChangeType())
                .eq(BPrizeStockDetail::getChangeNum, queryBo.getChangeNum())
                .eq(BPrizeStockDetail::getChangeAfterNum, queryBo.getChangeAfterNum())
                .eq(BPrizeStockDetail::getOperator, queryBo.getOperator());
        //todo 多余删除，不足补齐
        Page<BPrizeStockDetail> pageReq = new Page<>(0, 0, 0);
        Page<BPrizeStockDetail> page = bPrizeStockDetailDao.page(pageReq, queryWrapper);
        PageBO<BPrizeStockDetailBO> result = new PageBO<>((int) page.getTotal(), (int) page.getSize(), (int) page.getPages(), (int) page.getCurrent());
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            result.setList(page.getRecords().stream().map(BPrizeStockDetailConverter.INSTANCE::convertBPrizeStockDetailPoToBo).collect(Collectors.toList()));
        }

        return result;
    }

}

