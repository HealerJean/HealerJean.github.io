package com.healerjean.proj.service.bizlog.service;


import com.healerjean.proj.service.bizlog.data.po.Operator;

/**
 * 用户获取Service
 * @author zhangyujin
 * @date 2023/5/30  19:58
 */
public interface IOperatorGetService {

    /**
     * 可以在里面外部的获取当前登陆的用户，比如UserContext.getCurrentUser()
     *
     * @return 转换成Operator返回
     */
    Operator getUser();
}
