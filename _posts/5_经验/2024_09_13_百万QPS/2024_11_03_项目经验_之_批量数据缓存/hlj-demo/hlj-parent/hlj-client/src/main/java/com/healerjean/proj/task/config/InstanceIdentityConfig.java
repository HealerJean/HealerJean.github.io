package com.healerjean.proj.task.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.SecureRandom;
import java.util.Enumeration;

/**
 * InstanceIdentityConfig
 *
 * @author zhangyujin
 * @date 2025/11/11
 */
@Getter
@Slf4j
@Configuration
public class InstanceIdentityConfig {

    /**
     * 本机唯一标识：格式为 IP:StartupTimestamp:RandomSuffix
     */
    private String instanceId;

    /**
     * 本机 IPv4 地址
     */
    private String ip;


    @PostConstruct
    public void init() {
        String localIp = getLocalIpAddress();
        long startupTime = System.currentTimeMillis();
        int randomSuffix = new SecureRandom().nextInt(100_000);

        this.ip = localIp;
        this.instanceId = String.format("%s:%d:%d", localIp, startupTime, randomSuffix);
        log.info("实例:{} 唯一标识生成: {}", this.ip, this.instanceId);
    }

    /**
     * getLocalIpAddress
     *
     * @param
     * @return {@link String}
     */
    private static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface nic = interfaces.nextElement();
                if (nic.isLoopback() || !nic.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = nic.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            log.warn("❌ 无法获取本机 IPv4 地址，fallback 到 127.0.0.1", e);
        }
        return "127.0.0.1";
    }
}
