package com.healerjean.proj.base.junit;

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
