package com.healerjean.proj.junit;

import com.healerjean.proj.BaseJunit5MockitoTest;
import com.healerjean.proj.service.BottomService;
import com.healerjean.proj.service.TopService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Junit5MockitoTest
 * @author zhangyujin
 * @date 2023/3/23  17:36.
 */
@Slf4j
public class Junit5MockitoTest extends BaseJunit5MockitoTest {

    /**
     * topService
     */
    @Mock
    private TopService topService;

    @InjectMocks
    private BottomService bottomService;


    @DisplayName("Junit5MockitoTest.test")
    @Test
    public void test(){
        when(bottomService.bottomMethod(anyString())).thenReturn("mockBottomMethod");
        String result = topService.topMethod("HealerJean");
        log.info("result:{}", result);
    }

}
