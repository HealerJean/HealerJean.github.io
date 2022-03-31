package com.healerjean.proj.mock.A_02_进阶.service;

/**
 * @author zhangyujin
 * @date 2022/3/31  13:22.
 * @description
 */
/**
 * 网络请求返回
 */
public interface NetCallback  {

    void onSuccess(Object data);

    void onFailure(int code, String msg);
}

