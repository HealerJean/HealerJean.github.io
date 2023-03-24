package com.healerjean.proj.junit;

import com.healerjean.proj.BaseJunit5SpringTest;
import com.healerjean.proj.service.BottomService;
import com.healerjean.proj.service.TopService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

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
