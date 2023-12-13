package com.healerjean.proj.template.resource.impl;

import com.healerjean.proj.template.bo.BPrizeStockDetailQueryBO;
import com.healerjean.proj.template.bo.BPrizeStockDetailBO;
import com.healerjean.proj.template.dto.BPrizeStockDetailDTO;
import com.healerjean.proj.template.req.BPrizeStockDetailSaveReq;
import com.healerjean.proj.template.req.BPrizeStockDetailQueryReq;
import com.healerjean.proj.template.req.BPrizeStockDetailDeleteReq;
import com.healerjean.proj.template.service.BPrizeStockDetailService;
import com.healerjean.proj.template.resource.BPrizeStockDetailResource;
import com.healerjean.proj.template.converter.BPrizeStockDetailConverter;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * 权益奖品增减库存明细表(BPrizeStockDetail)Resource接口
 *
 * @author zhangyujin
 * @date 2023-12-12 11:07:29
 */
@Service
public class BPrizeStockDetailResourceImpl implements BPrizeStockDetailResource {

    /**
     * bPrizeStockDetailService
     */
    @Resource
    private BPrizeStockDetailService bPrizeStockDetailService;


    /**
     * 保存-BPrizeStockDetail
     *
     * @param req req
     * @return boolean
     */
    @Override
    public boolean saveBPrizeStockDetail(BPrizeStockDetailSaveReq req) {
        BPrizeStockDetailBO bo = BPrizeStockDetailConverter.INSTANCE.convertBPrizeStockDetailSaveReqToBo(req);
        return bPrizeStockDetailService.saveBPrizeStockDetail(bo);
    }

    /**
     * 删除-BPrizeStockDetail
     *
     * @param req req
     * @return boolean
     */
    @Override
    public boolean deleteBPrizeStockDetail(BPrizeStockDetailDeleteReq req) {
        BPrizeStockDetailBO bo = BPrizeStockDetailConverter.INSTANCE.convertBPrizeStockDetailDeleteReqToBo(req);
        return bPrizeStockDetailService.deleteBPrizeStockDetail(bo);
    }

    /**
     * 更新-BPrizeStockDetail
     *
     * @param req req
     * @return boolean
     */
    @Override
    public boolean updateBPrizeStockDetail(BPrizeStockDetailSaveReq req) {
        BPrizeStockDetailBO bo = BPrizeStockDetailConverter.INSTANCE.convertBPrizeStockDetailSaveReqToBo(req);
        return bPrizeStockDetailService.updateBPrizeStockDetail(bo);
    }

    /**
     * 单条查询-BPrizeStockDetail
     *
     * @param req req
     * @return BPrizeStockDetailDTO
     */
    @Override
    public BPrizeStockDetailDTO queryBPrizeStockDetailSingle(BPrizeStockDetailQueryReq req) {
        BPrizeStockDetailQueryBO queryBo = BPrizeStockDetailConverter.INSTANCE.convertBPrizeStockDetailQueryReqToBo(req);
        BPrizeStockDetailBO bo = bPrizeStockDetailService.queryBPrizeStockDetailSingle(queryBo);
        return BPrizeStockDetailConverter.INSTANCE.convertBPrizeStockDetailBoToDto(bo);
    }

    /**
     * 列表查询-BPrizeStockDetail
     *
     * @param req req
     * @return List<BPrizeStockDetailDTO>
     */
    @Override
    public List<BPrizeStockDetailDTO> queryBPrizeStockDetailList(BPrizeStockDetailQueryReq req) {
        BPrizeStockDetailQueryBO queryBo = BPrizeStockDetailConverter.INSTANCE.convertBPrizeStockDetailQueryReqToBo(req);
        List<BPrizeStockDetailBO> bos = bPrizeStockDetailService.queryBPrizeStockDetailList(queryBo);
        return BPrizeStockDetailConverter.INSTANCE.convertBPrizeStockDetailBoToDtoList(bos);
    }

}

