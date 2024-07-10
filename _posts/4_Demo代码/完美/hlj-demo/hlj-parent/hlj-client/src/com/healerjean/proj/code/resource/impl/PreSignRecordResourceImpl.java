package com.healerjean.proj.code.com.healerjean.proj.template.resource.impl;

import com.healerjean.proj.template.bo.PreSignRecordQueryBO;
import com.healerjean.proj.template.bo.PreSignRecordBO;
import com.healerjean.proj.template.dto.PreSignRecordDTO;
import com.healerjean.proj.template.req.PreSignRecordSaveReq;
import com.healerjean.proj.template.req.PreSignRecordQueryReq;
import com.healerjean.proj.template.req.PreSignRecordDeleteReq;
import com.healerjean.proj.template.service.PreSignRecordService;
import com.healerjean.proj.template.resource.PreSignRecordResource;
import com.healerjean.proj.template.converter.PreSignRecordConverter;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * 预签约记录(PreSignRecord)Resource接口
 *
 * @author zhangyujin
 * @date 2024-07-10 10:12:22
 */
@Service
public class PreSignRecordResourceImpl implements PreSignRecordResource {

    /**
     * preSignRecordService
     */
    @Resource
    private PreSignRecordService preSignRecordService;


    /**
     * 保存-PreSignRecord
     *
     * @param req req
     * @return boolean
     */
    @Override
    public boolean savePreSignRecord(PreSignRecordSaveReq req) {
        PreSignRecordBO bo = PreSignRecordConverter.INSTANCE.convertPreSignRecordSaveReqToBo(req);
        return preSignRecordService.savePreSignRecord(bo);
    }

    /**
     * 删除-PreSignRecord
     *
     * @param req req
     * @return boolean
     */
    @Override
    public boolean deletePreSignRecord(PreSignRecordDeleteReq req) {
        PreSignRecordBO bo = PreSignRecordConverter.INSTANCE.convertPreSignRecordDeleteReqToBo(req);
        return preSignRecordService.deletePreSignRecord(bo);
    }

    /**
     * 更新-PreSignRecord
     *
     * @param req req
     * @return boolean
     */
    @Override
    public boolean updatePreSignRecord(PreSignRecordSaveReq req) {
        PreSignRecordBO bo = PreSignRecordConverter.INSTANCE.convertPreSignRecordSaveReqToBo(req);
        return preSignRecordService.updatePreSignRecord(bo);
    }

    /**
     * 单条查询-PreSignRecord
     *
     * @param req req
     * @return PreSignRecordDTO
     */
    @Override
    public PreSignRecordDTO queryPreSignRecordSingle(PreSignRecordQueryReq req) {
        PreSignRecordQueryBO queryBo = PreSignRecordConverter.INSTANCE.convertPreSignRecordQueryReqToBo(req);
        PreSignRecordBO bo = preSignRecordService.queryPreSignRecordSingle(queryBo);
        return PreSignRecordConverter.INSTANCE.convertPreSignRecordBoToDto(bo);
    }

    /**
     * 列表查询-PreSignRecord
     *
     * @param req req
     * @return List<PreSignRecordDTO>
     */
    @Override
    public List<PreSignRecordDTO> queryPreSignRecordList(PreSignRecordQueryReq req) {
        PreSignRecordQueryBO queryBo = PreSignRecordConverter.INSTANCE.convertPreSignRecordQueryReqToBo(req);
        List<PreSignRecordBO> bos = preSignRecordService.queryPreSignRecordList(queryBo);
        return PreSignRecordConverter.INSTANCE.convertPreSignRecordBoToDtoList(bos);
    }

}

