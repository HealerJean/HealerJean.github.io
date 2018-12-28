package com.hlj.moudle.demo.controller;


import com.hlj.data.res.test.JavaBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/3/22  上午10:22.
 */
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "访问正常"),
        @ApiResponse(code = 301, message = "逻辑错误"),
        @ApiResponse(code = 500, message = "系统错误"),
        @ApiResponse(code = 401, message = "未认证"),
        @ApiResponse(code = 403, message = "禁止访问"),
        @ApiResponse(code = 404, message = "url错误")
})
@Api(description = "demo控制器")
@Controller
@RequestMapping("demo")
@Slf4j
public class DemoController {


    @GetMapping("getMapHaveDateFalse")
    @ResponseBody
    public Map<String ,Object> getMap(){

        JavaBean javaBean = getJavaBean();
        Map<String, Object> map = (Map<String, Object>) JSONObject.fromObject(javaBean);

        return map ;
    }


    /**
     * 假数据JavaBean
     * @return
     */
    private JavaBean getJavaBean() {
        JavaBean javaBean = new JavaBean();
        javaBean.setN_long(10045456456L);
        javaBean.setN_bigDecimal(new BigDecimal("12.12245"));
        javaBean.setN_date(new Date());
        javaBean.setN_integer(100);
//        javaBean.setN_string(null);
        javaBean.setN_string("");

        return javaBean;
    }


}
