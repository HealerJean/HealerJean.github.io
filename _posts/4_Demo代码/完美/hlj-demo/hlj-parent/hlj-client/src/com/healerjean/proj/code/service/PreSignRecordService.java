package com.healerjean.proj.code.com.healerjean.proj.template.service;

import com.healerjean.proj.template.bo.PreSignRecordQueryBO;
import com.healerjean.proj.template.bo.PreSignRecordBO;

import java.util.List;

/**
 * 预签约记录(PreSignRecord)Service接口
 *
 * @author zhangyujin
 * @date 2024-07-10 10:12:22
 */
public interface PreSignRecordService {

    /**
     * 保存-PreSignRecord
     *
     * @param bo bo
     * @return boolean
     */
    boolean savePreSignRecord(PreSignRecordBO bo);

    /**
     * 删除-PreSignRecord
     *
     * @param bo bo
     * @return boolean
     */
    boolean deletePreSignRecord(PreSignRecordBO bo);

    /**
     * 更新-PreSignRecord
     *
     * @param bo bo
     * @return boolean
     */
    boolean updatePreSignRecord(PreSignRecordBO bo);

    /**
     * 单条查询-PreSignRecord
     *
     * @param queryBo queryBo
     * @return PreSignRecordBO
     */
    PreSignRecordBO queryPreSignRecordSingle(PreSignRecordQueryBO queryBo);

    /**
     * 列表查询-PreSignRecord
     *
     * @param queryBo queryBo
     * @return List<PreSignRecordBO>
     */
    List<PreSignRecordBO> queryPreSignRecordList(PreSignRecordQueryBO queryBo);

}

