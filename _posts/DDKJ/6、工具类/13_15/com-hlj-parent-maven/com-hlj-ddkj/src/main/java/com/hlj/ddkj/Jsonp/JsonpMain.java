package com.hlj.ddkj.Jsonp;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.Test;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JsonpMain {

    @Test
    public void testGet() throws IOException {

        //data中可以放入我们想要的参数，map类型或者是其他的自己看
        //method()后面没有其他方法
        Map<String,String> data =  new HashMap<>();
        Map<String,String> headers = new HashMap<>();

        Connection.Response connection = Jsoup.connect("http://www.baidu.com")
                                                .data(data)
                                                .headers(headers)
                                                .method(Connection.Method.GET)
                                                .ignoreContentType(true)
                                                .validateTLSCertificates(false)
                                                .execute();

        String body =  connection.body();
        log.info(body);
    }
}
