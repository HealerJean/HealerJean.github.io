package com.healerjean.proj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.healerjean.proj.common.data.bo.PageBO;
import com.healerjean.proj.common.data.bo.PageQueryBO;
import com.healerjean.proj.common.data.convert.PageConverter;
import com.healerjean.proj.common.enums.SystemEnum;
import com.healerjean.proj.data.bo.UserDemoBO;
import com.healerjean.proj.data.bo.UserDemoQueryBO;
import com.healerjean.proj.data.convert.UserConverter;
import com.healerjean.proj.data.dao.UserDemoDao;
import com.healerjean.proj.data.excel.UserDemoExcel;
import com.healerjean.proj.data.manager.UserDemoManager;
import com.healerjean.proj.data.po.UserDemo;
import com.healerjean.proj.service.UserDemoService;
import com.healerjean.proj.utils.db.IdQueryBO;
import com.healerjean.proj.utils.db.MybatisBatchUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Future;

/**
 * UserService
 *
 * @author zhangyujin
 * @date 2023/6/14  14:52.
 */
@Slf4j
@Service
public class UserDemoServiceImpl implements UserDemoService {

    /**
     * userDemoManager
     */
    @Resource
    private UserDemoManager userDemoManager;

    /**
     * userDemoDao
     */
    @Resource
    private UserDemoDao userDemoDao;

    /**
     * sqlSessionFactory
     */
    @Resource
    private SqlSessionFactory sqlSessionFactory;


    /**
     * userDemoBo
     *
     * @param userDemoBo userDemoBo
     * @return boolean
     */
    @Override
    public boolean saveUserDemo(UserDemoBO userDemoBo) {
        UserDemo userDemo = UserConverter.INSTANCE.covertUserDemoBoToPo(userDemoBo);
        return userDemoManager.saveUserDemo(userDemo);
    }

    /**
     * deleteUserDemo
     *
     * @param id id
     * @return boolean
     */
    @Override
    public boolean deleteUserDemo(Long id) {
        UserDemo userDemo = new UserDemo();
        userDemo.setId(id);
        userDemo.setValidFlag(SystemEnum.StatusEnum.VALID.getCode());
        return userDemoManager.deleteUserDemo(userDemo);
    }


    /**
     * updateUserDemo
     *
     * @param userDemoBo userDemoBo
     * @return boolean
     */
    @Override
    public boolean updateUserDemo(UserDemoBO userDemoBo) {
        UserDemo userDemo = UserConverter.INSTANCE.covertUserDemoBoToPo(userDemoBo);
        return userDemoManager.updateUserDemo(userDemo);
    }


    /**
     * selectById
     *
     * @param id id
     * @return UserDemoBO
     */
    @Override
    public UserDemoBO selectById(Long id) {
        UserDemo userDemo = userDemoManager.selectById(id);
        return UserConverter.INSTANCE.covertUserDemoPoToBo(userDemo);
    }

    /**
     * queryUserDemoList
     *
     * @param query query
     * @return List<UserDemoBO>
     */
    @Override
    public List<UserDemoBO> queryUserDemoList(UserDemoQueryBO query) {
        List<UserDemo> userDemos = userDemoManager.queryUserDemoList(query);
        return UserConverter.INSTANCE.covertUserDemoPoToBoList(userDemos);
    }

    /**
     * queryUserDemoPage
     *
     * @param pageQuery pageQuery
     * @return PageBO<UserDemoBO>
     */
    @Override
    public PageBO<UserDemoBO> queryUserDemoPage(PageQueryBO<UserDemoQueryBO> pageQuery) {
        Page<UserDemo> page = userDemoManager.queryUserDemoPage(pageQuery);
        List<UserDemoBO> userDemoBos = UserConverter.INSTANCE.covertUserDemoPoToBoList(page.getRecords());
        return PageConverter.INSTANCE.covertPageBoToBo(page, userDemoBos);
    }

    /**
     * queryFutureAll
     *
     * @param completionService completionService
     * @param query             query
     * @return List<Future < List < UserDemoExcel>>>
     */
    @Override
    public List<Future<List<UserDemoExcel>>> queryAllUserDemoByPoolLimit(CompletionService<List<UserDemoExcel>> completionService, UserDemoQueryBO query) {
        QueryWrapper<UserDemo> queryWrapper = userDemoManager.queryBuilderQueryWrapper(query);
        return MybatisBatchUtils.queryAllByPoolLimit(completionService,
                (p, q) -> userDemoDao.page(p, q),
                queryWrapper,
                1,
                UserConverter.INSTANCE::covertUserDemoPoToExcelList);
    }


    /**
     * 根据
     *
     * @param completionService completionService
     * @param query             queryBO
     * @return List<Future < List < UserDemoExcel>>>
     */
    @Override
    public List<Future<List<UserDemoExcel>>> queryAllUserDemoByPoolIdSub(CompletionService<List<UserDemoExcel>> completionService, UserDemoQueryBO query) {
        ImmutablePair<Long, Long> minAndMaxId = userDemoManager.queryMinAndMaxId(query);
        Long minId = minAndMaxId.getLeft();
        Long maxId = minAndMaxId.getRight();
        IdQueryBO idQueryBO = new IdQueryBO(minId, maxId, 2L);
        return MybatisBatchUtils.queryAllByPoolIdSub(completionService,
                (p, q) -> userDemoManager.queryUserDemoByIdSub(p, q),
                query,
                idQueryBO,
                UserConverter.INSTANCE::covertUserDemoPoToExcelList);
    }



    /**
     * 大数据量-分页查询全部
     *
     * @param queryBo queryBo
     * @return List<UserDemoBO>
     */
    @Override
    public List<UserDemoBO> queryAllUserDemoByLimit(UserDemoQueryBO queryBo) {
        QueryWrapper<UserDemo> queryWrapper = userDemoManager.queryBuilderQueryWrapper(queryBo);
        List<UserDemo> list = MybatisBatchUtils.queryAllByLimit((p, q) -> userDemoDao.page(p, q), queryWrapper, 1000L);
        return UserConverter.INSTANCE.covertUserDemoPoToBoList(list);
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
        List<UserDemo> list = MybatisBatchUtils.queryAllByIdSize(
                (p, q) -> userDemoManager.queryUserDemoByIdSize(p, q),
                queryBo,
                idQueryBO);
        return UserConverter.INSTANCE.covertUserDemoPoToBoList(list);
    }
}

