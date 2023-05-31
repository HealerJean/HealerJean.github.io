package com.healerjean.proj.service.impl;

import com.healerjean.proj.service.UserQueryService;
import com.healerjean.proj.service.bizlog.anno.LogRecordAnnotation;
import com.healerjean.proj.service.bizlog.common.BizLogConstants;
import com.healerjean.proj.service.bizlog.data.po.User;
import com.healerjean.proj.service.bizlog.utils.LogTheadLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * UserQueryServiceImpl
 * @author zhangyujin
 * @date 2023/5/31  15:58
 */
@Slf4j
@Service
public class UserQueryServiceImpl implements UserQueryService {

    /**
     * getUserList
     * @param userId userId
     * @return User
     */
    @Override
    @LogRecordAnnotation(success = "获取用户列表,内层方法调用人{{#user}}",
            prefix = BizLogConstants.BizLogTypeConstant.ORDER_TYPE,
            bizNo = "{{#user}}")
    public User getUser(String userId) {
        LogTheadLocal.putVariable("user", userId);
        return null;
    }
}
