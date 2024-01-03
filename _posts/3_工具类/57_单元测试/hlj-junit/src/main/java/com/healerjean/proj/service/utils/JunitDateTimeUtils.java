package com.healerjean.proj.service.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * JunitDateTimeUtils
 *
 * @author zhangyujin
 * @date 2023/12/29
 */
public class JunitDateTimeUtils {


    public static void repairSystemTime(){
        setSystemTime(LocalDateTime.now());
    }

    public static void setSystemTime(LocalDateTime dateTime) {
        // 将LocalDateTime转换为Date
        Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        // 设置系统时间
        System.setProperty("user.timezone", dateTime.atZone(ZoneId.systemDefault()).getZone().getId());
        System.setProperty("sun.calendar.base", String.valueOf(date.getTime()));
    }



}
