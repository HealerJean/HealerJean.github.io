package com.healerjean.proj;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * BaseJunit5MockitoTest
 * @author zhangyujin
 * @date 2023/3/23  17:34.
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
public class BaseJunit5MockitoTest {

    /**
     * 所有测试方法运行前运行
     */
    @BeforeAll
    public static void beforeAll() {
        log.info("[Junit5MockitoBaseTest#beforeAll] Run before all test methods run");
    }

    /**
     * 每个测试方法运行前运行
     */
    @BeforeEach
    public void beforeEach() {
        //增加改注解
        log.info("[Junit5MockitoBaseTest#beforeEach] Run before each test method runs");
    }

    /**
     * 每个测试方法运行完毕后运行
     */
    @AfterEach
    public void afterEach() {
        log.info("[Junit5MockitoBaseTest#afterEach] Run after each test method finishes running");
    }

    /**
     * 在所有测试方法运行完毕后运行
     */
    @AfterAll
    public static void afterAll() {
        log.info("[Junit5MockitoBaseTest#afterAll] Run after all test methods have finished running");
    }

}
