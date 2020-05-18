package com.healerjean.proj.utils;

import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {

    public static final String MM = "MM";
    public static final String YYYYMM = "yyyyMM";
    public static final String YYYYMMdd = "yyyyMMdd";
    public static final String YYYY_MM_dd = "yyyy-MM-dd";
    public static final String YYYY_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYYMMddHHmmss = "yyyyMMddHHmmss";


    public static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static Date toDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay(zone).toInstant();
        return Date.from(instant);
    }

    public static LocalDateTime toLocalDateTime(String dateStr, String format) {
        if (StringUtils.isBlank(dateStr) || StringUtils.isBlank(format)) {
            return null;
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        LocalDateTime parse = LocalDateTime.parse(dateStr, df);
        return parse;
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    public static String toDateString(LocalDate localDate, String format) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return df.format(localDate);
    }

    public static String toDateTimeString(LocalDateTime localDateTime, String format) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return df.format(localDateTime);
    }

    public static LocalDate toLocalDate(String dateStr, String format) {
        if (StringUtils.isBlank(dateStr) || StringUtils.isBlank(format)) {
            return null;
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        LocalDate parse = LocalDate.parse(dateStr, df);
        return parse;
    }

    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDate();
    }

    public static Date toDate(String dateStr, String format) {
        if (StringUtils.isBlank(dateStr) || StringUtils.isBlank(format)) {
            return null;
        }
        LocalDate localDate = toLocalDate(dateStr, format);
        return toDate(localDate);
    }




}
