package com.healerjean.proj.base;

import com.healerjean.proj.TomcatLauncher;
import com.healerjean.proj.mock.DemoPrcResourceMock;
import com.healerjean.proj.rpc.provider.DemoPrcResource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Junit5SpringBaseTest
 *
 * @author zhangyujin
 * @date 2023/3/23  17:12.
 */
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@DisplayName("Junit5-SpringBootTest 基础类")
@SpringBootTest(classes = TomcatLauncher.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseJunit5SpringTest {

    /**
     * 1、使用@Resource 会有问题，不让 when
     * 2、其他地方在使用的时候，用@Resource，不可以使用@MockBean了，因为会导致重复
     */
    @MockBean
    private DemoPrcResource demoPrcResource;


    /**
     * 非静态方法必须指定 @TestInstance(TestInstance.Lifecycle.PER_CLASS)
     */
    @BeforeAll
    public void beforeAll() {
        when(demoPrcResource.rpcInvoke(any())).thenReturn(DemoPrcResourceMock.rpcInvokeReturn());
    }

    /**
     * 每个测试方法运行前运行
     */
    @BeforeEach
    public void beforeEach() {
    }

    /**
     * 每个测试方法运行完毕后运行
     */
    @AfterEach
    public void afterEach() {
    }

    /**
     * 在所有测试方法运行完毕后运行
     */
    @AfterAll
    public static void afterAll() {
    }

}
