package com.healerjean.proj.service.impl;


import com.healerjean.proj.beans.Operator;
import com.healerjean.proj.service.IOperatorGetService;

/**
 * @author muzhantong
 * create on 2020/4/29 5:45 下午
 */
public class DefaultOperatorGetServiceImpl implements IOperatorGetService {

    @Override
    public Operator getUser() {
        Operator operator = new Operator();
        operator.setOperatorId("2222");
        return operator;
    }
}
