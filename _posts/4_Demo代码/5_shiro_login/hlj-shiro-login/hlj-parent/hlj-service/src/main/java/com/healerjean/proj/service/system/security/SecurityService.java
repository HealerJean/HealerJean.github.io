package com.healerjean.proj.service.system.security;

import com.healerjean.proj.constant.CommonConstants;
import com.healerjean.proj.exception.BusinessException;
import com.healerjean.proj.exception.ParameterErrorException;
import com.healerjean.proj.service.system.cache.CacheService;
import com.healerjean.proj.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName 登陆安全规则
 * @Date 2019/10/18  15:10.
 * @Description
 */
@Slf4j
@Service
public class SecurityService {

    private static final String REDIS_VERIFY_KEY_PREFIX = CommonConstants.REDIS_HLJ + ":" + CommonConstants.REDIS_VERIFY_CODE + ":" + CommonConstants.REDIS_PHONE;
    private static final String REDIS_USER_SECURITY_PREFIX = CommonConstants.REDIS_HLJ + ":" + CommonConstants.REDIS_USER_SECURITY + ":";


    @Autowired
    private CacheService cacheService;

    /**
     * 登陆错误次数限制（10次）
     * except admin
     */
    public boolean loginTimeLimitCheck(String username) {
        String localTime = DateUtils.toDateString(new Date(), DateUtils.YYYY_MM_DD);
        StringBuffer sb = new StringBuffer();
        String userRegKey = sb.append(REDIS_USER_SECURITY_PREFIX).append(username).append("_").append(localTime).toString();
        if (!CommonConstants.ADMIN.equals(username)) {
            sb.setLength(0);
            Object o = cacheService.get(userRegKey);
            if (o != null) {
                Integer integer = null;
                try {
                    integer = Integer.valueOf(String.valueOf(o));
                } catch (Exception e) {
                    log.error("登陆安全用户登录次数获取失败");
                    integer = 0;
                }
                if (integer >= 10) {
                    throw new ParameterErrorException("您的账号今天已经被锁定");
                }
            }
        }
        return true;
    }


    public void loginTimeLimitCount(String username, boolean flag) {
        String localTime = DateUtils.toDateString(new Date(), DateUtils.YYYY_MM_DD);
        StringBuffer sb = new StringBuffer();
        String userRegKey = sb.append(REDIS_USER_SECURITY_PREFIX).append(username).append("_").append(localTime).toString();
        if (flag) {
            //登陆成功清除错误计数
            cacheService.delete(userRegKey);
            return;
        }
        if (!CommonConstants.ADMIN.equals(username)) {
            //该处是否需要使用redis的脚本来执行
            Long increment = cacheService.increment(userRegKey, 1L);
            cacheService.set(userRegKey, increment, 1, TimeUnit.DAYS);
            if (increment == 10) {
                throw new BusinessException("密码错误，账号已被锁定");
            }
            throw new BusinessException("密码错误，今天还有" + (10 - increment) + "次错误机会");
        }
        throw new BusinessException("密码错误");
    }


    /**
     * 校验验证码次数
     */
    public void checkMsgSecurit(String ip, String key, String type) {
        //先判断发送时长是否有一分钟
        String oneMinKey = REDIS_VERIFY_KEY_PREFIX + CommonConstants.REDIS_ONE_MIN + ":" + ip + "_" + key + "_" + type;
        Object oneMinResult = cacheService.get(oneMinKey);
        if (oneMinResult != null) {
            log.error("短信验证码已经发送，请等待一分钟后在尝试");
            throw new BusinessException("短信验证码已经发送，请等待一分钟后在尝试");
        }

        //判断每天一个手机只能发送十次
        String dateString = DateUtils.toDateString(new Date(), DateUtils.YYYY_MM_DD);
        String allDayKey = REDIS_VERIFY_KEY_PREFIX + CommonConstants.REDIS_DAY + ":" + key + "_" + type + "_" + dateString;
        Object allDayResult = cacheService.get(allDayKey);
        if (allDayResult != null && StringUtils.isNotBlank(allDayResult.toString())) {
            Integer integer = Integer.valueOf(allDayResult.toString());
            if (integer > 10) {
                log.error("短信验证码输入错误超过10次,请明天在试");
                throw new BusinessException("短信验证码输入错误超过10次,请明天在试");
            }
        }
    }

    /**
     * 添加验证码次数限制
     */
    public void doMsgSecurit(String ip, String key, String type) {
        // 发送时长是否有一分钟
        String oneMinKey = REDIS_VERIFY_KEY_PREFIX + CommonConstants.REDIS_ONE_MIN + ":" + ip + "_" + key + "_" + type;
        cacheService.set(oneMinKey, key, 1, TimeUnit.MINUTES);

        // 每天一个手机只能发送十次
        String dateString = DateUtils.toDateString(new Date(), DateUtils.YYYY_MM_DD);
        String allDayKey = REDIS_VERIFY_KEY_PREFIX + CommonConstants.REDIS_DAY + ":" + key + "_" + type + "_" + dateString;
        Object allDayResult = cacheService.get(allDayKey);
        if (allDayResult != null && !StringUtils.isEmpty(allDayResult.toString())) {
            cacheService.increment(allDayKey, 1L);
            return;
        }
        cacheService.set(allDayKey, 1L, 1, TimeUnit.DAYS);
    }
}
