package com.healerjean.proj.junit;

import com.healerjean.proj.BaseJunit5MockitoTest;
import com.healerjean.proj.service.service.CenterService;
import com.healerjean.proj.service.service.impl.TopServiceImpl;
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
    @InjectMocks
    private TopServiceImpl topService;

    @Mock
    private CenterService centerService;

    @DisplayName("Junit5MockitoTest.test")
    @Test
    public void test(){
        when(centerService.centerMethod(anyString())).thenReturn("mockCenterMethod");
        String result = topService.topMethod("HealerJean");
        log.info("result:{}", result);
    }

}
