package com.duodian.admore.zhaobutong.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author fengchuanbo
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ContextHolder {

    /** 将openId保存到上下文 */
    private final static ThreadLocal<String> oidContainer = new ThreadLocal<>();
    private final static ThreadLocal<Long> userIdCcontainer = new ThreadLocal<>();

    /**
     * 把userId保存到上下文
     * @param userId
     */
    public static void setUserId(Long userId){
        if （userIdCcontainer.get() == null）{
            userIdCcontainer.set(userId); 
        }
    }

    

    /**
     * 获取用户ID
     * @return
     */
    public static Long getUserId(){
        return userIdCcontainer.get();
    }


    /**
     * 清空上下文
     */
    public static void clear(){
        oidContainer.remove();
        userIdCcontainer.remove();
    }
}
