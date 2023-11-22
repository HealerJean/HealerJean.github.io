package com.healerjean.proj.controller;

import com.alicp.jetcache.anno.CacheInvalidate;
import com.healerjean.proj.common.anno.ElParam;
import com.healerjean.proj.common.anno.LogIndex;
import com.healerjean.proj.common.data.ValidateGroup;
import com.healerjean.proj.common.data.bo.BaseRes;
import com.healerjean.proj.data.bo.UserDemoBO;
import com.healerjean.proj.data.convert.UserConverter;
import com.healerjean.proj.data.req.UserDemoSaveReq;
import com.healerjean.proj.data.vo.UserDemoVO;
import com.healerjean.proj.exceptions.ParameterException;
import com.healerjean.proj.service.UserDemoService;
import com.healerjean.proj.utils.validate.ValidateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * JetCacheController
 *
 * @author zhangyujin
 * @date 2023/11/21
 */
@RestController
@RequestMapping("api/jetcache/anno")
@Api(tags = "JetCacheAnnoController-控制器")
@Slf4j
public class JetCacheAnnoController {

    /**
     * userDemoService
     */
    @Resource
    private UserDemoService userDemoService;


    @ApiOperation("用户信息-新增")
    @LogIndex
    @PostMapping("user/save")
    @ResponseBody
    public BaseRes<UserDemoVO> saveUserDemo(@ElParam("#req.name") @RequestBody UserDemoSaveReq req) {
        String errorMessage = ValidateUtils.validate(req, ValidateGroup.SaveUserDemo.class);
        if (!ValidateUtils.COMMON_SUCCESS.equals(errorMessage)) {
            throw new ParameterException(errorMessage);
        }
        UserDemoBO userDemoBo = UserConverter.INSTANCE.covertUserDemoSaveReqToBo(req);
        boolean success = userDemoService.saveUserDemo(userDemoBo);
        if (Boolean.FALSE.equals(success)) {
            return BaseRes.buildFailure();
        }
        UserDemoVO userDemoVO = UserConverter.INSTANCE.covertUserDemoBoToVo(userDemoBo);
        return BaseRes.buildSuccess(userDemoVO);
    }


    @CacheInvalidate(name = "userCache:", key = "#id")
    @ApiOperation("用户信息-删除")
    @LogIndex
    @DeleteMapping("user/{id}")
    public BaseRes<Boolean> deleteUserDemo(@PathVariable Long id) {
        boolean success = userDemoService.deleteUserDemo(id);
        if (Boolean.FALSE.equals(success)) {
            return BaseRes.buildFailure();
        }
        return BaseRes.buildSuccess(success);
    }

    @ApiOperation("用户信息-修改")
    @LogIndex
    @PutMapping("user/{id}")
    @ResponseBody
    public BaseRes<UserDemoVO> updateUserDemo(@PathVariable Long id, @RequestBody UserDemoSaveReq req) {
        UserDemoBO userDemoBo = UserConverter.INSTANCE.covertUserDemoSaveReqToBo(req);
        userDemoBo.setId(id);
        boolean success = userDemoService.updateUserDemo(userDemoBo);
        if (Boolean.FALSE.equals(success)) {
            return BaseRes.buildFailure();
        }
        UserDemoVO userDemoVO = UserConverter.INSTANCE.covertUserDemoBoToVo(userDemoBo);
        return BaseRes.buildSuccess(userDemoVO);
    }

    @LogIndex
    @ApiOperation("用户信息-单条查询")
    @GetMapping("user/{userId}")
    @ResponseBody
    public BaseRes<UserDemoVO> queryUserDemoSingle(@ElParam @PathVariable("userId") Long userId) {
        UserDemoBO userDemoBo = userDemoService.selectById(userId);
        UserDemoVO userDemoVo = UserConverter.INSTANCE.covertUserDemoBoToVo(userDemoBo);
        return BaseRes.buildSuccess(userDemoVo);
    }


}
