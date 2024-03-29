package com.hlj.proj.controller;

import com.hlj.proj.controller.config.aspect.LogIndex;
import com.hlj.proj.controller.dto.UserDTO;
import com.hlj.proj.controller.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName LogbackController
 * @date 2019/7/12  15:12.
 * @Description logback 测试
 */
@RestController
@RequestMapping(value = "hlj")
@Slf4j
public class LogbackController {

    public static final Marker MARKER_MQ_LOG = MarkerFactory.getMarker("MARKER_MQ_LOG");


    private static final Logger BIZ_LOG = LoggerFactory.getLogger("BIZ_LOG");

    @LogIndex
    @GetMapping("logback")
    public String logback() {
        log.debug("debug日志==================");
        log.info("info日志==================");
        log.warn("warn日志====================");
        log.error("error日志=====================");
        int i = 1 / 0;
        return "日志处理成功";
    }


    @LogIndex
    @GetMapping("logJson")
    public UserDTO logJson(HttpServletRequest request) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(0L);
        userDTO.setName("HealerJean");
        log.info("userDTO：{}, name：{}", userDTO, userDTO.getName());
        log.info("request:{}", JsonUtils.toJsonString(request));
        return userDTO;
    }


    @LogIndex
    @GetMapping("logBizLog")
    public String logBizLog() {
        BIZ_LOG.debug("BUSINESS_LOG debug日志==================");
        BIZ_LOG.info("BUSINESS_LOG info日志==================");
        BIZ_LOG.warn("BUSINESS_LOG warn日志====================");
        BIZ_LOG.error("BUSINESS_LOG error日志=====================");
        return "ok";
    }


    @LogIndex
    @GetMapping("markerLogBizLog")
    public String markerLogBizlOG() {
        log.debug(MARKER_MQ_LOG,"debug日志==================");
        log.info(MARKER_MQ_LOG,"info日志==================");
        log.warn(MARKER_MQ_LOG,"warn日志====================");
        log.error(MARKER_MQ_LOG,"error日志=====================");
        return "ok";
    }

}
