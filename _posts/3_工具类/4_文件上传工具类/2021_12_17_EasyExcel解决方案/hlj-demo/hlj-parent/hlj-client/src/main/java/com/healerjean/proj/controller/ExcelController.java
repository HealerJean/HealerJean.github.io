package com.healerjean.proj.controller;

import com.healerjean.proj.service.UserDemoService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * ExcelController
 *
 * @author zhangyujin
 * @date 2023/6/26$  18:17$
 */
@RestController
@RequestMapping("hlj/excel")
@Api(tags = "Excel-控制器")
@Slf4j
public class ExcelController {

    /**
     * userDemoService
     */
    @Resource
    private UserDemoService userDemoService;

}
