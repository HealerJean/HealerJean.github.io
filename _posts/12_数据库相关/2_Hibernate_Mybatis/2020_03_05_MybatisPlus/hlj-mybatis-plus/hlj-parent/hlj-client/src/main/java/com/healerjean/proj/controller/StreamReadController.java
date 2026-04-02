package com.healerjean.proj.controller;

import com.google.common.collect.Lists;
import com.healerjean.proj.common.dto.ResponseBean;
import com.healerjean.proj.data.entity.User;
import com.healerjean.proj.data.mapper.UserMapper;
import com.healerjean.proj.dto.UserDTO;
import com.healerjean.proj.utils.JsonUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
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
     * sqlSessionFactory
     */
    @Resource
    private SqlSessionFactory sqlSessionFactory;


    @GetMapping(value = "selectList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseBean selectList() {
        List<User> users = Lists.newArrayList();

        try(SqlSession sqlSession = sqlSessionFactory.openSession()){
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            mapper.getStreamAll(new ResultHandler<User>() {
                private int batchCount = 0;
                private List<User> batch = new ArrayList<>(1000);
                @Override
                public void handleResult(ResultContext<? extends User> resultContext) {
                    User user = resultContext.getResultObject();
                    batch.add(user);
                    batchCount++;

                    // 每 1000 条处理一次（如批量入库、写文件）
                    if (batchCount % 1000 == 0) {
                        processBatch(batch);
                        batch.clear();
                        sqlSession.commit();
                    }
                }
                private void processBatch(List<User> batch) {
                    // 例如：批量插入、导出文件、发送消息等
                }
            });
            sqlSession.commit();
        }

        log.info("用户管理--------selectLis：【{}】", JsonUtils.toJsonString(users));
        return ResponseBean.buildSuccess(users);
    }
}
