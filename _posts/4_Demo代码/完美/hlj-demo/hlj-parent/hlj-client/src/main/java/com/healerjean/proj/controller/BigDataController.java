package com.healerjean.proj.controller;

import com.google.common.collect.Lists;
import com.healerjean.proj.common.anno.LogIndex;
import com.healerjean.proj.common.data.bo.BaseRes;
import com.healerjean.proj.data.bo.UserDemoBO;
import com.healerjean.proj.data.bo.UserDemoQueryBO;
import com.healerjean.proj.data.convert.UserConverter;
import com.healerjean.proj.data.req.UserDemoQueryReq;
import com.healerjean.proj.data.vo.UserDemoVO;
import com.healerjean.proj.service.BidDataService;
import com.healerjean.proj.utils.ThreadPoolUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

/**
 * BigDataController
 *
 * @author zhangyujin
 * @date 2023/12/13
 */
@RestController
@RequestMapping("hlj")
@Api(tags = "BigDataController")
@Slf4j
public class BigDataController {

    /**
     * bidDataService
     */
    @Resource
    private BidDataService bidDataService;


    @ApiOperation("大数据量-分页查询全部")
    @LogIndex(resFlag = false)
    @GetMapping("user/queryAllUserDemoByLimit")
    @ResponseBody
    public BaseRes<List<UserDemoVO>> queryAllUserDemoByLimit(UserDemoQueryReq req) {
        UserDemoQueryBO userDemoPageQuery = UserConverter.INSTANCE.covertUserDemoQueryReqToBo(req);
        List<UserDemoBO> list = bidDataService.queryAllUserDemoByLimit(userDemoPageQuery);
        List<UserDemoVO> userDemoVos = UserConverter.INSTANCE.covertUserDemoBoToVoList(list);
        return BaseRes.buildSuccess(userDemoVos);
    }

    @ApiOperation("大数据量-IdSize查询全部")
    @LogIndex(resFlag = false)
    @GetMapping("user/queryAllUserDemoByIdSize")
    @ResponseBody
    public BaseRes<List<UserDemoVO>> queryAllUserDemoByIdSize(UserDemoQueryReq req) {
        UserDemoQueryBO userDemoPageQuery = UserConverter.INSTANCE.covertUserDemoQueryReqToBo(req);
        List<UserDemoBO> list = bidDataService.queryAllUserDemoByIdSize(userDemoPageQuery);
        List<UserDemoVO> userDemoVos = UserConverter.INSTANCE.covertUserDemoBoToVoList(list);
        return BaseRes.buildSuccess(userDemoVos);
    }

    @ApiOperation("大数据量-Id区间查询全部")
    @LogIndex(resFlag = false)
    @GetMapping("user/queryAllUserDemoByIdSub")
    @ResponseBody
    public BaseRes<List<UserDemoVO>> queryAllUserDemoByIdSub(UserDemoQueryReq req) {
        UserDemoQueryBO userDemoPageQuery = UserConverter.INSTANCE.covertUserDemoQueryReqToBo(req);
        List<UserDemoBO> list = bidDataService.queryAllUserDemoByIdSub(userDemoPageQuery);
        List<UserDemoVO> userDemoVos = UserConverter.INSTANCE.covertUserDemoBoToVoList(list);
        return BaseRes.buildSuccess(userDemoVos);
    }


    @ApiOperation("大数据量-线程池根据Id区间查询")
    @LogIndex(resFlag = false)
    @GetMapping("user/queryAllUserDemoByPoolIdSub")
    @ResponseBody
    public BaseRes<List<UserDemoVO>> queryAllUserDemoByPoolIdSub() {
        CompletionService<List<UserDemoBO>> completionService = new ExecutorCompletionService<>(ThreadPoolUtils.DEFAULT_THREAD_POOL_TASK_EXECUTOR);
        UserDemoQueryBO queryBO = new UserDemoQueryBO();
        List<Future<List<UserDemoBO>>> futures = bidDataService.queryAllUserDemoByPoolIdSub(completionService, queryBO);
        List<UserDemoBO> bos = Lists.newArrayList();
        for (int i = 0; i < futures.size(); i++) {
            try {
                Future<List<UserDemoBO>> future = completionService.take();
                List<UserDemoBO> userDemos = future.get();
                if (CollectionUtils.isEmpty(userDemos)) {
                    continue;
                }
                bos.addAll(future.get());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        List<UserDemoVO> userDemoVos = UserConverter.INSTANCE.covertUserDemoBoToVoList(bos);
        return BaseRes.buildSuccess(userDemoVos);
    }

    @ApiOperation("大数据量-线程池limit查询")
    @LogIndex(resFlag = false)
    @GetMapping("user/queryAllUserDemoByPoolLimit")
    @ResponseBody
    public BaseRes<List<UserDemoVO>> queryAllUserDemoByPoolLimit() {
        CompletionService<List<UserDemoBO>> completionService = new ExecutorCompletionService<>(ThreadPoolUtils.DEFAULT_THREAD_POOL_TASK_EXECUTOR);
        UserDemoQueryBO queryBO = new UserDemoQueryBO();
        List<Future<List<UserDemoBO>>> futures = bidDataService.queryAllUserDemoByPoolLimit(completionService, queryBO);
        List<UserDemoBO> all = Lists.newArrayList();
        for (int i = 0; i < futures.size(); i++) {
            try {
                Future<List<UserDemoBO>> future = completionService.take();
                List<UserDemoBO> userDemos = future.get();
                if (CollectionUtils.isEmpty(userDemos)) {
                    continue;
                }
                all.addAll(future.get());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        List<UserDemoVO> userDemoVos = UserConverter.INSTANCE.covertUserDemoBoToVoList(all);
        return BaseRes.buildSuccess(userDemoVos);
    }


    @ApiOperation("大数据量-分页缓存全部")
    @LogIndex(resFlag = false)
    @GetMapping("user/bigKeyCache")
    @ResponseBody
    public BaseRes<Long> bigKeyCache(UserDemoQueryReq req) {
        UserDemoQueryBO userDemoPageQuery = UserConverter.INSTANCE.covertUserDemoQueryReqToBo(req);
        return BaseRes.buildSuccess(bidDataService.bigKeyCache(userDemoPageQuery));
    }




}
