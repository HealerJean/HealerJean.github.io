package com.healerjean.proj.controller;

import com.healerjean.proj.common.anno.LogIndex;
import com.healerjean.proj.common.anno.RedisLock;
import com.healerjean.proj.common.contants.RedisConstants;
import com.healerjean.proj.common.data.bo.BaseRes;
import com.healerjean.proj.data.bo.EsTradeLogBO;
import com.healerjean.proj.data.converter.TradeLogConverter;
import com.healerjean.proj.data.req.EsTradeLogBatchReq;
import com.healerjean.proj.service.EsTradeLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * EsTradeLogController
 *
 * @author zhangyujin
 * @date 2024/4/26
 */
@RestController
@RequestMapping("hlj/esTrade")
@Api(tags = "EsTradeLog-控制器")
@Slf4j
public class EsTradeLogController {



    @Resource
    private EsTradeLogService esTradeLogService;


    @ApiOperation("新增-批量")
    @RedisLock(RedisConstants.LockEnum.COMMON)
    @LogIndex
    @PostMapping("batchSave")
    @ResponseBody
    public BaseRes<Boolean> batchSave( @RequestBody EsTradeLogBatchReq req) {
        List<EsTradeLogBO> tradeLogs = TradeLogConverter.INSTANCE.convertTradeLogDtoToBoList(req.getTradeLogs());
        esTradeLogService.batchSavenTradeLog(tradeLogs);
        return BaseRes.buildSuccess();
    }


}
