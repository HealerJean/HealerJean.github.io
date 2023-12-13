package com.healerjean.proj.template.resource;

import com.healerjean.proj.template.dto.BPrizeExchangeDetailDTO;
import com.healerjean.proj.template.req.BPrizeExchangeDetailSaveReq;
import com.healerjean.proj.template.req.BPrizeExchangeDetailQueryReq;
import com.healerjean.proj.template.req.BPrizeExchangeDetailDeleteReq;

import java.util.List;

/**
 * 权益奖品兑换明细(BPrizeExchangeDetail)Service接口
 *
 * @author zhangyujin
 * @date 2023-12-12 11:07:29
 */
public interface BPrizeExchangeDetailResource {

    /**
     * 保存-BPrizeExchangeDetail
     *
     * @param req req
     * @return boolean
     */
    boolean saveBPrizeExchangeDetail(BPrizeExchangeDetailSaveReq req);

    /**
     * 删除-BPrizeExchangeDetail
     *
     * @param req req
     * @return boolean
     */
    boolean deleteBPrizeExchangeDetail(BPrizeExchangeDetailDeleteReq req);

    /**
     * 更新-BPrizeExchangeDetail
     *
     * @param req req
     * @return boolean
     */
    boolean updateBPrizeExchangeDetail(BPrizeExchangeDetailSaveReq req);

    /**
     * 单条查询-BPrizeExchangeDetail
     *
     * @param req req
     * @return BPrizeExchangeDetailDTO
     */
    BPrizeExchangeDetailDTO queryBPrizeExchangeDetailSingle(BPrizeExchangeDetailQueryReq req);

    /**
     * 列表查询-BPrizeExchangeDetail
     *
     * @param req req
     * @return List<BPrizeExchangeDetailDTO>
     */
    List<BPrizeExchangeDetailDTO> queryBPrizeExchangeDetailList(BPrizeExchangeDetailQueryReq req);

}

