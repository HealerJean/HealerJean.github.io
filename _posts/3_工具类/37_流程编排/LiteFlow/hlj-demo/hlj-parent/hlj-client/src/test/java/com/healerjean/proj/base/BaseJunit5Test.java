package com.healerjean.proj.base;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

/**
 * Junit5BaseTest
 * @author zhangyujin
 * @date 2023/3/23  15:43.
 */
@Slf4j
@DisplayName("junit5功能测试")
public class BaseJunit5Test {


    /**
     * 所有测试方法运行前运行
     */
    @BeforeAll
    public static void beforeAll() {
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
