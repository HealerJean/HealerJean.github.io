package com.healerjean.proj.mock.A_02_进阶;

import com.healerjean.proj.mock.A_02_进阶.service.NetCallback;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.helpers.MessageFormatter;

import java.util.HashMap;

/**
 * @author zhangyujin
 * @date 2022/3/31  13:24.
 * @description
 */
public class TestMain {

    @Test
    public void test() {
        Api api = Mockito.mock(Api.class);

        Mockito.doAnswer(invocation -> {
            // 1、获取参数
            Object[] arguments = invocation.getArguments();
            NetCallback callback = (NetCallback) arguments[2];
            // callback.onFailure(500, "Server error");
            callback.onSuccess("hello");
            return 500;
        }).when(api).get(Mockito.anyString(), Mockito.any(HashMap.class), Mockito.any(NetCallback.class));


        //这时我们调用api.get()方法
        api.get("blog.healerjean.com", new HashMap<>(16), new NetCallback() {
            @Override
            public void onSuccess(Object data) {
                String logMsg = MessageFormatter.arrayFormat("success, data:{}", new Object[]{data}).getMessage();
                System.out.print(logMsg);
            }

            @Override
            public void onFailure(int code, String msg) {
                String logMsg = MessageFormatter.arrayFormat("fail, code:{}, msg:{}", new Object[]{code, msg}).getMessage();
                System.out.print(logMsg);
            }
        });
    }
}
