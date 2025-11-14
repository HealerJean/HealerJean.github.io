package com.healerjean.proj.utils.http;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

/**
 * HTTP 工具类：包含安全 URL 校验（防 SSRF）和响应写入功能
 *
 * @author zhangyujin
 * @date 2025/3/17
 */
@Slf4j
public class HttpUtils {

    /**
     * 默认允许的协议（用于 SSRF 防护）
     */
    private static final Set<String> ALLOWED_PROTOCOLS = Sets.newHashSet("http", "https");

    /**
     * 默认允许的端口（用于 SSRF 防护）
     */
    private static final Set<Integer> ALLOWED_PORTS = Sets.newHashSet(80, 443);

    // ==================== URL 安全校验方法 ====================

    /**
     * 校验给定 URL 的 host 是否在允许的域名白名单中（仅校验域名，不校验协议/端口/IP）
     * <p>
     * 白名单规则：
     * - 以 "." 开头（如 ".bail.com"）：匹配主域及所有子域（bail.com, app.bail.com, x.y.bail.com）
     * - 不以 "." 开头（如 "bail.com"）：仅精确匹配该主域
     *
     * @param url            待校验的完整 URL（如 "https://app.bail.com/path"）
     * @param allowedDomains 域名白名单
     * @return true 表示域名合法，false 表示不合法或 URL 格式错误
     */
    public static boolean isValidUrl(String url, List<String> allowedDomains) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }
        String host;
        try {
            // 使用 URI 解析，避免 DNS 查询（安全！）
            host = new URI(url).getHost();
        } catch (URISyntaxException e) {
            log.warn("URL 格式非法: {}", url, e);
            return false;
        }

        if (host == null || host.isEmpty()) {
            log.warn("URL 缺少 host: {}", url);
            return false;
        }

        // 统一小写处理，确保匹配一致性
        host = host.toLowerCase();
        return isAllowedDomain(host, allowedDomains);
    }

    /**
     * 全面校验 URL 安全性（用于防止 SSRF 攻击）
     * <p>
     * 校验项包括：
     * 1. 协议是否为 http/https
     * 2. 域名是否在白名单内
     * 3. 是否尝试访问私有 IP（回环、内网等）
     * 4. 端口是否为 80/443
     *
     * @param url            待校验的 URL 字符串
     * @param allowedDomains 允许的域名白名单（支持 .domain.com 通配）
     * @return true 表示安全，false 表示存在风险
     */
    public static boolean sslfUrl(String url, List<String> allowedDomains) {
        if (url == null || url.trim().isEmpty()) {
            log.warn("sslfUrl - 输入 URL 为空");
            return false;
        }

        URI uri;
        try {
            // 使用 URI 而非 URL，避免意外 DNS 查询或连接
            uri = new URI(url);
        } catch (URISyntaxException e) {
            log.warn("sslfUrl - URL 格式非法: {}", url, e);
            return false;
        }

        // 1. 协议检查
        String scheme = uri.getScheme();
        if (scheme == null || !ALLOWED_PROTOCOLS.contains(scheme.toLowerCase())) {
            log.warn("sslfUrl - 协议不合法: {}", url);
            return false;
        }

        // 2. Host 检查
        String host = uri.getHost();
        if (host == null || host.isEmpty()) {
            log.warn("sslfUrl - URL 缺少 host: {}", url);
            return false;
        }
        host = host.toLowerCase();

        if (!isAllowedDomain(host, allowedDomains)) {
            log.warn("sslfUrl - 域名不在白名单内: {}", url);
            return false;
        }

        // 3. 私有地址检查（防止 SSRF）
        try {
            InetAddress address = InetAddress.getByName(host);
            if (address.isAnyLocalAddress() ||
                    address.isLoopbackAddress() ||
                    address.isLinkLocalAddress() ||
                    address.isSiteLocalAddress()) {
                log.warn("sslfUrl - 禁止访问私有或回环地址: {}", url);
                return false;
            }
        } catch (UnknownHostException e) {
            log.warn("sslfUrl - DNS 解析失败: {}", url, e);
            return false;
        }

        // 4. 端口检查
        int port = uri.getPort();
        if (port == -1) {
            // 默认端口：https -> 443, http -> 80
            port = "https".equalsIgnoreCase(scheme) ? 443 : 80;
        }
        if (!ALLOWED_PORTS.contains(port)) {
            log.warn("sslfUrl - 端口不合法: {} (port={})", url, port);
            return false;
        }

        return true;
    }

    /**
     * 判断 host 是否匹配域名白名单规则
     *
     * @param host           目标主机名（已转为小写）
     * @param allowedDomains 白名单列表（如 [".bail.com", "api.trusted.com"]）
     * @return 是否匹配
     */
    private static boolean isAllowedDomain(String host, List<String> allowedDomains) {
        for (String pattern : allowedDomains) {
            if (pattern == null || pattern.trim().isEmpty()) {
                continue;
            }
            String cleanPattern = pattern.trim().toLowerCase();

            if (cleanPattern.startsWith(".")) {
                // 通配模式：.example.com
                String domain = cleanPattern.substring(1);
                if (domain.isEmpty()) {
                    continue; // 跳过无效模式如 "."
                }
                // 匹配主域（example.com）或子域（xxx.example.com）
                if (host.equals(domain) || host.endsWith("." + domain)) {
                    return true;
                }
            } else {
                // 精确匹配模式：example.com
                if (host.equals(cleanPattern)) {
                    return true;
                }
            }
        }
        return false;
    }

    // ==================== 响应写入方法 ====================

    /**
     * 将对象以 JSON 格式写入 HttpServletResponse
     * <p>
     * - 自动设置 Content-Type 为 application/json; charset=UTF-8
     * - 支持 String 类型直接输出（避免二次 JSON 化）
     *
     * @param obj      要输出的对象（POJO 或 JSON 字符串）
     * @param response HttpServletResponse
     * @throws RuntimeException 当 IO 异常发生时（通常表示客户端已断开）
     */
    public static void writeHttpResponse(Object obj, HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("HttpUtils#writeHttpResponse: {}", JSONObject.toJSONString(obj));
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        try (PrintWriter writer = response.getWriter()) {
            if (obj instanceof String) {
                writer.write((String) obj);
            } else {
                writer.write(JSONObject.toJSONString(obj));
            }
            writer.flush(); // 显式 flush 更稳妥
        } catch (IOException e) {
            log.error("HttpUtils#writeHttpResponse - 写入响应失败", e);
            throw new RuntimeException("响应写入异常", e);
        }
    }
}