package com.healerjean.proj.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author HealerJean
 * @ClassName DateUtils
 * @date 2020/3/27  16:57.
 * @Description
 */
public class DateUtils {

    public static final String YYYYMMdd = "yyyyMMdd";
    public static final String YYYY_MM_dd = "yyyy-MM-dd";
    public static final String YYYY_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";


    public static LocalDate toLocalDate(String dateStr, String format){
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        LocalDate parse = LocalDate.parse(dateStr , df);
        return  parse;
    }

    public static String toDateString(LocalDate localDate, String format){
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return df.format(localDate);
    }

    public static LocalDateTime toLocalDateTime(String dateStr, String format){
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        LocalDateTime parse = LocalDateTime.parse(dateStr , df);
        return  parse;
    }
    public static String  toDateString(LocalDateTime localDateTime, String format){
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return  localDateTime.format(df);
        //下面这个也可以
        // return df.format(localDateTime);
    }

    public static LocalDateTime localDateToLocalDateTime (LocalDate localDate){
        String dateStr = toDateString(localDate, YYYY_MM_dd);
        LocalDateTime localDateTime = toLocalDateTime(dateStr+" 00:00:00", YYYY_MM_dd_HH_mm_ss);
        return localDateTime ;
    }



    public static Date toDate(LocalDateTime localDateTime){
        ZoneId zoneId = ZoneId.systemDefault() ;
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        return  Date.from(instant);
    }

    public static LocalDateTime toLocalDateTime(Date date){
        ZoneId zoneId = ZoneId.systemDefault() ;
        Instant instant = date.toInstant();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    public static Date toDate(LocalDate localDate){
        ZoneId zoneId = ZoneId.systemDefault() ;
        Instant instant = localDate.atStartOfDay(zoneId).toInstant();
        return  Date.from(instant);
    }
    public static LocalDate toLocalDate(Date date){
        ZoneId zoneId = ZoneId.systemDefault() ;
        Instant instant = date.toInstant();
        return instant.atZone(zoneId).toLocalDate();
    }


    public static LocalDateTime toLocalDateTimeByMills(Long timestamp){
        ZoneId zoneId = ZoneId.systemDefault() ;
        Instant instant = Instant.ofEpochMilli(timestamp);
        return  instant.atZone(zoneId).toLocalDateTime();
    }

    public static Long toTimeInMills(LocalDateTime localDateTime){
        ZoneId zoneId = ZoneId.systemDefault() ;
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        return instant.toEpochMilli() ;
    }

    public static Long toTimeInSecond (LocalDateTime localDateTime){
        ZoneId zoneId = ZoneId.systemDefault() ;
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        return instant.getEpochSecond() ;
    }
}
