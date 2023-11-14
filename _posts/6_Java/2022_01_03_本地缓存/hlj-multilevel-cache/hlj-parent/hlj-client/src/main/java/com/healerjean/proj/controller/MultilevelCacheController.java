package com.healerjean.proj.controller;

import com.healerjean.proj.cache.MultilevelCache;
import com.healerjean.proj.cache.MultilevelCacheManager;
import com.healerjean.proj.common.anno.ElParam;
import com.healerjean.proj.common.anno.LogIndex;
import com.healerjean.proj.common.data.bo.BaseRes;
import com.healerjean.proj.common.enums.MultilevelCacheEnum;
import com.healerjean.proj.data.bo.UserDemoBO;
import com.healerjean.proj.data.convert.UserConverter;
import com.healerjean.proj.data.vo.UserDemoVO;
import com.healerjean.proj.service.UserDemoService;
import com.healerjean.proj.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * MultilevelCacheController
 *
 * @author zhangyujin
 * @date 2023/11/14
 */
@RestController
@RequestMapping("hlj/multilevelCache")
@Api(tags = "多级缓存-控制器")
@Slf4j
public class MultilevelCacheController {

    /**
     * userDemoService
     */
    @Resource
    private UserDemoService userDemoService;

    /**
     * multilevelCacheManager
     */
    @Resource
    private MultilevelCacheManager multilevelCacheManager;


    @ApiOperation("用户信息-单条查询")
    @LogIndex
    @GetMapping("user/{userId}")
    @ResponseBody
    public BaseRes<UserDemoVO> queryUserDemoSingle(@ElParam @PathVariable("userId") Long userId) {
        MultilevelCache multilevelCache = multilevelCacheManager.getCache(MultilevelCacheEnum.MultilevelCacheNameEnum.USER_CACHE.getCode());
        Object object = multilevelCache.get(userId,()-> null);
        if (Objects.nonNull(object)){
            log.info("[MultilevelCacheController#queryUserDemoSingle] 多级缓存获取成功, userDemo:{}", JsonUtils.toString(object));

            return BaseRes.buildSuccess();
        }
        UserDemoBO userDemoBo = userDemoService.selectById(userId);
        UserDemoVO userDemo = UserConverter.INSTANCE.covertUserDemoBoToVo(userDemoBo);
        multilevelCache.put(userId, userDemo);
        return BaseRes.buildSuccess(userDemo);
    }

}