package com.healerjean.proj.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

/**
 * DateUtils
 *
 * @author zhangyujin
 * @date 2023/6/14  16:06
 */
public class DateUtils {

    /**
     * YYYYMMDD
     */
    public static final String YYYYMMDD = "yyyyMMdd";
    /**
     * YYYY_MM_dd
     */
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    /**
     * YYYY_MM_DD_HH_MM_SS
     */
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";


    /**
     * toLocalDate
     *
     * @param dateStr dateStr
     * @param format  format
     * @return LocalDate
     */
    public static LocalDate toLocalDate(String dateStr, String format) {
        return Optional.ofNullable(dateStr).map(item -> LocalDate.parse(item, DateTimeFormatter.ofPattern(format))).orElse(null);
    }


    /**
     * toLocalDate
     *
     * @param dateStr dateStr
     * @return LocalDate
     */
    public static LocalDate toLocalDate(String dateStr) {
        return toLocalDate(dateStr, YYYY_MM_DD);
    }

    /**
     * toDateString
     *
     * @param localDate localDate
     * @param format    format
     * @return String
     */
    public static String toDateString(LocalDate localDate, String format) {
        return Optional.ofNullable(localDate).map(item -> DateTimeFormatter.ofPattern(format).format(item)).orElse(null);
    }

    /**
     * toDateString
     *
     * @param localDate localDate
     * @return String
     */
    public static String toDateString(LocalDate localDate) {
        return toDateString(localDate, YYYY_MM_DD);
    }


    /**
     * LocalDateTime
     *
     * @param dateStr dateStr
     * @param format  format
     * @return toLocalDateTime
     */
    public static LocalDateTime toLocalDateTime(String dateStr, String format) {
        return Optional.ofNullable(dateStr).map(item -> LocalDateTime.parse(item, DateTimeFormatter.ofPattern(format))).orElse(null);

    }

    /**
     * LocalDateTime
     *
     * @param dateStr dateStr
     * @return toLocalDateTime
     */
    public static LocalDateTime toLocalDateTime(String dateStr) {
        return toLocalDateTime(dateStr, YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * toDateString
     *
     * @param localDateTime localDateTime
     * @param format        format
     * @return String
     */
    public static String toDateString(LocalDateTime localDateTime, String format) {
        return Optional.ofNullable(localDateTime).map(item -> item.format(DateTimeFormatter.ofPattern(format))).orElse(null);

    }

    /**
     * toDateString
     *
     * @param localDateTime localDateTime
     * @return String
     */
    public static String toDateString(LocalDateTime localDateTime) {
        return toDateString(localDateTime, YYYY_MM_DD_HH_MM_SS);
    }


    /**
     * toDate
     *
     * @param localDateTime localDateTime
     * @return Date
     */
    public static Date toDate(LocalDateTime localDateTime) {
        return Optional.ofNullable(localDateTime).map(item -> Date.from(item.atZone(ZoneId.systemDefault()).toInstant())).orElse(null);
    }

    /**
     * toLocalDateTime
     *
     * @param date date
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return Optional.ofNullable(date).map(item -> item.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()).orElse(null);

    }


    /**
     * toDate
     *
     * @param localDate localDate
     * @return Date
     */
    public static Date toDate(LocalDate localDate) {
        return Optional.ofNullable(localDate).map(item -> Date.from(item.atStartOfDay(ZoneId.systemDefault()).toInstant())).orElse(null);

    }

    /**
     * toLocalDate
     *
     * @param date date
     * @return LocalDate
     */
    public static LocalDate toLocalDate(Date date) {
        return Optional.ofNullable(date).map(item -> item.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).orElse(null);

    }


    /**
     * toTimeInSecond
     *
     * @param localDateTime localDateTime
     * @return Long
     */
    public static Long toTimeInSecond(LocalDateTime localDateTime) {
        return Optional.ofNullable(localDateTime).map(item -> item.atZone(ZoneId.systemDefault()).toEpochSecond()).orElse(null);

    }

    /**
     * 获取今天的起始时间
     *
     * @param localDateTime localDateTime
     * @return LocalDateTime
     */
    public static LocalDateTime getStartDay(LocalDateTime localDateTime) {
        return LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN);
    }

    /**
     * 获取今天的最大时间
     *
     * @param localDateTime localDateTime
     * @return LocalDateTime
     */
    public static LocalDateTime getEndDay(LocalDateTime localDateTime) {
        return LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MAX);
    }
}
