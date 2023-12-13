package com.healerjean.proj.template.resource;

import com.healerjean.proj.template.dto.BPrizeStockDetailDTO;
import com.healerjean.proj.template.req.BPrizeStockDetailSaveReq;
import com.healerjean.proj.template.req.BPrizeStockDetailQueryReq;
import com.healerjean.proj.template.req.BPrizeStockDetailDeleteReq;

import java.util.List;

/**
 * 权益奖品增减库存明细表(BPrizeStockDetail)Service接口
 *
 * @author zhangyujin
 * @date 2023-12-12 11:07:29
 */
public interface BPrizeStockDetailResource {

    /**
     * 保存-BPrizeStockDetail
     *
     * @param req req
     * @return boolean
     */
    boolean saveBPrizeStockDetail(BPrizeStockDetailSaveReq req);

    /**
     * 删除-BPrizeStockDetail
     *
     * @param req req
     * @return boolean
     */
    boolean deleteBPrizeStockDetail(BPrizeStockDetailDeleteReq req);

    /**
     * 更新-BPrizeStockDetail
     *
     * @param req req
     * @return boolean
     */
    boolean updateBPrizeStockDetail(BPrizeStockDetailSaveReq req);

    /**
     * 单条查询-BPrizeStockDetail
     *
     * @param req req
     * @return BPrizeStockDetailDTO
     */
    BPrizeStockDetailDTO queryBPrizeStockDetailSingle(BPrizeStockDetailQueryReq req);

    /**
     * 列表查询-BPrizeStockDetail
     *
     * @param req req
     * @return List<BPrizeStockDetailDTO>
     */
    List<BPrizeStockDetailDTO> queryBPrizeStockDetailList(BPrizeStockDetailQueryReq req);

}

