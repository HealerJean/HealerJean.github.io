package com.healerjean.proj.service;

import com.healerjean.proj.common.data.bo.PageBO;
import com.healerjean.proj.common.data.bo.PageQueryBO;
import com.healerjean.proj.data.bo.UserDemoBO;
import com.healerjean.proj.data.bo.UserDemoQueryBO;
import com.healerjean.proj.data.excel.UserDemoExcel;

import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Future;

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


    /**
     * 线程池查询所有数据
     *
     * @param query query
     * @return List<Future < List < UserDemoBO>>>
     */
    List<Future<List<UserDemoExcel>>> queryAllUserDemoByPoolLimit(CompletionService<List<UserDemoExcel>> completionService, UserDemoQueryBO query);

    /**
     * 根据id区间分页查询所有数据
     * @param queryBO queryBO
     * @return List<UserDemoExcel>
     */
    List<Future<List<UserDemoExcel>>> queryAllUserDemoByPoolIdSub(CompletionService<List<UserDemoExcel>> completionService, UserDemoQueryBO queryBO);


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
}
