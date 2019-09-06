package com.hlj.proj.controller;

import com.hlj.proj.dto.DateBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;

/**
 * @author HealerJean
 * @ClassName DateErrorController
 * @date 2019-08-04  16:50.
 * @Description
 */
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "访问正常"),
        @ApiResponse(code = 301, message = "逻辑错误"),
        @ApiResponse(code = 500, message = "系统错误"),
        @ApiResponse(code = 401, message = "未认证"),
        @ApiResponse(code = 403, message = "禁止访问"),
        @ApiResponse(code = 404, message = "url错误")
})
@Api(description = "JsonObject")
@Controller
@RequestMapping("hlj")
@Slf4j
public class DateErrorController {



    @GetMapping("getMapHaveDateFalse")
    @ResponseBody
    public Map<String, Object> getMap() {
        DateBean dateBean = new DateBean();
        dateBean.setDate(new Date());
        dateBean.setName("HealerJean");
        Map<String, Object> map = (Map<String, Object>) JSONObject.fromObject(dateBean);
        return map;
    }
}
