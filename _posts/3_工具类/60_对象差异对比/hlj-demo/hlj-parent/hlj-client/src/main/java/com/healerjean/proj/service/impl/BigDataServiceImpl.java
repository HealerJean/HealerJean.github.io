package com.healerjean.proj.service.impl;

import com.healerjean.proj.data.bo.UserDemoBO;
import com.healerjean.proj.data.bo.UserDemoQueryBO;
import com.healerjean.proj.data.convert.UserConverter;
import com.healerjean.proj.data.manager.UserDemoManager;
import com.healerjean.proj.data.po.UserDemo;
import com.healerjean.proj.service.BidDataService;
import com.healerjean.proj.utils.db.IdQueryBO;
import com.healerjean.proj.utils.db.BatchQueryUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Future;

/**
 * BigDataServiceImpl
 *
 * @author zhangyujin
 * @date 2023/12/13
 */
@Slf4j
@Service
public class BigDataServiceImpl implements BidDataService {

    /**
     * userDemoManager
     */
    @Resource
    private UserDemoManager userDemoManager;

    /**
     * 大数据量-分页查询全部
     *
     * @param queryBo queryBo
     * @return List<UserDemoBO>
     */
    @Override
    public List<UserDemoBO> queryAllUserDemoByLimit(UserDemoQueryBO queryBo) {
        return BatchQueryUtils.queryAllByLimit(p -> userDemoManager.queryUserDemoPage(p), queryBo, 1000L);
    }

    /**
     * 大数据量-IdSize查询全部
     *
     * @param queryBo queryBo
     * @return List<UserDemoBO>
     */
    @Override
    public List<UserDemoBO> queryAllUserDemoByIdSize(UserDemoQueryBO queryBo) {
        IdQueryBO idQueryBO = new IdQueryBO(0L, 2L);
        List<UserDemo> list = BatchQueryUtils.queryAllByIdSize(
                (p, q) -> userDemoManager.queryUserDemoByIdSize(p, q),
                queryBo,
                idQueryBO);
        return UserConverter.INSTANCE.covertUserDemoPoToBoList(list);
    }

    /**
     * 大数据量-Id区间查询全部
     *
     * @param queryBo queryBo
     * @return List<UserDemoBO>
     */
    @Override
    public List<UserDemoBO> queryAllUserDemoByIdSub(UserDemoQueryBO queryBo) {
        ImmutablePair<Long, Long> minAndMaxId = userDemoManager.queryMinAndMaxId(queryBo);
        Long minId = minAndMaxId.getLeft();
        Long maxId = minAndMaxId.getRight();
        IdQueryBO idQueryBO = new IdQueryBO(minId, maxId, 2L);
        List<UserDemo> list = BatchQueryUtils.queryAllByIdSub(
                (p, q) -> userDemoManager.queryUserDemoByIdSub(p, q),
                queryBo,
                idQueryBO);
        return UserConverter.INSTANCE.covertUserDemoPoToBoList(list);
    }



    /**
     * 大数据量-线程池limit查询
     *
     * @param completionService completionService
     * @param query             query
     * @return List<Future < List < UserDemoExcel>>>
     */
    @Override
    public List<Future<List<UserDemoBO>>> queryAllUserDemoByPoolLimit(CompletionService<List<UserDemoBO>> completionService, UserDemoQueryBO query) {
        return BatchQueryUtils.queryAllByPoolLimit(completionService, p -> userDemoManager.queryUserDemoPage(p), query, 1);
    }


    /**
     * 大数据量-线程池根据Id区间查询
     *
     * @param completionService completionService
     * @param query             queryBO
     * @return List<Future < List < UserDemoExcel>>>
     */
    @Override
    public List<Future<List<UserDemoBO>>> queryAllUserDemoByPoolIdSub(CompletionService<List<UserDemoBO>> completionService, UserDemoQueryBO query) {
        ImmutablePair<Long, Long> minAndMaxId = userDemoManager.queryMinAndMaxId(query);
        Long minId = minAndMaxId.getLeft();
        Long maxId = minAndMaxId.getRight();
        IdQueryBO idQueryBO = new IdQueryBO(minId, maxId, 2L);
        return BatchQueryUtils.queryAllByPoolIdSub(completionService,
                (p, q) -> userDemoManager.queryUserDemoByIdSub(p, q),
                query,
                idQueryBO,
                UserConverter.INSTANCE::covertUserDemoPoToBoList);
    }

}
