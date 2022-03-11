package com.healerjean.proj.service.impl;

import com.healerjean.proj.constants.LogRecordType;
import com.healerjean.proj.context.LogRecordContext;
import com.healerjean.proj.service.UserQueryService;
import com.healerjean.proj.starter.annotation.LogRecordAnnotation;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author muzhantong
 * create on 2021/2/10 11:13 上午
 */
@Service
public class UserQueryServiceImpl implements UserQueryService {

    @Override
    @LogRecordAnnotation(success = "获取用户列表,内层方法调用人{{#user}}", prefix = LogRecordType.ORDER, bizNo = "{{#user}}")
    public List<User> getUserList(List<String> userIds) {
        LogRecordContext.putVariable("user", "mzt");
        return null;
    }
}
