package com.hlj.proj.controller;

import com.hlj.proj.aspect.LogIndex;
import com.hlj.proj.aspect.business.LogRecordAnnotation;
import com.hlj.proj.bean.LogBean;
import com.hlj.proj.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangyujin
 * @date 2022/1/17  7:16 下午.
 * @description
 */
@RestController
@RequestMapping(value = "hlj/business/log")
@Slf4j
public class BusinessLogController {

    @LogRecordAnnotation(operator = "#logBean.name", content =  "发起请求", bizNo = "#logBean.bizNo")
    @LogIndex
    @GetMapping("demo")
    public String log4j2(LogBean logBean) {
        return JsonUtils.toJsonString(logBean);
    }

}
