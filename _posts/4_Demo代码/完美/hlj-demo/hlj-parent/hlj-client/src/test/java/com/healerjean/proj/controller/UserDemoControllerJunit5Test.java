package com.healerjean.proj.controller;

import com.healerjean.proj.base.junit.BaseJunit5SpringTest;
import com.healerjean.proj.common.contants.DataSourceConstant;
import com.healerjean.proj.common.data.bo.BaseRes;
import com.healerjean.proj.data.vo.UserDemoVO;
import com.healerjean.proj.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.context.jdbc.HSql;
import org.springframework.test.context.jdbc.SqlConfig;

import javax.annotation.Resource;

/**
 * UserDemoControllerTest
 *
 * @author zhangyujin
 * @date 2023/6/15  15:42.
 */
@Slf4j
public class UserDemoControllerJunit5Test extends BaseJunit5SpringTest {

    /**
     * userDemoController
     */
    @Resource
    private UserDemoController userDemoController;


    @ParameterizedTest
    @DisplayName("testQueryUserDemoSingle")
    @ValueSource(longs = {100, 102, 103, 104, 999})
    @HSql(value = {"/ut/mock/sql/unit/controller/healerjean/UserDemoControllerTest/testQueryUserDemoSingle.sql"})
    @HSql(value = {"/ut/mock/sql/clean/user_demo_clean.sql"}, executionPhase = HSql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testQueryUserDemoSingle(long id) {
        BaseRes<UserDemoVO> res = userDemoController.queryUserDemoSingle(id);
        log.info("[UserDemoControllerTest#testQueryUserDemoSingle] id:{}, res:{}", id, JsonUtils.toString(res));
        Assertions.assertNotNull(res, "res不能为空");
        Assertions.assertEquals(res.getSuccess(), Boolean.TRUE);
    }


    @ParameterizedTest
    @DisplayName("testQueryUserDemoSingleDbTest")
    @ValueSource(longs = {1, 100, 102, 103, 104, 999})
    @HSql(config = @SqlConfig(dataSource = DataSourceConstant.MASTER_HEALER_JEAN),
            value = {"/ut/mock/sql/unit/controller/healerjean/UserDemoControllerTest/testQueryUserDemoSingle.sql"})
    @HSql(config = @SqlConfig(dataSource = DataSourceConstant.MASTER_HEALER_JEAN),
            value = {"/ut/mock/sql/clean/user_demo_clean.sql"},
            executionPhase = HSql.ExecutionPhase.AFTER_TEST_METHOD)
    @HSql(config = @SqlConfig(dataSource = DataSourceConstant.SLAVE_TEST),
            value = {"/ut/mock/sql/unit/controller/test/UserDemoControllerTest/testQueryUserDemoSingle.sql"})
    @HSql(config = @SqlConfig(dataSource = DataSourceConstant.SLAVE_TEST),
            value = {"/ut/mock/sql/clean/user_demo_clean.sql"},
            executionPhase = HSql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testQueryUserDemoSingleDbTest(long id) {
        BaseRes<UserDemoVO> res = userDemoController.queryUserDemoSingle(id);
        log.info("[UserDemoControllerTest#testQueryUserDemoSingleDbTest] id:{}, res:{}", id, JsonUtils.toString(res));
        Assertions.assertNotNull(res, "res不能为空");
        Assertions.assertEquals(res.getSuccess(), Boolean.TRUE);
    }

    @ParameterizedTest
    @DisplayName("testQueryUserDemoSingleDbTest")
    @ValueSource(longs = {1, 100, 102, 103, 104, 999})
    @HSql(config = @SqlConfig(dataSource = DataSourceConstant.SLAVE_TEST),
            value = {"/ut/mock/sql/unit/controller/test/UserDemoControllerTest/testQueryUserDemoSingle.sql"})
    @HSql(config = @SqlConfig(dataSource = DataSourceConstant.SLAVE_TEST),
            value = {"/ut/mock/sql/clean/user_demo_clean.sql"},
            executionPhase = HSql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testQueryUserDemoSingleDbTest1(long id) {
        BaseRes<UserDemoVO> res = userDemoController.queryUserDemoSingle(id);
        log.info("[UserDemoControllerTest#testQueryUserDemoSingleDbTest] id:{}, res:{}", id, JsonUtils.toString(res));
        Assertions.assertNotNull(res, "res不能为空");
        Assertions.assertEquals(res.getSuccess(), Boolean.TRUE);
    }


    @ParameterizedTest
    @DisplayName("testQueryUserDemoSingleDbTest")
    @ValueSource(longs = {1, 100, 102, 103, 104, 999})
    @HSql(config = @SqlConfig(dataSource = DataSourceConstant.MASTER_HEALER_JEAN),
            value = {"/ut/mock/sql/unit/controller/healerjean/UserDemoControllerTest/testQueryUserDemoSingle.sql"})
    @HSql(config = @SqlConfig(dataSource = DataSourceConstant.MASTER_HEALER_JEAN),
            value = {"/ut/mock/sql/clean/user_demo_clean.sql"},
            executionPhase = HSql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testQueryUserDemoSingleDbTest2(long id) {
        BaseRes<UserDemoVO> res = userDemoController.queryUserDemoSingle(id);
        log.info("[UserDemoControllerTest#testQueryUserDemoSingleDbTest] id:{}, res:{}", id, JsonUtils.toString(res));
        Assertions.assertNotNull(res, "res不能为空");
        Assertions.assertEquals(res.getSuccess(), Boolean.TRUE);
    }

}

