package com.healerjean.proj.code.com.healerjean.proj.template.manager;

import com.healerjean.proj.template.po.PreSignRecord;
import com.healerjean.proj.template.bo.PreSignRecordBO;
import com.healerjean.proj.template.bo.PageBO;
import com.healerjean.proj.template.bo.PreSignRecordQueryBO;

import java.util.List;

/**
 * 预签约记录(PreSignRecord)Manager接口
 *
 * @author zhangyujin
 * @date 2024-07-10 10:12:22
 */
public interface PreSignRecordManager {

    /**
     * 保存-PreSignRecord
     *
     * @param po po
     * @return boolean
     */
    boolean savePreSignRecord(PreSignRecord po);

    /**
     * 删除-PreSignRecord
     *
     * @param id id
     * @return boolean
     */
    boolean deletePreSignRecordById(Long id);

    /**
     * 更新-PreSignRecord
     *
     * @param po po
     * @return boolean
     */
    boolean updatePreSignRecord(PreSignRecord po);

    /**
     * 单条主键查询-PreSignRecord
     *
     * @param id id
     * @return PreSignRecord
     */
    PreSignRecordBO queryPreSignRecordById(Long id);


    /**
     * 单条查询-PreSignRecord
     *
     * @param queryBo queryBo
     * @return PreSignRecord
     */
    PreSignRecordBO queryPreSignRecordSingle(PreSignRecordQueryBO queryBo);

    /**
     * 列表查询-PreSignRecord
     *
     * @param queryBo queryBo
     * @return List<PreSignRecord>
     */
    List<PreSignRecordBO> queryPreSignRecordList(PreSignRecordQueryBO queryBo);

    /**
     * 分页查询-PreSignRecord
     *
     * @param queryBo queryBo
     * @return Page<PreSignRecord>
     */
    PageBO<PreSignRecordBO> queryPreSignRecordPage(PreSignRecordQueryBO queryBo);

}

