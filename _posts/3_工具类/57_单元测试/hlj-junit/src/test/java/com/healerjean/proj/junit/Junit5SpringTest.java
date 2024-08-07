package com.healerjean.proj.junit;

import com.healerjean.proj.BaseJunit5SpringTest;
import com.healerjean.proj.service.service.BottomService;
import com.healerjean.proj.service.service.TopService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import java.time.LocalDateTime;

import static java.time.ZoneOffset.UTC;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Junit5SpringTest
 * @author zhangyujin
 * @date 2023/3/23  17:23.
 */
@Slf4j
public class Junit5SpringTest extends BaseJunit5SpringTest {

    /**
     * topService
     */
    @Resource
    private TopService topService;

    /**
     * bottomService
     */
    @MockBean
    private BottomService bottomService;


    @DisplayName("topService.topMethod")
    @Test
    public void test(){
        when(bottomService.bottomMethod(anyString())).thenReturn("mockBottomMethod");
        String result = topService.topMethod("HealerJean");
        log.info("result:{}", result);
    }


}
