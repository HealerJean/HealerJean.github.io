package com.healerjean.proj.controller;

import com.healerjean.proj.common.anno.ElParam;
import com.healerjean.proj.common.anno.LogIndex;
import com.healerjean.proj.common.anno.RedisLock;
import com.healerjean.proj.common.contants.RedisConstants;
import com.healerjean.proj.common.data.ValidateGroup;
import com.healerjean.proj.common.data.bo.BaseRes;
import com.healerjean.proj.common.data.bo.PageBO;
import com.healerjean.proj.common.data.bo.PageQueryBO;
import com.healerjean.proj.common.data.convert.PageConverter;
import com.healerjean.proj.common.data.req.PageReq;
import com.healerjean.proj.common.data.vo.PageVO;
import com.healerjean.proj.data.bo.UserDemoBO;
import com.healerjean.proj.data.bo.UserDemoQueryBO;
import com.healerjean.proj.data.converter.UserConverter;
import com.healerjean.proj.data.req.UserDemoQueryReq;
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
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("hlj")
@Api(tags = "UserDemo-控制器")
@Slf4j
public class UserDemoController {

    /**
     * userDemoService
     */
    @Resource
    private UserDemoService userDemoService;


    @ApiOperation("用户信息-新增")
    @RedisLock(RedisConstants.LockEnum.COMMON)
    @LogIndex
    @PostMapping("user/save")
    @ResponseBody
    public BaseRes<Boolean> saveUserDemo(@ElParam("#req.name") @RequestBody UserDemoSaveReq req) {
        String errorMessage = ValidateUtils.validate(req, ValidateGroup.SaveUserDemo.class);
        if (!ValidateUtils.COMMON_SUCCESS.equals(errorMessage)) {
            throw new ParameterException(errorMessage);
        }
        UserDemoBO userDemoBo = UserConverter.INSTANCE.covertUserDemoSaveReqToBo(req);
        boolean success = userDemoService.saveUserDemo(userDemoBo);
        return BaseRes.buildSuccess(success);
    }

    @ApiOperation("用户信息-删除")
    @LogIndex
    @DeleteMapping("user/{id}")
    public BaseRes<Boolean> deleteUserDemo(@PathVariable Long id) {
        boolean success = userDemoService.deleteUserDemo(id);
        return BaseRes.buildSuccess(success);
    }

    @ApiOperation("用户信息-修改")
    @LogIndex
    @PutMapping("user/{id}")
    @ResponseBody
    public BaseRes<Boolean> updateUserDemo(@NotNull @PathVariable Long id, @RequestBody UserDemoSaveReq req) {
        UserDemoBO userDemoBo = UserConverter.INSTANCE.covertUserDemoSaveReqToBo(req);
        userDemoBo.setId(id);
        boolean success = userDemoService.updateUserDemo(userDemoBo);
        return BaseRes.buildSuccess(success);
    }

    // @RedisCache(RedisConstants.CacheEnum.USER)
    @LogIndex(timeOut = 0)
    @ApiOperation("用户信息-单条查询")
    @GetMapping("user/{userId}")
    @ResponseBody
    public BaseRes<UserDemoVO> queryUserDemoSingle(@ElParam @PathVariable("userId") Long userId) {
        UserDemoBO userDemoBo = userDemoService.selectById(userId);
        UserDemoVO userDemoVo = UserConverter.INSTANCE.covertUserDemoBoToVo(userDemoBo);
        return BaseRes.buildSuccess(userDemoVo);
    }

    @ApiOperation("用户信息-列表查询")
    @LogIndex(timeOut = 0)
    @GetMapping("user/list")
    @ResponseBody
    public BaseRes<List<UserDemoVO>> queryUserDemoList(UserDemoQueryReq req) {
        UserDemoQueryBO userDemoQueryBo = UserConverter.INSTANCE.covertUserDemoQueryReqToBo(req);
        List<UserDemoBO> userDemoBos = userDemoService.queryUserDemoList(userDemoQueryBo);
        List<UserDemoVO> userDemoVos = UserConverter.INSTANCE.covertUserDemoBoToVoList(userDemoBos);
        return BaseRes.buildSuccess(userDemoVos);
    }

    @ApiOperation("用户信息-分页查询")
    @LogIndex
    @PostMapping("user/page")
    @ResponseBody
    public BaseRes<PageVO<UserDemoVO>> queryUserDemoPage(@RequestBody PageReq<UserDemoQueryReq> req) {
        PageQueryBO<UserDemoQueryBO> userDemoPageQuery = UserConverter.INSTANCE.covertUserDemoQueryPageReqToBo(req);
        PageBO<UserDemoBO> pageBo = userDemoService.queryUserDemoPage(userDemoPageQuery);
        List<UserDemoVO> userDemoVos = UserConverter.INSTANCE.covertUserDemoBoToVoList(pageBo.getList());
        PageVO<UserDemoVO> pageVo = PageConverter.INSTANCE.covertPageBoToVo(pageBo, userDemoVos);
        return BaseRes.buildSuccess(pageVo);
    }

    @ApiOperation("用户信息-分页查询全部")
    @LogIndex
    @PostMapping("user/pageAll")
    @ResponseBody
    public BaseRes<List<UserDemoVO>>  queryUserDemoPageAll(@RequestBody UserDemoQueryReq req) {
        UserDemoQueryBO userDemoQueryBo = UserConverter.INSTANCE.covertUserDemoQueryReqToBo(req);
        List<UserDemoBO> userDemoBos = userDemoService.queryUserDemoPageAll(userDemoQueryBo);
        List<UserDemoVO> userDemoVos = UserConverter.INSTANCE.covertUserDemoBoToVoList(userDemoBos);
        return BaseRes.buildSuccess(userDemoVos);
    }


}
