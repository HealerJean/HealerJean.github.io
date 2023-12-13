package com.healerjean.proj.service;

import com.healerjean.proj.base.BaseJunit5SpringTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;

import javax.annotation.Resource;

/**
 * UserDemoServiceTest
 *
 * @author zhangyujin
 * @date 2023/7/5$  15:31$
 */
@Slf4j
@DisplayName("UserDemoServiceTest")
public class UserDemoServiceTest extends BaseJunit5SpringTest {

    /**
     * userDemoService
     */
    @Resource
    private UserDemoService userDemoService;


}
