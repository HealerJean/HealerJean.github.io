package com.hlj.proj.config.interceptor;

import com.hlj.proj.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * @author HealerJean
 * @ClassName RefererInterceptor
 * @date 2019/12/31  10:47.
 * @Description
 */
@Component
@Slf4j
public class RefererInterceptor implements HandlerInterceptor {

    private List<String> whiteList = Arrays.asList("fddsign.otc.mi.com", "staging.usign-web.fintech.pt.xiaomi.com");

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object o) throws Exception {

        String referer = req.getHeader("referer");
        if (referer == null) {
            //直接访问网站 refer为空
            return true;
        }
        URL url = null;
        try {
            url = new java.net.URL(referer);
        } catch (MalformedURLException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return false;
        }
        // 首先判断请求域名和referer域名是否相同
        String host = req.getServerName();
        if (!host.equals(url.getHost())) {
            if (whiteList.contains(url.getHost())) {
                return true;
            }
            return false;
        }
        return true;
    }


}
