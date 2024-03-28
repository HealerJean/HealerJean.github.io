package com.healerjean.proj.service;

import com.healerjean.proj.data.bo.UserDemoBO;
import com.healerjean.proj.data.bo.UserDemoQueryBO;

import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Future;

/**
 * BidDataService
 *
 * @author zhangyujin
 * @date 2023/12/13
 */
public interface BidDataService {


    /**
     * 大数据量-线程池limit查询
     *
     * @param query query
     * @return List<Future < List < UserDemoBO>>>
     */
    List<Future<List<UserDemoBO>>> queryAllUserDemoByPoolLimit(CompletionService<List<UserDemoBO>> completionService, UserDemoQueryBO query);

    /**
     * 大数据量-线程池Id区间查询
     *
     * @param queryBO queryBO
     * @return List<UserDemoExcel>
     */
    List<Future<List<UserDemoBO>>> queryAllUserDemoByPoolIdSub(CompletionService<List<UserDemoBO>> completionService, UserDemoQueryBO queryBO);

    /**
     * 大数据量-分页查询全部
     *
     * @param queryBo queryBo
     * @return List<UserDemoBO>
     */
    List<UserDemoBO> queryAllUserDemoByLimit(UserDemoQueryBO queryBo);

    /**
     * 大数据量-IdSize查询全部
     *
     * @param queryBo queryBo
     * @return List<UserDemoBO>
     */
    List<UserDemoBO> queryAllUserDemoByIdSize(UserDemoQueryBO queryBo);

    /**
     * 大数据量-Id区间查询全部
     *
     * @param queryBo queryBo
     * @return List<UserDemoBO>
     */
    List<UserDemoBO> queryAllUserDemoByIdSub(UserDemoQueryBO queryBo);

    /**
     * 大key缓存
     *
     * @param queryBO queryBO
     */
    Long bigKeyCache(UserDemoQueryBO queryBO);
}
