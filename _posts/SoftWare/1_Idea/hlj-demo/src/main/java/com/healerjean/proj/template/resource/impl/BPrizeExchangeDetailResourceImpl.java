package com.healerjean.proj.template.resource.impl;

import com.healerjean.proj.template.bo.BPrizeExchangeDetailQueryBO;
import com.healerjean.proj.template.bo.BPrizeExchangeDetailBO;
import com.healerjean.proj.template.dto.BPrizeExchangeDetailDTO;
import com.healerjean.proj.template.req.BPrizeExchangeDetailSaveReq;
import com.healerjean.proj.template.req.BPrizeExchangeDetailQueryReq;
import com.healerjean.proj.template.req.BPrizeExchangeDetailDeleteReq;
import com.healerjean.proj.template.service.BPrizeExchangeDetailService;
import com.healerjean.proj.template.resource.BPrizeExchangeDetailResource;
import com.healerjean.proj.template.converter.BPrizeExchangeDetailConverter;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * 权益奖品兑换明细(BPrizeExchangeDetail)Resource接口
 *
 * @author zhangyujin
 * @date 2023-12-12 11:07:29
 */
@Service
public class BPrizeExchangeDetailResourceImpl implements BPrizeExchangeDetailResource {

    /**
     * bPrizeExchangeDetailService
     */
    @Resource
    private BPrizeExchangeDetailService bPrizeExchangeDetailService;


    /**
     * 保存-BPrizeExchangeDetail
     *
     * @param req req
     * @return boolean
     */
    @Override
    public boolean saveBPrizeExchangeDetail(BPrizeExchangeDetailSaveReq req) {
        BPrizeExchangeDetailBO bo = BPrizeExchangeDetailConverter.INSTANCE.convertBPrizeExchangeDetailSaveReqToBo(req);
        return bPrizeExchangeDetailService.saveBPrizeExchangeDetail(bo);
    }

    /**
     * 删除-BPrizeExchangeDetail
     *
     * @param req req
     * @return boolean
     */
    @Override
    public boolean deleteBPrizeExchangeDetail(BPrizeExchangeDetailDeleteReq req) {
        BPrizeExchangeDetailBO bo = BPrizeExchangeDetailConverter.INSTANCE.convertBPrizeExchangeDetailDeleteReqToBo(req);
        return bPrizeExchangeDetailService.deleteBPrizeExchangeDetail(bo);
    }

    /**
     * 更新-BPrizeExchangeDetail
     *
     * @param req req
     * @return boolean
     */
    @Override
    public boolean updateBPrizeExchangeDetail(BPrizeExchangeDetailSaveReq req) {
        BPrizeExchangeDetailBO bo = BPrizeExchangeDetailConverter.INSTANCE.convertBPrizeExchangeDetailSaveReqToBo(req);
        return bPrizeExchangeDetailService.updateBPrizeExchangeDetail(bo);
    }

    /**
     * 单条查询-BPrizeExchangeDetail
     *
     * @param req req
     * @return BPrizeExchangeDetailDTO
     */
    @Override
    public BPrizeExchangeDetailDTO queryBPrizeExchangeDetailSingle(BPrizeExchangeDetailQueryReq req) {
        BPrizeExchangeDetailQueryBO queryBo = BPrizeExchangeDetailConverter.INSTANCE.convertBPrizeExchangeDetailQueryReqToBo(req);
        BPrizeExchangeDetailBO bo = bPrizeExchangeDetailService.queryBPrizeExchangeDetailSingle(queryBo);
        return BPrizeExchangeDetailConverter.INSTANCE.convertBPrizeExchangeDetailBoToDto(bo);
    }

    /**
     * 列表查询-BPrizeExchangeDetail
     *
     * @param req req
     * @return List<BPrizeExchangeDetailDTO>
     */
    @Override
    public List<BPrizeExchangeDetailDTO> queryBPrizeExchangeDetailList(BPrizeExchangeDetailQueryReq req) {
        BPrizeExchangeDetailQueryBO queryBo = BPrizeExchangeDetailConverter.INSTANCE.convertBPrizeExchangeDetailQueryReqToBo(req);
        List<BPrizeExchangeDetailBO> bos = bPrizeExchangeDetailService.queryBPrizeExchangeDetailList(queryBo);
        return BPrizeExchangeDetailConverter.INSTANCE.convertBPrizeExchangeDetailBoToDtoList(bos);
    }

}

