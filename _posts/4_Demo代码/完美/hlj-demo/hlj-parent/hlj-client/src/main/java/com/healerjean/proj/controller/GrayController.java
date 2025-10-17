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
        log.info("1:{}",GrayUtil.hitGray( "1", GrayEnum.GrayBusinessEnum.BUSINESS_OOO1));
        log.info("2:{}",GrayUtil.hitGray( "2", GrayEnum.GrayBusinessEnum.BUSINESS_OOO1));
        log.info("3:{}",GrayUtil.hitGray( "3", GrayEnum.GrayBusinessEnum.BUSINESS_OOO1));
        log.info("4:{}",GrayUtil.hitGray( "4", GrayEnum.GrayBusinessEnum.BUSINESS_OOO1));
        log.info("5:{}",GrayUtil.hitGray( "5", GrayEnum.GrayBusinessEnum.BUSINESS_OOO1));
        log.info("6:{}",GrayUtil.hitGray( "6", GrayEnum.GrayBusinessEnum.BUSINESS_OOO1));
        log.info("7:{}",GrayUtil.hitGray( "7", GrayEnum.GrayBusinessEnum.BUSINESS_OOO1));
        log.info("8:{}",GrayUtil.hitGray( "8", GrayEnum.GrayBusinessEnum.BUSINESS_OOO1));
        log.info("9:{}",GrayUtil.hitGray( "9", GrayEnum.GrayBusinessEnum.BUSINESS_OOO1));

        return BaseRes.buildSuccess(GrayUtil.hitGray(grayValue, grayBusinessEnum));
    }

}
