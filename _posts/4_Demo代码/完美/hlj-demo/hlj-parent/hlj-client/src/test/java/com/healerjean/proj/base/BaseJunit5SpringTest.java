package com.healerjean.proj.base;

import com.healerjean.proj.TomcatLauncher;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
     * 非静态方法必须指定 @TestInstance(TestInstance.Lifecycle.PER_CLASS)
     */
    @BeforeAll
    public void beforeAll() {
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
