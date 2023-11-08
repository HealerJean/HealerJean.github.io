package com.healerjean.proj.service.bizlog.service.function.impl;

import com.healerjean.proj.service.UserQueryService;
import com.healerjean.proj.service.bizlog.common.enums.BizLogEnum;
import com.healerjean.proj.service.bizlog.data.po.User;
import com.healerjean.proj.service.bizlog.service.function.IParseFunction;
import com.healerjean.proj.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * UserParseFunction
 *
 * @author zhangyujin
 * @date 2023/5/31  16:07
 */
@Slf4j
@Component
public class UserParseFunction implements IParseFunction {

    /**
     * userQueryService
     */
    @Resource
    private UserQueryService userQueryService;


    @Override
    public String functionName() {
        return BizLogEnum.IParseFunctionEnum.USER_PARSE_FUNCTION.getFunction();
    }

    /**
     * 函数调用
     *
     * @param userId userId
     * @return String
     */
    @Override
    public String apply(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return userId;
        }
        User user = userQueryService.getUser(userId);
        log.info("[UserParseFunction#apply] userId:{}, user:{}", userId, JsonUtils.toJsonString(user));
        return user.getUserId();

    }
}
