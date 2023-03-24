package com.healerjean.proj.junit;

import com.healerjean.proj.BaseJunit5Test;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Junit5Test
 * @author zhangyujin
 * @date 2023/3/23  15:51.
 */
@Slf4j
public class Junit5Test extends BaseJunit5Test {

    @Disabled
    @DisplayName("test Disabled")
    @Test
    void testDisabled() {
        log.info("[Junit5Test#testDisabled] testDisabled");
    }


    @Test
    @DisplayName("array assertion")
    public void array() {
        assertArrayEquals(new int[]{1, 2}, new int[] {1, 2});
    }


    @Test
    @DisplayName("assert all")
    public void all() {
        assertAll("组合断言失败",
                () -> Assertions.assertEquals(2, 1 + 1),
                () -> Assertions.assertTrue(false)
        );
    }


    @Test
    @DisplayName("异常断言")
    public void exceptionTest() {
        Assertions.assertThrows(
                ArithmeticException.class, () -> {
                    int i = 1 % 0;
                });
    }

    @Test
    @DisplayName("超时测试(如果测试方法时间超过1s将会异常)")
    public void timeoutTest() {
        Assertions.assertTimeout(Duration.ofMillis(1000), () -> Thread.sleep(500));
    }


    @Test
    @DisplayName("fail")
    public void shouldFail() {
        fail("This should fail");
    }


    /**
     * 测试前置条件
     */
    @DisplayName("测试前置条件")
    @Test
    void testAssumptions() {
        Assumptions.assumeTrue(false, "结果不足true");
        log.info("[Junit5Test#testAssumptions] 结果不足true");
    }



    @ParameterizedTest
    @DisplayName("参数化测试")
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void testParamterized(int i) {
        System.out.println(i);
    }


    @ParameterizedTest
    @DisplayName("参数化方法测试")
    @MethodSource("stringProvider")
    void testParamterized2(String s) {
        System.out.println(s);
    }

    static Stream<String> stringProvider() {
        return Stream.of("apple", "banana");
    }

    @Nested
    @DisplayName("when new")
    class InnerTest {

        @BeforeEach
        void innerBeforeEach() {
            log.info("[InnerTest#innerBeforeEach] innerBeforeEach");
        }


        @Test
        @DisplayName("is empty")
        void isEmpty() {
            timeoutTest();
            assertTrue(true, "参数不为true");
        }

    }


}
