package com.healerjean.proj.controller;

import com.healerjean.proj.common.constant.CommonConstants;
import com.healerjean.proj.common.dto.ValidateGroup;
import com.healerjean.proj.common.dto.ResponseBean;
import com.healerjean.proj.common.enums.ResponseEnum;
import com.healerjean.proj.common.exception.BusinessException;
import com.healerjean.proj.dto.DemoDTO;
import com.healerjean.proj.service.DemoEntityService;
import com.healerjean.proj.utils.validate.ValidateUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName DemoController
 * @date 2019/6/13  20:01.
 * @Description
 */
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "访问正常"),
        @ApiResponse(code = 301, message = "逻辑错误"),
        @ApiResponse(code = 500, message = "系统错误"),
        @ApiResponse(code = 401, message = "未认证"),
        @ApiResponse(code = 403, message = "禁止访问"),
        @ApiResponse(code = 404, message = "url错误")
})
@Api(description = "demo控制器")
@Controller
@RequestMapping("hlj/demo")
@Slf4j
public class DemoController {

    @Autowired
    private DemoEntityService demoEntityService;


    @ApiOperation(value = "jpaInsert",
            notes = "jpaInsert",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = DemoDTO.class)
    @PostMapping(value = "/jpaInsert", produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseBean jpaInsert(DemoDTO demoDTO) {
        log.info("样例--------jpa添加demo实体------数据信息{}", demoDTO);
        String validate = ValidateUtils.validate(demoDTO, ValidateGroup.HealerJean.class);
        if (!validate.equals(CommonConstants.COMMON_SUCCESS)) {
            throw new BusinessException(ResponseEnum.参数错误, validate);
        }
        return ResponseBean.buildSuccess(demoEntityService.jpaInsert(demoDTO));
    }


    @ApiOperation(value = "mybatisPlusInsert",
            notes = "mybatisPlusInsert",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = DemoDTO.class)
    @PostMapping(value = "mybatisPlusInsert", produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseBean mybatisPlus(DemoDTO demoDTO) {
        log.info("样例--------mybaits-plus添加demo实体------数据信息{}", demoDTO);
        String validate = ValidateUtils.validate(demoDTO, ValidateGroup.HealerJean.class);
        if (!validate.equals(CommonConstants.COMMON_SUCCESS)) {
            throw new BusinessException(ResponseEnum.参数错误, validate);
        }
        return ResponseBean.buildSuccess(demoEntityService.mybatisPlusInsert(demoDTO));
    }



    @ApiOperation(notes = "jpaFindById",
            value = "jpaFindById",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = DemoDTO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "demo主键", required = true, paramType = "path", dataType = "long"),
    })
    @GetMapping("jpaFindById/{id}")
    @ResponseBody
    public ResponseBean findById(@PathVariable Long id) {
        log.info("样例--------jpaFindById------数据Id{}", id);
        return ResponseBean.buildSuccess(demoEntityService.jpaFindById(id));
    }



    @ApiOperation(notes = "mybatisPlusFindById",
            value = "mybatisPlusFindById",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = DemoDTO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "demo主键", required = true, paramType = "path", dataType = "long"),
    })
    @GetMapping("mybatisPlusFindById/{id}")
    @ResponseBody
    public ResponseBean mybatisPlusFindById(@PathVariable Long id) {
        log.info("样例--------mybatisPlusFindById------数据Id{}", id);
        return ResponseBean.buildSuccess(demoEntityService.mybatisPlusFindById(id));
    }



    @ApiOperation(notes = "查询Demo实体类-列表",
            value = "查询Demo实体类-列表",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = DemoDTO.class)
    @GetMapping("demos")
    @ResponseBody
    public ResponseBean queryDemoList(DemoDTO demoDTO) {
        log.info("样例--------查询Demo实体类-列表------数据信息：{}", demoDTO);
        if (demoDTO.getPage()) {
            return ResponseBean.buildSuccess(demoEntityService.queryDemoPage(demoDTO));
        }
        return ResponseBean.buildSuccess(demoEntityService.queryDemoList(demoDTO));
    }


}
