package com.healerjean.proj.mock.A_02_进阶;

import com.healerjean.proj.mock.A_02_进阶.service.NetCallback;

import java.util.Map;

/**
 * @author zhangyujin
 * @date 2022/3/31  13:23.
 * @description
 */
public class Api{
    /**
     *  网络请求处理操作逻辑，这块忽略写法
     *  1、成功调用callback.onSuccess(data);
     *  2、失败调用callback.onFailure(code,msg);
     */
    public void get(String url, Map<String, String> params, NetCallback netCallback) {

    }
}