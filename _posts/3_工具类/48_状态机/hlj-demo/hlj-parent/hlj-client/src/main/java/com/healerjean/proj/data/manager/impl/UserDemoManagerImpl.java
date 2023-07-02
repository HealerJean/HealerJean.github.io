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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
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


    @Override
    public LambdaQueryWrapper<UserDemo> queryBuilderQueryWrapper(UserDemoQueryBO query) {
        QueryWrapper<UserDemo> queryWrapper = new QueryWrapper<>();
        if (!CollectionUtils.isEmpty(query.getSelectFields())) {
            queryWrapper.select(StringUtils.join(query.getSelectFields(), ","));
        }
        if (!CollectionUtils.isEmpty(query.getOrderByList())) {
            query.getOrderByList().forEach(item -> queryWrapper.orderBy(Boolean.TRUE, item.getDirection(), item.getProperty()));
        }
        return queryWrapper.lambda()
                .eq(Objects.nonNull(query.getId()), UserDemo::getId, query.getId())
                .eq(Objects.nonNull(query.getValidFlag()), UserDemo::getValidFlag, query.getValidFlag())
                .eq(StringUtils.isNotBlank(query.getName()), UserDemo::getName, query.getName())
                .eq(StringUtils.isNotBlank(query.getPhone()), UserDemo::getPhone, query.getPhone())
                .eq(StringUtils.isNotBlank(query.getEmail()), UserDemo::getEmail, query.getEmail())
                .like(StringUtils.isNotBlank(query.getLikeName()), UserDemo::getName, query.getLikeName())
                .like(StringUtils.isNotBlank(query.getLikePhone()), UserDemo::getPhone, query.getLikePhone())
                .lt(Objects.nonNull(query.getQueryTime()), UserDemo::getStartTime, query.getQueryTime())
                .gt(Objects.nonNull(query.getQueryTime()), UserDemo::getEndTime, query.getQueryTime());
    }
}
