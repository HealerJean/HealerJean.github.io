package com.healerjean.proj.data.manager;

import com.google.common.collect.Lists;
import com.healerjean.proj.base.BaseJunit5SpringTest;
import com.healerjean.proj.data.po.UserDemo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * UserDemoManagerTest
 *
 * @author zhangyujin
 * @date 2024/3/12
 */
@Slf4j
@DisplayName("UserDemoServiceTest")
class UserDemoManagerTest extends BaseJunit5SpringTest {

    /**
     * userDemoManager
     */
    @Resource
    private UserDemoManager userDemoManager;


    @Test
    public void test(){
        UserDemo userDemo = new UserDemo();
        userDemo.setName("healerjean");
        userDemo.setAge(23);
        userDemo.setPhone("18833334444");
        userDemo.setEmail("healerJean");
        userDemo.setValidFlag(1);
        List<UserDemo> users = Lists.newArrayList(userDemo, userDemo);
        userDemoManager.batchSaveOrUpdateUserDemo(users);
    }
}