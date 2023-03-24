package com.healerjean.proj;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Junit5SpringBaseTest
 * @author zhangyujin
 * @date 2023/3/23  17:12.
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = HljJunitApplication.class)
@DisplayName("Junit5-SpringBootTest 基础类")
public class BaseJunit5SpringTest {


    /**
     * 所有测试方法运行前运行
     */
    @BeforeAll
    public static void beforeAll() {
        log.info("[Junit5BaseTest#beforeAll] Run before all test methods run");
    }

    /**
     * 每个测试方法运行前运行
     */
    @BeforeEach
    public void beforeEach() {
        log.info("[Junit5BaseTest#beforeEach] Run before each test method runs");
    }

    /**
     * 每个测试方法运行完毕后运行
     */
    @AfterEach
    public void afterEach() {
        log.info("[Junit5BaseTest#afterEach] Run after each test method finishes running");
    }

    /**
     * 在所有测试方法运行完毕后运行
     */
    @AfterAll
    public static void afterAll() {
        log.info("[Junit5BaseTest#afterAll] Run after all test methods have finished running");
    }

}
