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
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.util.RamUsageEstimator;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
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
            return BaseRes.buildSuccess((UserDemoVO) object);
        }
        UserDemoBO userDemoBo = userDemoService.selectById(userId);
        UserDemoVO userDemo = UserConverter.INSTANCE.covertUserDemoBoToVo(userDemoBo);
        multilevelCache.put(userId, userDemo);
        return BaseRes.buildSuccess(userDemo);
    }


    /**
     * RamUsageEstimator就是根据java对象在堆内存中的存储格式，通过计算Java对象头、实例数据、引用等的大小，相加而得，如果有引用，还能递归计算引用对象的大小。RamUsageEstimator的源码并不多，几百行，清晰可读。这里不进行一一解读了。它在初始化的时候会根据当前JVM运行环境、CPU架构、运行参数、是否开启指针压缩、JDK版本等综合计算对象头的大小，而实例数据部分则按照java基础数据类型的标准大小进行计
     */
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        System.out.println("map init value is " +  RamUsageEstimator.sizeOfObject(map));
        for (int i = 1000000; i < 6000000; i++) {
            map.put(String.valueOf(i), String.valueOf(i+1000000));
        }

        System.out.println("map size: 1  is " + RamUsageEstimator.sizeOfObject(map.get("1000000"))+ " byte");
        System.out.println("map size: " +map.size() +"  is " + RamUsageEstimator.sizeOfMap(map)+ " byte");
        System.out.println("map size: " +map.size() +"  is " + RamUsageEstimator.sizeOfObject(map)+ " byte");

        // JDK自带
        System.out.println("map size: " +map.size() +" is " + ObjectSizeCalculator.getObjectSize(map) + " byte");
    }

}