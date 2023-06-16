package com.healerjean.proj.base.junit;

import com.healerjean.proj.TomcatLauncher;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Junit5SpringBaseTest
 * @author zhangyujin
 * @date 2023/3/23  17:12.
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TomcatLauncher.class)
@DisplayName("Junit5-SpringBootTest 基础类")
public class BaseJunit5SpringTest {


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
