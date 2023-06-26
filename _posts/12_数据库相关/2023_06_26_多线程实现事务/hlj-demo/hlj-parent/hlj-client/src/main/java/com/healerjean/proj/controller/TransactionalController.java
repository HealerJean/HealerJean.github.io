package com.healerjean.proj.controller;

import com.healerjean.proj.common.anno.LogIndex;
import com.healerjean.proj.common.data.bo.BaseRes;
import com.healerjean.proj.data.manager.TransactionalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zhangyujin
 * @date 2023/6/26$  10:34$
 */
@RestController
@RequestMapping("hlj/transactional")
@Api(tags = "事务控制-控制器")
@Slf4j
public class TransactionalController {

    /**
     * transactionalService
     */
    @Resource
    private TransactionalService transactionalService;

    @ApiOperation("多线程事务-回滚")
    @LogIndex
    @PostMapping("rollback")
    @ResponseBody
    public BaseRes<Boolean> rollback() {
         transactionalService.rollback();
        return BaseRes.buildSuccess();
    }

    @ApiOperation("多线程事务-执行")
    @LogIndex
    @PostMapping("submit")
    @ResponseBody
    public BaseRes<Boolean> buildSuccess() {
        transactionalService.submit();
        return BaseRes.buildSuccess();
    }

}
