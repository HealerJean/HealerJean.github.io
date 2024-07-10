package com.healerjean.proj.code.com.healerjean.proj.template.manager.impl;

import com.healerjean.proj.template.po.PreSignRecord;
import com.healerjean.proj.template.bo.PreSignRecordBO;
import com.healerjean.proj.template.bo.PageBO;
import com.healerjean.proj.template.bo.PreSignRecordQueryBO;
import com.healerjean.proj.template.dao.PreSignRecordDao;
import com.healerjean.proj.template.manager.PreSignRecordManager;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.util.CollectionUtils;
import org.springframework.stereotype.Service;


/**
 * 预签约记录(PreSignRecord)Manager接口
 *
 * @author zhangyujin
 * @date 2024-07-10 10:12:22
 */
@Service
public class PreSignRecordManagerImpl implements PreSignRecordManager {

    /**
     * preSignRecordDao
     */
    @Resource
    private PreSignRecordDao preSignRecordDao;

    /**
     * 保存-PreSignRecord
     *
     * @param po po
     * @return boolean
     */
    @Override
    public boolean savePreSignRecord(PreSignRecord po) {
        return preSignRecordDao.save(po);
    }

    /**
     * 删除-PreSignRecord
     *
     * @param id id
     * @return boolean
     */
    @Override
    public boolean deletePreSignRecordById(Long id) {
        //todo
        return false;
    }

    /**
     * 更新-PreSignRecord
     *
     * @param po po
     * @return boolean
     */
    @Override
    public boolean updatePreSignRecord(PreSignRecord po) {
        LambdaUpdateWrapper<PreSignRecord> updateWrapper = Wrappers.lambdaUpdate(PreSignRecord.class)
                .set(PreSignRecord::getId, po.getId())
                .set(PreSignRecord::getInsuranceId, po.getInsuranceId())
                .set(PreSignRecord::getCustomerCode, po.getCustomerCode())
                .set(PreSignRecord::getCustomerType, po.getCustomerType())
                .set(PreSignRecord::getBizId, po.getBizId())
                .set(PreSignRecord::getType, po.getType())
                .set(PreSignRecord::getSignChannelSource, po.getSignChannelSource())
                .set(PreSignRecord::getStartTime, po.getStartTime())
                .set(PreSignRecord::getEndTime, po.getEndTime())
                .set(PreSignRecord::getStatus, po.getStatus())
                .set(PreSignRecord::getSignFailReason, po.getSignFailReason())
                .set(PreSignRecord::getBizInfo, po.getBizInfo())
                .set(PreSignRecord::getExt, po.getExt())
                .set(PreSignRecord::getVersion, po.getVersion())
                .set(PreSignRecord::getPreSignTime, po.getPreSignTime())
                .set(PreSignRecord::getSignUser, po.getSignUser())
                .set(PreSignRecord::getRefSignUpStatusId, po.getRefSignUpStatusId())
                .set(PreSignRecord::getOperateTime, po.getOperateTime())
                .set(PreSignRecord::getOperator, po.getOperator())
                .set(PreSignRecord::getDeleteFlag, po.getDeleteFlag())
                .set(PreSignRecord::getModifiedTime, po.getModifiedTime())
                .set(PreSignRecord::getCreatedTime, po.getCreatedTime())

                .eq(PreSignRecord::getId, po.getId())
                .eq(PreSignRecord::getInsuranceId, po.getInsuranceId())
                .eq(PreSignRecord::getCustomerCode, po.getCustomerCode())
                .eq(PreSignRecord::getCustomerType, po.getCustomerType())
                .eq(PreSignRecord::getBizId, po.getBizId())
                .eq(PreSignRecord::getType, po.getType())
                .eq(PreSignRecord::getSignChannelSource, po.getSignChannelSource())
                .eq(PreSignRecord::getStartTime, po.getStartTime())
                .eq(PreSignRecord::getEndTime, po.getEndTime())
                .eq(PreSignRecord::getStatus, po.getStatus())
                .eq(PreSignRecord::getSignFailReason, po.getSignFailReason())
                .eq(PreSignRecord::getBizInfo, po.getBizInfo())
                .eq(PreSignRecord::getExt, po.getExt())
                .eq(PreSignRecord::getVersion, po.getVersion())
                .eq(PreSignRecord::getPreSignTime, po.getPreSignTime())
                .eq(PreSignRecord::getSignUser, po.getSignUser())
                .eq(PreSignRecord::getRefSignUpStatusId, po.getRefSignUpStatusId())
                .eq(PreSignRecord::getOperateTime, po.getOperateTime())
                .eq(PreSignRecord::getOperator, po.getOperator())
                .eq(PreSignRecord::getDeleteFlag, po.getDeleteFlag())
                .eq(PreSignRecord::getModifiedTime, po.getModifiedTime())
                .eq(PreSignRecord::getCreatedTime, po.getCreatedTime());
        //todo 多余删除，不足补齐
        return preSignRecordDao.update(updateWrapper);
    }

    /**
     * 单条主键查询-PreSignRecord
     *
     * @param id id
     * @return PreSignRecord
     */
    @Override
    public PreSignRecordBO queryPreSignRecordById(Long id) {
        PreSignRecord po = preSignRecordDao.getById(id);
        return PreSignRecordConverter.INSTANCE.convertPreSignRecordPoToBo(po);
    }


    /**
     * 单条查询-PreSignRecord
     *
     * @param queryBo queryBo
     * @return PreSignRecord
     */
    @Override
    public PreSignRecordBO queryPreSignRecordSingle(PreSignRecordQueryBO queryBo) {
        LambdaQueryWrapper<PreSignRecord> queryWrapper = Wrappers.lambdaQuery(PreSignRecord.class)
                .eq(PreSignRecord::getId, queryBo.getId())
                .eq(PreSignRecord::getInsuranceId, queryBo.getInsuranceId())
                .eq(PreSignRecord::getCustomerCode, queryBo.getCustomerCode())
                .eq(PreSignRecord::getCustomerType, queryBo.getCustomerType())
                .eq(PreSignRecord::getBizId, queryBo.getBizId())
                .eq(PreSignRecord::getType, queryBo.getType())
                .eq(PreSignRecord::getSignChannelSource, queryBo.getSignChannelSource())
                .eq(PreSignRecord::getStartTime, queryBo.getStartTime())
                .eq(PreSignRecord::getEndTime, queryBo.getEndTime())
                .eq(PreSignRecord::getStatus, queryBo.getStatus())
                .eq(PreSignRecord::getSignFailReason, queryBo.getSignFailReason())
                .eq(PreSignRecord::getBizInfo, queryBo.getBizInfo())
                .eq(PreSignRecord::getExt, queryBo.getExt())
                .eq(PreSignRecord::getVersion, queryBo.getVersion())
                .eq(PreSignRecord::getPreSignTime, queryBo.getPreSignTime())
                .eq(PreSignRecord::getSignUser, queryBo.getSignUser())
                .eq(PreSignRecord::getRefSignUpStatusId, queryBo.getRefSignUpStatusId())
                .eq(PreSignRecord::getOperateTime, queryBo.getOperateTime())
                .eq(PreSignRecord::getOperator, queryBo.getOperator())
                .eq(PreSignRecord::getDeleteFlag, queryBo.getDeleteFlag())
                .eq(PreSignRecord::getModifiedTime, queryBo.getModifiedTime())
                .eq(PreSignRecord::getCreatedTime, queryBo.getCreatedTime());
        //todo 多余删除，不足补齐
        List<PreSignRecord> list = preSignRecordDao.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return PreSignRecordConverter.INSTANCE.convertPreSignRecordPoToBo(list.get(0));
    }

    /**
     * 列表查询-PreSignRecord
     *
     * @param queryBo queryBo
     * @return List<PreSignRecord>
     */
    @Override
    public List<PreSignRecordBO> queryPreSignRecordList(PreSignRecordQueryBO queryBo) {
        LambdaQueryWrapper<PreSignRecord> queryWrapper = Wrappers.lambdaQuery(PreSignRecord.class)
                .eq(PreSignRecord::getId, queryBo.getId())
                .eq(PreSignRecord::getInsuranceId, queryBo.getInsuranceId())
                .eq(PreSignRecord::getCustomerCode, queryBo.getCustomerCode())
                .eq(PreSignRecord::getCustomerType, queryBo.getCustomerType())
                .eq(PreSignRecord::getBizId, queryBo.getBizId())
                .eq(PreSignRecord::getType, queryBo.getType())
                .eq(PreSignRecord::getSignChannelSource, queryBo.getSignChannelSource())
                .eq(PreSignRecord::getStartTime, queryBo.getStartTime())
                .eq(PreSignRecord::getEndTime, queryBo.getEndTime())
                .eq(PreSignRecord::getStatus, queryBo.getStatus())
                .eq(PreSignRecord::getSignFailReason, queryBo.getSignFailReason())
                .eq(PreSignRecord::getBizInfo, queryBo.getBizInfo())
                .eq(PreSignRecord::getExt, queryBo.getExt())
                .eq(PreSignRecord::getVersion, queryBo.getVersion())
                .eq(PreSignRecord::getPreSignTime, queryBo.getPreSignTime())
                .eq(PreSignRecord::getSignUser, queryBo.getSignUser())
                .eq(PreSignRecord::getRefSignUpStatusId, queryBo.getRefSignUpStatusId())
                .eq(PreSignRecord::getOperateTime, queryBo.getOperateTime())
                .eq(PreSignRecord::getOperator, queryBo.getOperator())
                .eq(PreSignRecord::getDeleteFlag, queryBo.getDeleteFlag())
                .eq(PreSignRecord::getModifiedTime, queryBo.getModifiedTime())
                .eq(PreSignRecord::getCreatedTime, queryBo.getCreatedTime());

        List<PreSignRecord> pos = preSignRecordDao.list(queryWrapper);
        return PreSignRecordConverter.INSTANCE.convertPreSignRecordPoToBoList(pos);
    }

    /**
     * 分页查询-PreSignRecord
     *
     * @param queryBo queryBo
     * @return Page<PreSignRecord>
     */
    @Override
    public PageBO<PreSignRecordBO> queryPreSignRecordPage(PreSignRecordQueryBO queryBo) {
        LambdaQueryWrapper<PreSignRecord> queryWrapper = Wrappers.lambdaQuery(PreSignRecord.class)
                .eq(PreSignRecord::getId, queryBo.getId())
                .eq(PreSignRecord::getInsuranceId, queryBo.getInsuranceId())
                .eq(PreSignRecord::getCustomerCode, queryBo.getCustomerCode())
                .eq(PreSignRecord::getCustomerType, queryBo.getCustomerType())
                .eq(PreSignRecord::getBizId, queryBo.getBizId())
                .eq(PreSignRecord::getType, queryBo.getType())
                .eq(PreSignRecord::getSignChannelSource, queryBo.getSignChannelSource())
                .eq(PreSignRecord::getStartTime, queryBo.getStartTime())
                .eq(PreSignRecord::getEndTime, queryBo.getEndTime())
                .eq(PreSignRecord::getStatus, queryBo.getStatus())
                .eq(PreSignRecord::getSignFailReason, queryBo.getSignFailReason())
                .eq(PreSignRecord::getBizInfo, queryBo.getBizInfo())
                .eq(PreSignRecord::getExt, queryBo.getExt())
                .eq(PreSignRecord::getVersion, queryBo.getVersion())
                .eq(PreSignRecord::getPreSignTime, queryBo.getPreSignTime())
                .eq(PreSignRecord::getSignUser, queryBo.getSignUser())
                .eq(PreSignRecord::getRefSignUpStatusId, queryBo.getRefSignUpStatusId())
                .eq(PreSignRecord::getOperateTime, queryBo.getOperateTime())
                .eq(PreSignRecord::getOperator, queryBo.getOperator())
                .eq(PreSignRecord::getDeleteFlag, queryBo.getDeleteFlag())
                .eq(PreSignRecord::getModifiedTime, queryBo.getModifiedTime())
                .eq(PreSignRecord::getCreatedTime, queryBo.getCreatedTime());
        //todo 多余删除，不足补齐
        Page<PreSignRecord> pageReq = new Page<>(0, 0, 0);
        Page<PreSignRecord> page = preSignRecordDao.page(pageReq, queryWrapper);
        PageBO<PreSignRecordBO> result = new PageBO<>((int) page.getTotal(), (int) page.getSize(), (int) page.getPages(), (int) page.getCurrent());
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            result.setList(page.getRecords().stream().map(PreSignRecordConverter.INSTANCE::convertPreSignRecordPoToBo).collect(Collectors.toList()));
        }

        return result;
    }

}

