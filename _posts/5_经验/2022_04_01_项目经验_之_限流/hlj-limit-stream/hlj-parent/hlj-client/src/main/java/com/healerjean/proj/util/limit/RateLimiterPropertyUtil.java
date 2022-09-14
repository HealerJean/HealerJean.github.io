package com.healerjean.proj.util.limit;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RateLimiterPropertyUtil
 *
 * @author zhangyujin
 * @date 2022/9/8  21:37.
 */
@Service
@Slf4j
public class RateLimiterPropertyUtil {

    /**
     * MAP_RATE
     */
    private static ConcurrentHashMap<String, RateLimiter> MAP_RATE = new ConcurrentHashMap();
    /**
     * props
     */
    private static Properties PROPS;

    private RateLimiterPropertyUtil() {
    }

    @PostConstruct
    public void init(){
        load();
    }

    private static synchronized void load() {
        log.info("开始加载限流文件内容.......");
        PROPS = new Properties();
        InputStream in = null;
        try {
            in = RateLimiterPropertyUtil.class.getResourceAsStream("/rateLimiter.properties");
            PROPS.load(in);
            Set<Map.Entry<Object, Object>> set = PROPS.entrySet();

            for (Map.Entry<Object, Object> objectObjectEntry : set) {
                Map.Entry<Object, Object> entry = objectObjectEntry;
                String key = String.valueOf(entry.getKey()).trim();
                String value = String.valueOf(entry.getValue());
                log.info("限流策略：key:{}, value:{}", key, value);
                if (StringUtils.hasText(key) && StringUtils.hasText(value)) {
                    RateLimiter rateLimiter = RateLimiter.create(Double.valueOf(value));
                    MAP_RATE.put(key, rateLimiter);
                }
            }
        } catch (FileNotFoundException var17) {
            log.error("jdbc.properties文件未找到");
        } catch (IOException var18) {
            log.error("出现IOException");
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException var16) {
                log.error("jdbc.properties文件流关闭出现异常");
            }
        }

        log.info("properties文件内容：" + PROPS);
    }

    public static ConcurrentHashMap<String, RateLimiter> getMap() {
        return MAP_RATE;
    }


}
