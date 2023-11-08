package com.healerjean.proj.service.bizlog.service.impl;


import com.healerjean.proj.service.bizlog.data.po.Operator;
import com.healerjean.proj.service.bizlog.service.IOperatorGetService;
import org.springframework.stereotype.Service;

/**
 * @author muzhantong
 * create on 2020/4/29 5:45 下午
 */
@Service
public class DefaultOperatorGetServiceImpl implements IOperatorGetService {

    /**
     * 获取用户
     * @return Operator
     */
    @Override
    public Operator getUser() {
        Operator operator = new Operator();
        operator.setOperatorId("2222");
        return operator;
    }
}
