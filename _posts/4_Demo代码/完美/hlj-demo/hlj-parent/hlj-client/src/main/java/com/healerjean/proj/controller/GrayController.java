package com.healerjean.proj.controller;

import com.healerjean.proj.common.anno.LogIndex;
import com.healerjean.proj.common.data.bo.BaseRes;
import com.healerjean.proj.utils.gray.GrayEnum;
import com.healerjean.proj.utils.gray.GrayUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * CompressedController
 *
 * @author zhangyujin
 * @date 2025/1/7
 */
@RestController
@RequestMapping("hlj/gray")
@Api(tags = "GrayController")
@Slf4j
public class GrayController {

    @ApiOperation("hit")
    @LogIndex
    @PostMapping("hit")
    @ResponseBody
    public BaseRes<GrayEnum.GrayResEnum> getBitInfo(GrayEnum.GrayBusinessEnum grayBusinessEnum, String grayValue) {
        log.info("1:{}",GrayUtil.hitGray(GrayEnum.GrayBusinessEnum.BUSINESS_OOO1, "1"));
        log.info("2:{}",GrayUtil.hitGray(GrayEnum.GrayBusinessEnum.BUSINESS_OOO1, "2"));
        log.info("3:{}",GrayUtil.hitGray(GrayEnum.GrayBusinessEnum.BUSINESS_OOO1, "3"));
        log.info("4:{}",GrayUtil.hitGray(GrayEnum.GrayBusinessEnum.BUSINESS_OOO1, "4"));
        log.info("5:{}",GrayUtil.hitGray(GrayEnum.GrayBusinessEnum.BUSINESS_OOO1, "5"));
        log.info("6:{}",GrayUtil.hitGray(GrayEnum.GrayBusinessEnum.BUSINESS_OOO1, "6"));
        log.info("7:{}",GrayUtil.hitGray(GrayEnum.GrayBusinessEnum.BUSINESS_OOO1, "7"));
        log.info("8:{}",GrayUtil.hitGray(GrayEnum.GrayBusinessEnum.BUSINESS_OOO1, "8"));
        log.info("9:{}",GrayUtil.hitGray(GrayEnum.GrayBusinessEnum.BUSINESS_OOO1, "9"));

        return BaseRes.buildSuccess(GrayUtil.hitGray(grayBusinessEnum, grayValue));
    }

}
