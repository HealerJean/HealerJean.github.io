package com.healerjean.proj.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.healerjean.proj.common.data.bo.PageBO;
import com.healerjean.proj.common.data.bo.PageQueryBO;
import com.healerjean.proj.common.data.convert.PageConverter;
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
}
