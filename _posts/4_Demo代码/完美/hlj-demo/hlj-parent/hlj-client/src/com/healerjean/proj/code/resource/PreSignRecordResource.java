package com.healerjean.proj.code.com.healerjean.proj.template.resource;

import com.healerjean.proj.template.dto.PreSignRecordDTO;
import com.healerjean.proj.template.req.PreSignRecordSaveReq;
import com.healerjean.proj.template.req.PreSignRecordQueryReq;
import com.healerjean.proj.template.req.PreSignRecordDeleteReq;

import java.util.List;

/**
 * 预签约记录(PreSignRecord)Service接口
 *
 * @author zhangyujin
 * @date 2024-07-10 10:12:22
 */
public interface PreSignRecordResource {

    /**
     * 保存-PreSignRecord
     *
     * @param req req
     * @return boolean
     */
    boolean savePreSignRecord(PreSignRecordSaveReq req);

    /**
     * 删除-PreSignRecord
     *
     * @param req req
     * @return boolean
     */
    boolean deletePreSignRecord(PreSignRecordDeleteReq req);

    /**
     * 更新-PreSignRecord
     *
     * @param req req
     * @return boolean
     */
    boolean updatePreSignRecord(PreSignRecordSaveReq req);

    /**
     * 单条查询-PreSignRecord
     *
     * @param req req
     * @return PreSignRecordDTO
     */
    PreSignRecordDTO queryPreSignRecordSingle(PreSignRecordQueryReq req);

    /**
     * 列表查询-PreSignRecord
     *
     * @param req req
     * @return List<PreSignRecordDTO>
     */
    List<PreSignRecordDTO> queryPreSignRecordList(PreSignRecordQueryReq req);

}

