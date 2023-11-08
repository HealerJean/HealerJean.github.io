package com.healerjean.proj.service;


import com.healerjean.proj.service.bizlog.data.po.User;

/**
 * UserQueryService
 * @author zhangyujin
 * @date 2023/5/31  16:02
 */
public interface UserQueryService {

    /**
     * getUser
     * @param userId userId
     * @return User
     */
    User getUser(String userId);
}
