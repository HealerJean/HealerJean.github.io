package com.healerjean.proj.template.manager.impl;

import com.healerjean.proj.template.converter.BPrizeExchangeDetailConverter;
import com.healerjean.proj.template.po.BPrizeExchangeDetail;
import com.healerjean.proj.template.bo.BPrizeExchangeDetailBO;
import com.healerjean.proj.template.bo.PageBO;
import com.healerjean.proj.template.bo.BPrizeExchangeDetailQueryBO;
import com.healerjean.proj.template.dao.BPrizeExchangeDetailDao;
import com.healerjean.proj.template.manager.BPrizeExchangeDetailManager;
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
 * 权益奖品兑换明细(BPrizeExchangeDetail)Manager接口
 *
 * @author zhangyujin
 * @date 2023-12-12 11:07:29
 */
@Service
public class BPrizeExchangeDetailManagerImpl implements BPrizeExchangeDetailManager {

    /**
     * bPrizeExchangeDetailDao
     */
    @Resource
    private BPrizeExchangeDetailDao bPrizeExchangeDetailDao;

    /**
     * 保存-BPrizeExchangeDetail
     *
     * @param po po
     * @return boolean
     */
    @Override
    public boolean saveBPrizeExchangeDetail(BPrizeExchangeDetail po) {
        return bPrizeExchangeDetailDao.save(po);
    }

    /**
     * 删除-BPrizeExchangeDetail
     *
     * @param id id
     * @return boolean
     */
    @Override
    public boolean deleteBPrizeExchangeDetailById(Long id) {
        //todo
        return false;
    }

    /**
     * 更新-BPrizeExchangeDetail
     *
     * @param po po
     * @return boolean
     */
    @Override
    public boolean updateBPrizeExchangeDetail(BPrizeExchangeDetail po) {
        LambdaUpdateWrapper<BPrizeExchangeDetail> updateWrapper = Wrappers.lambdaUpdate(BPrizeExchangeDetail.class)
                .set(BPrizeExchangeDetail::getId, po.getId())
                .set(BPrizeExchangeDetail::getCreatedDate, po.getCreatedDate())
                .set(BPrizeExchangeDetail::getModifiedDate, po.getModifiedDate())
                .set(BPrizeExchangeDetail::getBenefitsId, po.getBenefitsId())
                .set(BPrizeExchangeDetail::getVenderId, po.getVenderId())
                .set(BPrizeExchangeDetail::getExchangeTime, po.getExchangeTime())
                .set(BPrizeExchangeDetail::getBenefitsName, po.getBenefitsName())
                .set(BPrizeExchangeDetail::getCostScore, po.getCostScore())
                .set(BPrizeExchangeDetail::getExchange, po.getExchange())

                .eq(BPrizeExchangeDetail::getId, po.getId())
                .eq(BPrizeExchangeDetail::getCreatedDate, po.getCreatedDate())
                .eq(BPrizeExchangeDetail::getModifiedDate, po.getModifiedDate())
                .eq(BPrizeExchangeDetail::getBenefitsId, po.getBenefitsId())
                .eq(BPrizeExchangeDetail::getVenderId, po.getVenderId())
                .eq(BPrizeExchangeDetail::getExchangeTime, po.getExchangeTime())
                .eq(BPrizeExchangeDetail::getBenefitsName, po.getBenefitsName())
                .eq(BPrizeExchangeDetail::getCostScore, po.getCostScore())
                .eq(BPrizeExchangeDetail::getExchange, po.getExchange());
        //todo 多余删除，不足补齐
        return bPrizeExchangeDetailDao.update(updateWrapper);
    }

    /**
     * 单条主键查询-BPrizeExchangeDetail
     *
     * @param id id
     * @return BPrizeExchangeDetail
     */
    @Override
    public BPrizeExchangeDetailBO queryBPrizeExchangeDetailById(Long id) {
        BPrizeExchangeDetail po = bPrizeExchangeDetailDao.getById(id);
        return BPrizeExchangeDetailConverter.INSTANCE.convertBPrizeExchangeDetailPoToBo(po);
    }


    /**
     * 单条查询-BPrizeExchangeDetail
     *
     * @param queryBo queryBo
     * @return BPrizeExchangeDetail
     */
    @Override
    public BPrizeExchangeDetailBO queryBPrizeExchangeDetailSingle(BPrizeExchangeDetailQueryBO queryBo) {
        LambdaQueryWrapper<BPrizeExchangeDetail> queryWrapper = Wrappers.lambdaQuery(BPrizeExchangeDetail.class)
                .eq(BPrizeExchangeDetail::getId, queryBo.getId())
                .eq(BPrizeExchangeDetail::getCreatedDate, queryBo.getCreatedDate())
                .eq(BPrizeExchangeDetail::getModifiedDate, queryBo.getModifiedDate())
                .eq(BPrizeExchangeDetail::getBenefitsId, queryBo.getBenefitsId())
                .eq(BPrizeExchangeDetail::getVenderId, queryBo.getVenderId())
                .eq(BPrizeExchangeDetail::getExchangeTime, queryBo.getExchangeTime())
                .eq(BPrizeExchangeDetail::getBenefitsName, queryBo.getBenefitsName())
                .eq(BPrizeExchangeDetail::getCostScore, queryBo.getCostScore())
                .eq(BPrizeExchangeDetail::getExchange, queryBo.getExchange());
        //todo 多余删除，不足补齐
        List<BPrizeExchangeDetail> list = bPrizeExchangeDetailDao.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return BPrizeExchangeDetailConverter.INSTANCE.convertBPrizeExchangeDetailPoToBo(list.get(0));
    }

    /**
     * 列表查询-BPrizeExchangeDetail
     *
     * @param queryBo queryBo
     * @return List<BPrizeExchangeDetail>
     */
    @Override
    public List<BPrizeExchangeDetailBO> queryBPrizeExchangeDetailList(BPrizeExchangeDetailQueryBO queryBo) {
        LambdaQueryWrapper<BPrizeExchangeDetail> queryWrapper = Wrappers.lambdaQuery(BPrizeExchangeDetail.class)
                .eq(BPrizeExchangeDetail::getId, queryBo.getId())
                .eq(BPrizeExchangeDetail::getCreatedDate, queryBo.getCreatedDate())
                .eq(BPrizeExchangeDetail::getModifiedDate, queryBo.getModifiedDate())
                .eq(BPrizeExchangeDetail::getBenefitsId, queryBo.getBenefitsId())
                .eq(BPrizeExchangeDetail::getVenderId, queryBo.getVenderId())
                .eq(BPrizeExchangeDetail::getExchangeTime, queryBo.getExchangeTime())
                .eq(BPrizeExchangeDetail::getBenefitsName, queryBo.getBenefitsName())
                .eq(BPrizeExchangeDetail::getCostScore, queryBo.getCostScore())
                .eq(BPrizeExchangeDetail::getExchange, queryBo.getExchange());

        List<BPrizeExchangeDetail> pos = bPrizeExchangeDetailDao.list(queryWrapper);
        return BPrizeExchangeDetailConverter.INSTANCE.convertBPrizeExchangeDetailPoToBoList(pos);
    }

    /**
     * 分页查询-BPrizeExchangeDetail
     *
     * @param queryBo queryBo
     * @return Page<BPrizeExchangeDetail>
     */
    @Override
    public PageBO<BPrizeExchangeDetailBO> queryBPrizeExchangeDetailPage(BPrizeExchangeDetailQueryBO queryBo) {
        LambdaQueryWrapper<BPrizeExchangeDetail> queryWrapper = Wrappers.lambdaQuery(BPrizeExchangeDetail.class)
                .eq(BPrizeExchangeDetail::getId, queryBo.getId())
                .eq(BPrizeExchangeDetail::getCreatedDate, queryBo.getCreatedDate())
                .eq(BPrizeExchangeDetail::getModifiedDate, queryBo.getModifiedDate())
                .eq(BPrizeExchangeDetail::getBenefitsId, queryBo.getBenefitsId())
                .eq(BPrizeExchangeDetail::getVenderId, queryBo.getVenderId())
                .eq(BPrizeExchangeDetail::getExchangeTime, queryBo.getExchangeTime())
                .eq(BPrizeExchangeDetail::getBenefitsName, queryBo.getBenefitsName())
                .eq(BPrizeExchangeDetail::getCostScore, queryBo.getCostScore())
                .eq(BPrizeExchangeDetail::getExchange, queryBo.getExchange());
        //todo 多余删除，不足补齐
        Page<BPrizeExchangeDetail> pageReq = new Page<>(0, 0, 0);
        Page<BPrizeExchangeDetail> page = bPrizeExchangeDetailDao.page(pageReq, queryWrapper);
        PageBO<BPrizeExchangeDetailBO> result = new PageBO<>((int) page.getTotal(), (int) page.getSize(), (int) page.getPages(), (int) page.getCurrent());
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            result.setList(page.getRecords().stream().map(BPrizeExchangeDetailConverter.INSTANCE::convertBPrizeExchangeDetailPoToBo).collect(Collectors.toList()));
        }

        return result;
    }

}

