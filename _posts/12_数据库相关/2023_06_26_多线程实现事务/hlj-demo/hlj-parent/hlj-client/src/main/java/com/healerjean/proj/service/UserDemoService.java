package com.healerjean.proj.service;

import com.healerjean.proj.common.data.bo.PageBO;
import com.healerjean.proj.common.data.bo.PageQueryBO;
import com.healerjean.proj.data.bo.UserDemoBO;
import com.healerjean.proj.data.bo.UserDemoQueryBO;

import java.util.List;

/**
 * UserService
 *
 * @author zhangyujin
 * @date 2023/6/14  10:56.
 */
public interface UserDemoService {

    /**
     * userDemoBo
     *
     * @param userDemoBo userDemoBo
     * @return boolean
     */
    boolean saveUserDemo(UserDemoBO userDemoBo);

    /**
     * deleteUserDemo
     *
     * @param id id
     * @return boolean
     */
    boolean deleteUserDemo(Long id);


    /**
     * updateUserDemo
     *
     * @param userDemoBo userDemoBo
     * @return boolean
     */
    boolean updateUserDemo(UserDemoBO userDemoBo);

    /**
     * selectById
     *
     * @param id id
     * @return UserDemoBO
     */
    UserDemoBO selectById(Long id);

    /**
     * queryUserDemoList
     *
     * @param query query
     * @return List<UserDemoBO>
     */
    List<UserDemoBO> queryUserDemoList(UserDemoQueryBO query);


    /**
     * queryUserDemoPage
     *
     * @param pageQuery pageQuery
     * @return PageBO<UserDemoBO>
     */
    PageBO<UserDemoBO> queryUserDemoPage(PageQueryBO<UserDemoQueryBO> pageQuery);
}
