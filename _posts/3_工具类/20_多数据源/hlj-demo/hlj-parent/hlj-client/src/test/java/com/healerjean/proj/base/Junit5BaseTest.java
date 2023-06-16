package com.healerjean.proj.base;

import com.healerjean.proj.TomcatLauncher;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


/**
 * Junit5BaseTest
 *
 * @author zhangyujin
 * @date 2023/6/14  17:23.
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TomcatLauncher.class)
@DisplayName("Junit5BaseTest")
public class Junit5BaseTest {


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
