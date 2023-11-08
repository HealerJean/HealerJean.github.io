package com.healerjean.proj.data.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.healerjean.proj.common.data.bo.PageQueryBO;
import com.healerjean.proj.data.bo.UserDemoQueryBO;
import com.healerjean.proj.data.po.UserDemo;
import com.healerjean.proj.utils.db.IdQueryBO;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.List;

/**
 * UserDemoManager
 *
 * @author zhangyujin
 * @date 2023/6/14  10:55.
 */
public interface UserDemoManager {


    /**
     * saveUserDemo
     *
     * @param userDemo userDemo
     * @return boolean
     */
    boolean saveUserDemo(UserDemo userDemo);

    /**
     * deleteUserDemo
     *
     * @param userDemo userDemo
     * @return boolean
     */
    boolean deleteUserDemo(UserDemo userDemo);

    /**
     * updateUserDemo
     *
     * @param userDemo userDemo
     * @return boolean
     */
    boolean updateUserDemo(UserDemo userDemo);

    /**
     * selectById
     *
     * @param id id
     * @return UserDemoBO
     */
    UserDemo selectById(Long id);

    /**
     * queryUserDemoList
     *
     * @param query query
     * @return List<UserDemo>
     */
    List<UserDemo> queryUserDemoList(UserDemoQueryBO query);

    /**
     * queryUserDemoPage
     *
     * @param pageQuery pageQuery
     * @return PageBO<UserDemo>
     */
    Page<UserDemo> queryUserDemoPage(PageQueryBO<UserDemoQueryBO> pageQuery);

    /**
     * queryFutureAll
     *
     * @param query query
     * @return List<Future < List < UserDemo>>>
     */
    QueryWrapper<UserDemo> queryBuilderQueryWrapper(UserDemoQueryBO query);


    /**
     * 查询条件内最小的id和最大的id
     *
     * @param query query
     * @return List<Long>
     */
    ImmutablePair<Long, Long> queryMinAndMaxId(UserDemoQueryBO query);

    /**
     * 根据Id区间查询所有记录
     *
     * @param idQueryBO       minMaxBO
     * @param userDemoQueryBO userDemoQueryBO
     * @return List<UserDemo>
     */
    List<UserDemo> queryUserDemoByIdSub(IdQueryBO idQueryBO, UserDemoQueryBO userDemoQueryBO);

    /**
     * 根据Id区间查询所有记录
     *
     * @param idQueryBO       minMaxBO
     * @param userDemoQueryBO userDemoQueryBO
     * @return List<UserDemo>
     */
    List<UserDemo> queryUserDemoByIdSize(IdQueryBO idQueryBO, UserDemoQueryBO userDemoQueryBO);



}
