package com.healerjean.proj.data.manager.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.healerjean.proj.common.data.bo.PageQueryBO;
import com.healerjean.proj.data.bo.UserDemoQueryBO;
import com.healerjean.proj.data.dao.UserDemoDao;
import com.healerjean.proj.data.manager.UserDemoManager;
import com.healerjean.proj.data.po.UserDemo;
import com.healerjean.proj.utils.db.IdQueryBO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * UserDemoManagerImpl
 *
 * @author zhangyujin
 * @date 2023/6/14  10:55.
 */
@Slf4j
@Service
public class UserDemoManagerImpl implements UserDemoManager {

    /**
     * userDemoDao
     */
    @Resource
    private UserDemoDao userDemoDao;

    /**
     * saveUserDemo
     *
     * @param userDemo userDemo
     * @return boolean
     */
    @Override
    public boolean saveUserDemo(UserDemo userDemo) {
        return userDemoDao.save(userDemo);
    }

    /**
     * deleteUserDemo
     *
     * @param userDemo userDemo
     * @return boolean
     */
    @Override
    public boolean deleteUserDemo(UserDemo userDemo) {
        LambdaUpdateWrapper<UserDemo> queryWrapper = Wrappers.lambdaUpdate(UserDemo.class)
                .eq(UserDemo::getId, userDemo.getId())

                .set(UserDemo::getValidFlag, userDemo.getValidFlag());
        return userDemoDao.update(queryWrapper);
    }

    /**
     * updateUserDemo
     *
     * @param userDemo userDemo
     * @return boolean
     */
    @Override
    public boolean updateUserDemo(UserDemo userDemo) {
        Wrapper<UserDemo> updateWrapper = Wrappers.lambdaUpdate(UserDemo.class)
                .eq(Objects.nonNull(userDemo.getId()), UserDemo::getId, userDemo.getId())
                .eq(Objects.nonNull(userDemo.getValidFlag()), UserDemo::getValidFlag, userDemo.getValidFlag())

                .set(StringUtils.isNotBlank(userDemo.getName()), UserDemo::getName, userDemo.getName())
                .set(StringUtils.isNotBlank(userDemo.getPhone()), UserDemo::getPhone, userDemo.getPhone())
                .set(StringUtils.isNotBlank(userDemo.getEmail()), UserDemo::getEmail, userDemo.getEmail())
                .set(Objects.nonNull(userDemo.getAge()), UserDemo::getAge, userDemo.getAge())
                .set(Objects.nonNull(userDemo.getStartTime()), UserDemo::getStartTime, userDemo.getStartTime())
                .set(Objects.nonNull(userDemo.getEndTime()), UserDemo::getEndTime, userDemo.getEndTime());
        return userDemoDao.update(updateWrapper);
    }


    /**
     * selectById
     *
     * @param id id
     * @return UserDemoBO
     */
    @Override
    public UserDemo selectById(Long id) {
        return userDemoDao.getById(id);
    }

    /**
     * queryUserDemoList
     *
     * @param query query
     * @return List<UserDemoBO>
     */
    @Override
    public List<UserDemo> queryUserDemoList(UserDemoQueryBO query) {
        QueryWrapper<UserDemo> queryWrapper = new QueryWrapper<>();
        if (!CollectionUtils.isEmpty(query.getSelectFields())) {
            queryWrapper.select(StringUtils.join(query.getSelectFields(), ","));
        }
        if (!CollectionUtils.isEmpty(query.getOrderByList())) {
            query.getOrderByList().forEach(item -> queryWrapper.orderBy(Boolean.TRUE, item.getDirection(), item.getProperty()));
        }

        LambdaQueryWrapper<UserDemo> lambdaQueryWrapper = queryWrapper.lambda()
                .eq(Objects.nonNull(query.getId()), UserDemo::getId, query.getId())
                .eq(Objects.nonNull(query.getValidFlag()), UserDemo::getValidFlag, query.getValidFlag())
                .eq(StringUtils.isNotBlank(query.getName()), UserDemo::getName, query.getName())
                .eq(StringUtils.isNotBlank(query.getPhone()), UserDemo::getPhone, query.getPhone())
                .eq(StringUtils.isNotBlank(query.getEmail()), UserDemo::getEmail, query.getEmail())
                .like(StringUtils.isNotBlank(query.getLikeName()), UserDemo::getName, query.getLikeName())
                .like(StringUtils.isNotBlank(query.getLikePhone()), UserDemo::getPhone, query.getLikePhone())
                .lt(Objects.nonNull(query.getQueryTime()), UserDemo::getStartTime, query.getQueryTime())
                .gt(Objects.nonNull(query.getQueryTime()), UserDemo::getEndTime, query.getQueryTime());

        return userDemoDao.list(lambdaQueryWrapper);
    }

    /**
     * queryUserDemoPage
     *
     * @param pageQuery pageQuery
     * @return PageBO<UserDemoBO>
     */
    @Override
    public Page<UserDemo> queryUserDemoPage(PageQueryBO<UserDemoQueryBO> pageQuery) {
        UserDemoQueryBO query = pageQuery.getData();
        QueryWrapper<UserDemo> queryWrapper = new QueryWrapper<>();
        if (!CollectionUtils.isEmpty(query.getSelectFields())) {
            queryWrapper.select(StringUtils.join(query.getSelectFields(), ","));
        }

        if (!CollectionUtils.isEmpty(query.getOrderByList())) {
            query.getOrderByList().forEach(item -> queryWrapper.orderBy(Boolean.TRUE, item.getDirection(), item.getProperty()));
        }
        LambdaQueryWrapper<UserDemo> lambdaQueryWrapper = queryWrapper.lambda()
                .eq(Objects.nonNull(query.getId()), UserDemo::getId, query.getId())
                .eq(Objects.nonNull(query.getValidFlag()), UserDemo::getValidFlag, query.getValidFlag())
                .eq(StringUtils.isNotBlank(query.getName()), UserDemo::getName, query.getName())
                .eq(StringUtils.isNotBlank(query.getPhone()), UserDemo::getPhone, query.getPhone())
                .eq(StringUtils.isNotBlank(query.getEmail()), UserDemo::getEmail, query.getEmail())

                .like(StringUtils.isNotBlank(query.getLikeName()), UserDemo::getName, query.getLikeName())
                .like(StringUtils.isNotBlank(query.getLikePhone()), UserDemo::getPhone, query.getLikePhone())
                .lt(Objects.nonNull(query.getQueryTime()), UserDemo::getStartTime, query.getQueryTime())
                .gt(Objects.nonNull(query.getQueryTime()), UserDemo::getEndTime, query.getQueryTime());

        Page<UserDemo> page = new Page<>(pageQuery.getCurrPage(), pageQuery.getPageSize(), pageQuery.getSearchCountFlag());
        return userDemoDao.page(page, lambdaQueryWrapper);
    }


    /**
     * 获取查询条件
     *
     * @param query query
     * @return LambdaQueryWrapper<UserDemo>
     */
    @Override
    public QueryWrapper<UserDemo> queryBuilderQueryWrapper(UserDemoQueryBO query) {
        QueryWrapper<UserDemo> queryWrapper = new QueryWrapper<>();
        if (!CollectionUtils.isEmpty(query.getSelectFields())) {
            queryWrapper.select(StringUtils.join(query.getSelectFields(), ","));
        }
        if (!CollectionUtils.isEmpty(query.getOrderByList())) {
            query.getOrderByList().forEach(item -> queryWrapper.orderBy(Boolean.TRUE, item.getDirection(), item.getProperty()));
        }
         queryWrapper.lambda()
                .eq(Objects.nonNull(query.getId()), UserDemo::getId, query.getId())
                .eq(Objects.nonNull(query.getValidFlag()), UserDemo::getValidFlag, query.getValidFlag())
                .eq(StringUtils.isNotBlank(query.getName()), UserDemo::getName, query.getName())
                .eq(StringUtils.isNotBlank(query.getPhone()), UserDemo::getPhone, query.getPhone())
                .eq(StringUtils.isNotBlank(query.getEmail()), UserDemo::getEmail, query.getEmail())
                .like(StringUtils.isNotBlank(query.getLikeName()), UserDemo::getName, query.getLikeName())
                .like(StringUtils.isNotBlank(query.getLikePhone()), UserDemo::getPhone, query.getLikePhone())
                .lt(Objects.nonNull(query.getQueryTime()), UserDemo::getStartTime, query.getQueryTime())
                .gt(Objects.nonNull(query.getQueryTime()), UserDemo::getEndTime, query.getQueryTime())
                .lt(Objects.nonNull(query.getQueryTime()), UserDemo::getStartTime, query.getQueryTime())
                .gt(Objects.nonNull(query.getQueryTime()), UserDemo::getEndTime, query.getQueryTime());
        return queryWrapper;
    }


    /**
     * 根据查询条件获取最大Id和最小Id
     *
     * @param query query
     * @return ImmutablePair<Long, Long>
     */
    @Override
    public ImmutablePair<Long, Long> queryMinAndMaxId(UserDemoQueryBO query) {
        QueryWrapper<UserDemo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("min(id) as \"minId\"", "max(id) as \"maxId\"");
        LambdaQueryWrapper<UserDemo> lambdaQueryWrapper = queryWrapper.lambda()
                .eq(Objects.nonNull(query.getId()), UserDemo::getId, query.getId())
                .eq(Objects.nonNull(query.getValidFlag()), UserDemo::getValidFlag, query.getValidFlag())
                .like(StringUtils.isNotBlank(query.getName()), UserDemo::getName, query.getName())
                .like(StringUtils.isNotBlank(query.getPhone()), UserDemo::getPhone, query.getPhone())
                .like(StringUtils.isNotBlank(query.getEmail()), UserDemo::getEmail, query.getEmail())
                .like(StringUtils.isNotBlank(query.getLikeName()), UserDemo::getName, query.getLikeName())
                .like(StringUtils.isNotBlank(query.getLikePhone()), UserDemo::getPhone, query.getLikePhone())
                .le(Objects.nonNull(query.getQueryTime()), UserDemo::getStartTime, query.getQueryTime())
                .ge(Objects.nonNull(query.getQueryTime()), UserDemo::getEndTime, query.getQueryTime());
        Map<String, Object> map = userDemoDao.getMap(lambdaQueryWrapper);
        return ImmutablePair.of(MapUtils.getLong(map, "minId"), MapUtils.getLong(map, "maxId"));
    }


    /**
     * 根据id区间查询数据
     *
     * @param idQueryBO idQueryBO
     * @return {@link List< UserDemo>}
     */
    @Override
    public List<UserDemo> queryUserDemoByIdSize(IdQueryBO idQueryBO, UserDemoQueryBO query) {
        QueryWrapper<UserDemo> queryWrapper = new QueryWrapper<>();
        if (!CollectionUtils.isEmpty(query.getSelectFields())) {
            queryWrapper.select(StringUtils.join(query.getSelectFields(), ","));
        }
        LambdaQueryWrapper<UserDemo> lambdaQueryWrapper = queryWrapper.lambda()
                .eq(Objects.nonNull(query.getId()), UserDemo::getId, query.getId())
                .eq(Objects.nonNull(query.getValidFlag()), UserDemo::getValidFlag, query.getValidFlag())
                .eq(StringUtils.isNotBlank(query.getName()), UserDemo::getName, query.getName())
                .eq(StringUtils.isNotBlank(query.getPhone()), UserDemo::getPhone, query.getPhone())
                .eq(StringUtils.isNotBlank(query.getEmail()), UserDemo::getEmail, query.getEmail())
                .like(StringUtils.isNotBlank(query.getLikeName()), UserDemo::getName, query.getLikeName())
                .like(StringUtils.isNotBlank(query.getLikePhone()), UserDemo::getPhone, query.getLikePhone())
                .lt(Objects.nonNull(query.getQueryTime()), UserDemo::getStartTime, query.getQueryTime())
                .gt(Objects.nonNull(query.getQueryTime()), UserDemo::getEndTime, query.getQueryTime())
                .gt(UserDemo::getId, idQueryBO.getMinId())
                .orderByAsc(UserDemo::getId)
                .last( "limit " + idQueryBO.getSize());

        List<UserDemo> list = userDemoDao.list(lambdaQueryWrapper);
        if (CollectionUtils.isEmpty(list)){
            idQueryBO.setMaxId(null);
            return null;
        }
        idQueryBO.setMaxId(list.get(list.size()-1).getId());
        return list;
    }

    /**
     * 根据id区间查询数据
     *
     * @param idQueryBO idQueryBO
     * @return {@link List< UserDemo>}
     */
    @Override
    public List<UserDemo> queryUserDemoByIdSub(IdQueryBO idQueryBO, UserDemoQueryBO query) {
        QueryWrapper<UserDemo> queryWrapper = new QueryWrapper<>();
        if (!CollectionUtils.isEmpty(query.getSelectFields())) {
            queryWrapper.select(StringUtils.join(query.getSelectFields(), ","));
        }
        LambdaQueryWrapper<UserDemo> lambdaQueryWrapper = queryWrapper.lambda()
                .eq(Objects.nonNull(query.getId()), UserDemo::getId, query.getId())
                .eq(Objects.nonNull(query.getValidFlag()), UserDemo::getValidFlag, query.getValidFlag())
                .eq(StringUtils.isNotBlank(query.getName()), UserDemo::getName, query.getName())
                .eq(StringUtils.isNotBlank(query.getPhone()), UserDemo::getPhone, query.getPhone())
                .eq(StringUtils.isNotBlank(query.getEmail()), UserDemo::getEmail, query.getEmail())
                .like(StringUtils.isNotBlank(query.getLikeName()), UserDemo::getName, query.getLikeName())
                .like(StringUtils.isNotBlank(query.getLikePhone()), UserDemo::getPhone, query.getLikePhone())
                .lt(Objects.nonNull(query.getQueryTime()), UserDemo::getStartTime, query.getQueryTime())
                .gt(Objects.nonNull(query.getQueryTime()), UserDemo::getEndTime, query.getQueryTime());

        if (idQueryBO.getMinEqualFlag()) {
            lambdaQueryWrapper.ge(UserDemo::getId, idQueryBO.getMinId());
        } else {
            lambdaQueryWrapper.gt(UserDemo::getId, idQueryBO.getMinId());
        }

        if (idQueryBO.getMaxEqualFlag()) {
            lambdaQueryWrapper.le(UserDemo::getId, idQueryBO.getMaxId());
        } else {
            lambdaQueryWrapper.lt(UserDemo::getId, idQueryBO.getMaxId());
        }
        return userDemoDao.list(lambdaQueryWrapper);
    }



}
