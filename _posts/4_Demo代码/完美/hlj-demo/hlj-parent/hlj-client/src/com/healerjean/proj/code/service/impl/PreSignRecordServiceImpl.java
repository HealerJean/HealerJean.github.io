package com.healerjean.proj.code.com.healerjean.proj.template.service.impl;

import com.healerjean.proj.template.po.PreSignRecord;
import com.healerjean.proj.template.bo.PreSignRecordQueryBO;
import com.healerjean.proj.template.bo.PreSignRecordBO;
import com.healerjean.proj.template.manager.PreSignRecordManager;
import com.healerjean.proj.template.service.PreSignRecordService;
import com.healerjean.proj.template.converter.PreSignRecordConverter;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * 预签约记录(PreSignRecord)Service
 *
 * @author zhangyujin
 * @date 2024-07-10 10:12:22
 */
@Service
public class PreSignRecordServiceImpl implements PreSignRecordService {

    /**
     * preSignRecordManager
     */
    @Resource
    private PreSignRecordManager preSignRecordManager;

    /**
     * 保存-PreSignRecord
     *
     * @param bo bo
     * @return boolean
     */
    @Override
    public boolean savePreSignRecord(PreSignRecordBO bo) {
        PreSignRecord po = PreSignRecordConverter.INSTANCE.convertPreSignRecordBoToPo(bo);
        return preSignRecordManager.savePreSignRecord(po);
    }

    /**
     * 删除-PreSignRecord
     *
     * @param bo bo
     * @return boolean
     */
    @Override
    public boolean deletePreSignRecord(PreSignRecordBO bo) {
        return preSignRecordManager.deletePreSignRecordById(bo.getId());
    }

    /**
     * 更新-PreSignRecord
     *
     * @param bo bo
     * @return boolean
     */
    @Override
    public boolean updatePreSignRecord(PreSignRecordBO bo) {
        PreSignRecord po = PreSignRecordConverter.INSTANCE.convertPreSignRecordBoToPo(bo);
        return preSignRecordManager.updatePreSignRecord(po);
    }

    /**
     * 单条查询-PreSignRecord
     *
     * @param queryBo queryBo
     * @return PreSignRecordBO
     */
    @Override
    public PreSignRecordBO queryPreSignRecordSingle(PreSignRecordQueryBO queryBo) {
        return preSignRecordManager.queryPreSignRecordSingle(queryBo);
    }

    /**
     * 列表查询-PreSignRecord
     *
     * @param queryBo queryBo
     * @return List<PreSignRecordBO>
     */
    @Override
    public List<PreSignRecordBO> queryPreSignRecordList(PreSignRecordQueryBO queryBo) {
        return preSignRecordManager.queryPreSignRecordList(queryBo);
    }

}

