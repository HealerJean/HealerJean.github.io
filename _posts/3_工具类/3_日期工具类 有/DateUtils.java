package com.healerjean.proj.utils.date;

import com.google.common.collect.Lists;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Stream;

import static com.healerjean.proj.utils.date.DateUtils.TimeTool.toLocalDateTime;

/**
 * DateUtils
 *
 * @author zhangyujin
 * @date 2023/6/14  16:06
 */
public class DateUtils {

    public static class TimeTool {

        /**
         * toDateString
         *
         * @param localDate localDate
         * @return String
         */
        public static String toDateString(LocalDate localDate) {
            return toDateString(localDate, DateConstants.YYYY_MM_DD);
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
         * @param localDateTime localDateTime
         * @return String
         */
        public static String toDateString(LocalDateTime localDateTime) {
            return toDateString(localDateTime, DateConstants.YYYY_MM_DD_HH_MM_SS);
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
         * toLocalDate
         *
         * @param dateStr dateStr
         * @return LocalDate
         */
        public static LocalDate toLocalDate(String dateStr) {
            return toLocalDate(dateStr, DateConstants.YYYY_MM_DD);
        }


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
         * @param date date
         * @return LocalDate
         */
        public static LocalDate toLocalDate(Date date) {
            return Optional.ofNullable(date).map(item -> item.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).orElse(null);
        }

        /**
         * LocalDateTime
         *
         * @param dateStr dateStr
         * @return toLocalDateTime
         */
        public static LocalDateTime toLocalDateTime(String dateStr) {
            return toLocalDateTime(dateStr, DateConstants.YYYY_MM_DD_HH_MM_SS);
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
         * @param localDateTime localDateTime
         * @return Date
         */
        public static Date toDate(LocalDateTime localDateTime) {
            return Optional.ofNullable(localDateTime).map(item -> Date.from(item.atZone(ZoneId.systemDefault()).toInstant())).orElse(null);
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

    }


    public static class TimeExtTool {



        /**
         * 获取开始时间
         *
         * @param localDateTime localDateTime
         * @return LocalDateTime
         */
        public static LocalDateTime getFirstTime(LocalDateTime localDateTime, ChronoUnit chronoUnit) {
            switch (chronoUnit) {
                case MINUTES:
                    return localDateTime.withSecond(0).withNano(0);
                case HOURS:
                    return localDateTime.withMinute(0).withSecond(0).withNano(0);
                case DAYS:
                    return localDateTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
                case MONTHS:
                    return localDateTime.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
                case YEARS:
                    return localDateTime.withDayOfYear(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
                default:
                    throw new IllegalArgumentException("field 无日期匹配类型");
            }
        }

        /**
         * 获取结束时间
         *
         * @param localDateTime localDateTime
         * @return LocalDateTime
         */
        public static LocalDateTime getEndTime(LocalDateTime localDateTime, ChronoUnit chronoUnit) {
            switch (chronoUnit) {
                case MINUTES:
                    return localDateTime.withSecond(59).withNano(0);
                case HOURS:
                    return localDateTime.withMinute(59).withSecond(59).withNano(0);
                case DAYS:
                    return localDateTime.withHour(23).withMinute(59).withSecond(59).withNano(0);
                case MONTHS:
                    return localDateTime.withDayOfMonth(localDateTime.getMonth().length(Year.isLeap(localDateTime.getYear()))).withHour(23).withMinute(59).withSecond(59).withNano(0);
                case YEARS:
                    return localDateTime.with(TemporalAdjusters.lastDayOfYear()).with(LocalTime.MAX);
                default:
                    throw new IllegalArgumentException("field 无日期匹配类型");
            }
        }

        /**
         * roll
         *
         * @param localDateTime localDateTime
         * @param field         field
         * @param num           num
         * @return {@link LocalDateTime}
         */
        public static LocalDateTime minusRoll(LocalDateTime localDateTime, ChronoUnit field, int num) {
            return localDateTime.minus(num, field);
        }


        /**
         * roll
         *
         * @param localDateTime localDateTime
         * @param field         field
         * @param num           num
         * @return {@link LocalDateTime}
         */
        public static LocalDateTime plusRoll(LocalDateTime localDateTime, ChronoUnit field, int num) {
            return localDateTime.plus(num, field);
        }

        /**
         * 获取包含日期所在月份的所有月
         *
         * @param startDateTime 起始时间
         * @param endDateTime   结束时间
         * @return 月份集合 如: 2022-06、2022-07
         */
        public static List<String> getDates(String startDateTime, String endDateTime, ChronoUnit chronoUnit, String format) {
            LocalDateTime startDateTimeLocal = toLocalDateTime(startDateTime);
            LocalDateTime endDateTimeLocal = toLocalDateTime(endDateTime);
            List<String> result = Lists.newArrayList();
            long distance = chronoUnit.between(startDateTimeLocal, endDateTimeLocal);
            DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
            switch (chronoUnit) {
                case MINUTES:
                    Stream.iterate(startDateTimeLocal, d -> d.plusMinutes(1)).limit(distance + 1).forEach(f -> result.add(df.format(f)));
                    break;
                case HOURS:
                   Stream.iterate(startDateTimeLocal, d -> d.plusHours(1)).limit(distance + 1).forEach(f -> result.add(df.format(f)));
                    break;
                case DAYS:
                    Stream.iterate(startDateTimeLocal, d -> d.plusDays(1)).limit(distance + 1).forEach(f -> result.add(df.format(f)));
                    break;
                case MONTHS:
                    Stream.iterate(startDateTimeLocal, d -> d.plusMonths(1)).limit(distance + 1).forEach(f -> result.add(df.format(f)));
                    break;
                case YEARS:
                    Stream.iterate(startDateTimeLocal, d -> d.plusYears(1)).limit(distance + 1).forEach(f -> result.add(df.format(f)));
                    break;
                default:
                    throw new IllegalArgumentException("field 无日期匹配类型");
            }
            Collections.sort(result);
            return result;
        }


        public static void main(String[] args) {
            // TimeExtTool.getFirstTime
            LocalDateTime dateTime = LocalDateTime.of(2023, 10, 23, 20, 23, 21); // 示例日期时间
            System.out.println("TimeExtTool.getFirstTime" + DateUtils.TimeTool.toDateString(dateTime));
            System.out.println(DateUtils.TimeTool.toDateString(getFirstTime(dateTime, ChronoUnit.MINUTES)));
            System.out.println(DateUtils.TimeTool.toDateString(getFirstTime(dateTime, ChronoUnit.HOURS)));
            System.out.println(DateUtils.TimeTool.toDateString(getFirstTime(dateTime, ChronoUnit.DAYS)));
            System.out.println(DateUtils.TimeTool.toDateString(getFirstTime(dateTime, ChronoUnit.MONTHS)));
            System.out.println(DateUtils.TimeTool.toDateString(getFirstTime(dateTime, ChronoUnit.YEARS)));
            System.out.println();
            System.out.println("TimeExtTool.getFirstTime" + DateUtils.TimeTool.toDateString(dateTime));
            System.out.println(DateUtils.TimeTool.toDateString(dateTime));
            System.out.println(DateUtils.TimeTool.toDateString(getEndTime(dateTime, ChronoUnit.MINUTES)));
            System.out.println(DateUtils.TimeTool.toDateString(getEndTime(dateTime, ChronoUnit.HOURS)));
            System.out.println(DateUtils.TimeTool.toDateString(getEndTime(dateTime, ChronoUnit.DAYS)));
            System.out.println(DateUtils.TimeTool.toDateString(getEndTime(dateTime, ChronoUnit.MONTHS)));
            System.out.println(DateUtils.TimeTool.toDateString(getEndTime(dateTime, ChronoUnit.YEARS)));
            System.out.println();
            System.out.println("TimeExtTool.minusRoll" + DateUtils.TimeTool.toDateString(dateTime));
            System.out.println(DateUtils.TimeTool.toDateString(minusRoll(dateTime, ChronoUnit.DAYS, 1)));
            System.out.println();
            System.out.println("TimeExtTool.plusRoll" + DateUtils.TimeTool.toDateString(dateTime));
            System.out.println(DateUtils.TimeTool.toDateString(plusRoll(dateTime, ChronoUnit.DAYS, 1)));

            System.out.println();
            System.out.println("TimeExtTool.getDates" + DateUtils.TimeTool.toDateString(dateTime));
            LocalDateTime startDate = LocalDateTime.of(2023, 10, 23, 20, 23, 21); // 示例日期时间
            LocalDateTime endDate = LocalDateTime.of(2023, 11, 23, 20, 23, 21); // 示例日期时间
            System.out.println(getDates(TimeTool.toDateString(startDate), TimeTool.toDateString(endDate), ChronoUnit.DAYS, DateConstants.YYYY_MM_DD));
            System.out.println(getDates(TimeTool.toDateString(startDate), TimeTool.toDateString(endDate), ChronoUnit.MONTHS, DateConstants.YYYY_MM));
        }

    }


    public static class DateTool {

        /**
         * toDateString
         *
         * @param date    date
         * @param pattern pattern
         * @return {@link String}
         */
        public static String toDateString(Date date, String pattern) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            return dateFormat.format(date);
        }


        /**
         * toDate
         *
         * @param dateStr dateStr
         * @param pattern pattern
         * @return {@link Date}
         */
        public static Date toDate(String dateStr, String pattern) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            try {
                return dateFormat.parse(dateStr);
            } catch (ParseException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }


        /**
         * longToDate
         *
         * @param longDate longDate
         * @return {@link Date}
         */
        public static Date longToDate(Long longDate) {
            if (longDate == null) {
                return null;
            }
            return new Date(longDate);
        }

    }


    public static class DateExtTool {

        /**
         * getFirstTime
         *
         * @return Date
         */
        public static Date getFirstTime(Date date, int field) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            switch (field) {
                //1、获取小时的最初时间
                case Calendar.HOUR:
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    return cal.getTime();
                case Calendar.DATE:
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    return cal.getTime();
                case Calendar.MONTH:
                    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    return cal.getTime();
                case Calendar.YEAR:
                    cal.set(Calendar.MONTH, Calendar.JANUARY);
                    cal.set(Calendar.DAY_OF_YEAR, cal.getActualMinimum(Calendar.DAY_OF_YEAR));
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    return cal.getTime();
                default:
                    throw new RuntimeException("field 无日期匹配类型");
            }
        }


        /**
         * getEndTime
         *
         * @return Date
         */
        public static Date getEndTime(Date date, int field) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            switch (field) {
                //1、获取小时的最后时间
                case Calendar.HOUR:
                    cal.set(Calendar.MINUTE, 59);
                    cal.set(Calendar.SECOND, 59);
                    cal.set(Calendar.MILLISECOND, 0);
                    return cal.getTime();
                case Calendar.DATE:
                    cal.set(Calendar.HOUR_OF_DAY, 23);
                    cal.set(Calendar.MINUTE, 59);
                    cal.set(Calendar.SECOND, 59);
                    cal.set(Calendar.MILLISECOND, 0);
                    return cal.getTime();
                case Calendar.MONTH:
                    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                    cal.set(Calendar.HOUR_OF_DAY, 23);
                    cal.set(Calendar.MINUTE, 59);
                    cal.set(Calendar.SECOND, 59);
                    cal.set(Calendar.MILLISECOND, 0);
                    return cal.getTime();
                case Calendar.YEAR:
                    cal.set(Calendar.MONTH, Calendar.DECEMBER);
                    cal.set(Calendar.DAY_OF_YEAR, cal.getActualMaximum(Calendar.DAY_OF_YEAR));
                    cal.set(Calendar.HOUR_OF_DAY, 23);
                    cal.set(Calendar.MINUTE, 59);
                    cal.set(Calendar.SECOND, 59);
                    cal.set(Calendar.MILLISECOND, 0);
                    return cal.getTime();
                default:
                    throw new RuntimeException("field 无日期匹配类型");
            }
        }


        /**
         * roll
         *
         * @param date date
         * @param field field
         * @param num num
         * @return {@link Date}
         */
        public static Date roll(Date date, int field, int num) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(field, num);
            return cal.getTime();
        }




    }
}
