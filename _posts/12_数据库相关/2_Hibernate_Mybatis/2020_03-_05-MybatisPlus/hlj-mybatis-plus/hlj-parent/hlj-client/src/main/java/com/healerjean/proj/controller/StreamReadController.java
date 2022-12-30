package com.healerjean.proj.controller;

import com.google.common.collect.Lists;
import com.healerjean.proj.common.dto.ResponseBean;
import com.healerjean.proj.data.entity.User;
import com.healerjean.proj.data.mapper.UserMapper;
import com.healerjean.proj.dto.UserDTO;
import com.healerjean.proj.utils.JsonUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhangyujin
 * @date 2022/12/30  14:35.
 */
@RestController
@RequestMapping("hlj/stream")
@Api(description = "流式")
@Slf4j
public class StreamReadController {

    /**
     * userMapper
     */
    @Resource
    private UserMapper userMapper;

    @GetMapping(value = "selectList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseBean selectList() {
        List<User> users = Lists.newArrayList();
        userMapper.getStreamAll(new ResultHandler<User>() {
            /**
             * mybatis流失查询会回调处理逻辑
             */
            @Override
            public void handleResult(ResultContext<? extends User> resultContext) {
                log.info("resultContext.result:{}", resultContext);
                users.add(resultContext.getResultObject());
            }
        });

        log.info("用户管理--------selectLis：【{}】", JsonUtils.toJsonString(users));
        return ResponseBean.buildSuccess(users);
    }
}
