package com.healerjean.proj.controller;

import com.healerjean.proj.common.anno.LogIndex;
import com.healerjean.proj.common.data.bo.BaseRes;
import com.healerjean.proj.common.data.vo.EnumLabelVO;
import com.healerjean.proj.utils.EnumUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * DictController
 *
 * @author zhangyujin
 * @date 2025/2/26
 */
@RestController
@RequestMapping("hlj/dict")
@Api(tags = "DictController")
@Slf4j
public class DictController {

    @ApiOperation("getEnumLabelByClass")
    @LogIndex
    @GetMapping("getEnumLabelByClassName")
    @ResponseBody
    public BaseRes<List<EnumLabelVO>> getEnumLabelByClass(String className) {
        return BaseRes.buildSuccess(EnumUtils.getLabelByClazz(className));
    }

}
