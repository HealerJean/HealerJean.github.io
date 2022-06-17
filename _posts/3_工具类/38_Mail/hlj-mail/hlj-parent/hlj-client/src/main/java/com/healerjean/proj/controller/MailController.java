package com.healerjean.proj.controller;

import com.google.common.collect.Lists;
import com.healerjean.proj.common.ResponseBean;
import com.healerjean.proj.dto.mail.SendMailDTO;
import com.healerjean.proj.util.mail.MailUtils;
import com.healerjean.proj.util.mail.ListToHtmlUtils;
import com.healerjean.proj.util.mail.dto.ListHtmlTableDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author zhangyujin
 * @date 2021/12/15  11:23 上午.
 * @description
 */
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "访问正常"),
        @ApiResponse(code = 301, message = "逻辑错误"),
        @ApiResponse(code = 500, message = "系统错误"),
        @ApiResponse(code = 401, message = "未认证"),
        @ApiResponse(code = 403, message = "禁止访问"),
        @ApiResponse(code = 404, message = "url错误")
})
@Api(description = "mail控制器")
@Controller
@RequestMapping("hlj/mail")
@Slf4j
public class MailController {

    @Autowired
    private MailUtils  mailUtils;

    @ApiOperation(value = "sendMail",
            notes = "sendMail",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ResponseBody
    @PostMapping(value = "sendMail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean sendMail(@RequestBody SendMailDTO sendMailDTO) throws Exception {
        List<ListHtmlTableDTO> list = Lists.newArrayList();
        ListHtmlTableDTO listHtmlTableDTO = new ListHtmlTableDTO();
        listHtmlTableDTO.setSystemName("A系统");
        listHtmlTableDTO.setName("Name1");
        ListHtmlTableDTO listHtmlTableDTO2 = new ListHtmlTableDTO();
        listHtmlTableDTO2.setSystemName("B系统");
        listHtmlTableDTO2.setName("Name2");
        ListHtmlTableDTO listHtmlTableDTO3 = new ListHtmlTableDTO();
        listHtmlTableDTO3.setSystemName("B系统");
        listHtmlTableDTO3.setName("Name3");
        list.add(listHtmlTableDTO);
        list.add(listHtmlTableDTO2);
        list.add(listHtmlTableDTO3);
        String content=  ListToHtmlUtils.convertHtml(list, "title", "bottom");
        sendMailDTO.setContent(content);
        mailUtils.sendMail(sendMailDTO);
        return ResponseBean.buildSuccess();
    }
}
