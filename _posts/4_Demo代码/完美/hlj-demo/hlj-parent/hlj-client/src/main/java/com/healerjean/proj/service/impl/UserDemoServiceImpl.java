package com.healerjean.proj.service.impl;

import com.healerjean.proj.common.data.bo.PageBO;
import com.healerjean.proj.common.data.bo.PageQueryBO;
import com.healerjean.proj.common.enums.SystemEnum;
import com.healerjean.proj.data.bo.UserDemoBO;
import com.healerjean.proj.data.bo.UserDemoQueryBO;
import com.healerjean.proj.data.convert.UserConverter;
import com.healerjean.proj.data.manager.UserDemoManager;
import com.healerjean.proj.data.po.UserDemo;
import com.healerjean.proj.service.UserDemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
        return userDemoManager.selectById(id);
    }

    /**
     * queryUserDemoList
     *
     * @param query query
     * @return List<UserDemoBO>
     */
    @Override
    public List<UserDemoBO> queryUserDemoList(UserDemoQueryBO query) {
        return userDemoManager.queryUserDemoList(query);
    }

    /**
     * queryUserDemoPage
     *
     * @param pageQuery pageQuery
     * @return PageBO<UserDemoBO>
     */
    @Override
    public PageBO<UserDemoBO> queryUserDemoPage(PageQueryBO<UserDemoQueryBO> pageQuery) {
        return userDemoManager.queryUserDemoPage(pageQuery);
    }


    /**
     * queryUserDemoPageAll
     *
     * @param pageQuery pageQuery
     * @return PageBO<UserDemoBO>
     */
    @Override
    public List<UserDemoBO> queryUserDemoPageAll(UserDemoQueryBO pageQuery) {
        return userDemoManager.queryUserDemoPageAll(pageQuery);
    }


}

