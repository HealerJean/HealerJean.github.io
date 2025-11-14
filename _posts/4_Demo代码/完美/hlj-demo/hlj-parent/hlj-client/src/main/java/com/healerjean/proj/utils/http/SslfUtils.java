package com.healerjean.proj.utils.http;

import com.google.common.collect.Sets;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

/**
 * SslfUtils
 *
 * @author zhangyujin
 * @date 2025/11/12
 */

public class SslfUtils {

    private static final Set<String> ALLOWED_PROTOCOLS = Sets.newHashSet("http", "https");
    private static final Set<Integer> ALLOWED_PORTS = Sets.newHashSet(80, 443);


    public static boolean sslfUrl(String url, List<String> allowedDomains) {
        URL urlObj;
        try {
            urlObj = new URL(url);
        } catch (Exception e) {
            System.out.println("URL 不合法: " + url);
            return false;
        }

        // 协议检查
        if (!ALLOWED_PROTOCOLS.contains(urlObj.getProtocol())) {
            System.out.println("协议不合法: " + url);
            return false;
        }

        // 域名检查
        String host = urlObj.getHost();
        if (!isAllowedDomain(host, allowedDomains)) {
            System.out.println("域名不合法: " + url);
            return false;
        }

        // IP 地址检查
        try {
            InetAddress address = InetAddress.getByName(host);
            if (address.isSiteLocalAddress() || address.isLoopbackAddress()) {
                System.out.println("禁止访问私有地址: " + url);
                return false;
            }
        } catch (UnknownHostException e) {
            System.out.println("DNS 解析失败: " + url);
            return false;
        }

        // 端口检查
        int port = urlObj.getPort();
        if (port == -1) {
            port = "https".equals(urlObj.getProtocol()) ? 443 : 80;
        }
        if (!ALLOWED_PORTS.contains(port)) {
            System.out.println("端口不合法: " + url);
            return false;
        }

        return true;
    }

    private static boolean isAllowedDomain(String host, List<String> allowedDomains) {
        for (String allowedDomain : allowedDomains) {
            if (allowedDomain.startsWith("*.")) {
                String suffix = allowedDomain.substring(2);
                if (host.endsWith(suffix)) {
                    return true;
                }
            } else if (host.equals(allowedDomain)) {
                return true;
            }
        }
        return false;
    }
}