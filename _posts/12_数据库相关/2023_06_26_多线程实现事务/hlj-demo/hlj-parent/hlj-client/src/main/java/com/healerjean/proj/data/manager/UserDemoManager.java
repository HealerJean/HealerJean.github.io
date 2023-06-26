package com.healerjean.proj.data.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.healerjean.proj.common.data.bo.PageBO;
import com.healerjean.proj.common.data.bo.PageQueryBO;
import com.healerjean.proj.data.bo.UserDemoBO;
import com.healerjean.proj.data.bo.UserDemoQueryBO;
import com.healerjean.proj.data.po.UserDemo;

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
}
