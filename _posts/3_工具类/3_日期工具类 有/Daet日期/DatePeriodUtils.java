package com.jd.baoxian.merchant.route.common.utils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;


/**
 * 日期区间工具，月、季度、半年 等
 *
 * @author zhangyujin
 * @date 2022/7/19  17:57.
 */
public final class DatePeriodUtils {
    public static final String YYYY_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter DTF_YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DTF_YYYY_MM = DateTimeFormatter.ofPattern("yyyy-MM");

    /**
     * 1、获取固定一年前的起始时间
     * 2、获取当前时间所在月份的最后一天
     *
     * @param date 日期如2022-05、2022-12 、2022-12-01
     * @return DatePeriod
     */
    public static DatePeriod getBackOneYearRange(String date) {
        String[] split = date.split("-");
        int year = Integer.parseInt(split[0]);
        int month = Integer.parseInt(split[1]);
        int halfPeripd = 1;
        if (month > 6) {
            halfPeripd = 2;
        }
        int lastYear = year - 1;
        String startTime = getHalfYearDayRangeStartTime(lastYear, halfPeripd);
        String endTime = getHalfYearDayRangeEndTime(year, halfPeripd);
        return new DatePeriod(startTime, endTime);
    }

    /**
     * 获取一段期间所在的最长半年周期
     *
     * @param startDate 起始时间 2022-01
     * @param endDate   结束时间
     * @return DatePeriod
     */
    public static DatePeriod getBackOneYearRange(String startDate, String endDate) {
        String[] startSplit = startDate.split("-");
        int startYear = Integer.parseInt(startSplit[0]);
        int startMonth = Integer.parseInt(startSplit[1]);
        int startHalfPeripd = 1;
        if (startMonth > 6) {
            startHalfPeripd = 2;
        }
        String startTime = getHalfYearDayRangeStartTime(startYear, startHalfPeripd);

        String[] endSplit = endDate.split("-");
        int endYear = Integer.parseInt(endSplit[0]);
        int endMonth = Integer.parseInt(endSplit[1]);
        int endHalfPeripd = 1;
        if (endMonth > 6) {
            endHalfPeripd = 2;
        }
        String endTime = getHalfYearDayRangeEndTime(endYear, endHalfPeripd);
        return new DatePeriod(startTime, endTime);
    }


    /**
     * 获取某年某月的天数
     *
     * @param date yyyy-MM 如：2022-06
     * @return  30
     */
    public static int getMonthDay(String date) {
        String[] split = date.split("-");
        return getMonthDay(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }

    /**
     * 获取某年某月的天数
     *
     * @param year  年
     * @param month 月份
     * @return 天数
     */
    public static int getMonthDay(int year, int month) {
        DatePeriod monthRange = getMonthDayRange(year, month);
        return (int) ChronoUnit.DAYS.between(toLocalDateTime(monthRange.getStartDateTime()), toLocalDateTime(monthRange.getEndDateTime())) + 1;
    }


    /**
     * 获取某年某季度的天数
     *
     * @param date  2022-4
     * @return 92
     */
    public static int getQuarterDay(String date) {
        String[] split = date.split("-");
        return getQuarterDay(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }

    /**
     * 获取某年某季度的天数
     *
     * @param year    年
     * @param quarter 季度
     * @return 天数
     */
    public static int getQuarterDay(int year, int quarter) {
        DatePeriod monthRange = getQuarterDayRange(year, quarter);
        return (int) ChronoUnit.DAYS.between(toLocalDateTime(monthRange.getStartDateTime()), toLocalDateTime(monthRange.getEndDateTime())) + 1;
    }

    /**
     * 获取某半年的天数
     *
     * @param date 日期
     * @return 天数
     */
    public static int getHalfYearDay(String date) {
        String[] split = date.split("-");
        return getHalfYearDay(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }

    /**
     * 获取某半年天数
     *
     * @param year    年
     * @param quarter 前半年或后半年
     * @return 天数
     */
    public static int getHalfYearDay(int year, int quarter) {
        DatePeriod monthRange = getHalfYearRange(year, quarter);
        return (int) ChronoUnit.DAYS.between(toLocalDateTime(monthRange.getStartDateTime()), toLocalDateTime(monthRange.getEndDateTime())) + 1;
    }


    /**
     * 计算环比月
     *
     * @param currMonth 2019-01
     * @return 2018-12
     */
    public static String getHbMonth(String currMonth) {
        currMonth = currMonth + "-01";
        LocalDate d = LocalDate.parse(currMonth, DTF_YYYY_MM_DD);
        return DTF_YYYY_MM.format(d.minusMonths(1L));
    }


    /**
     * 计算同比月
     *
     * @param currMonth 2019-01
     * @return 2018-01
     */
    public static String getTbMonth(String currMonth) {
        currMonth = currMonth + "-01";
        LocalDate d = LocalDate.parse(currMonth, DTF_YYYY_MM_DD);
        return DTF_YYYY_MM.format(d.minusYears(1L));
    }


    /**
     * 计算同比季度（去年同期）
     * date 2022-4
     *
     * @return 2021-4
     */
    public static String getTbQuarter(String date) {
        String[] dateArray = date.split("-");
        int year = Integer.parseInt(dateArray[0]) - 1;
        return year + "-" + dateArray[1];
    }


    /**
     * 计算环比季度
     *
     * @param date 2019-1
     * @return 2019-1 -> 2018-4 、 2019-2 -> 2019-1
     */
    public static String getHbQuarter(String date) {
        String[] dateArray = date.split("-");
        int quarter = Integer.parseInt(dateArray[1]) - 1;
        if (quarter > 0) {
            return dateArray[0] + "-" + quarter;
        }

        int year = Integer.parseInt(dateArray[0]) - 1;
        return year + "-" + 4;
    }


    /**
     * 计算同比 半年
     * date 2022-1
     *
     * @return 2021-1
     */
    public static String getTbHalfYear(String date) {
        String[] dateArray = date.split("-");
        int year = Integer.parseInt(dateArray[0]) - 1;
        return year + "-" + dateArray[1];
    }


    /**
     * 计算环比-半年
     * date 2022-1
     *
     * @return 2021-1
     */
    public static String getHbHalfYear(String date) {
        String[] dateArray = date.split("-");
        int num = Integer.parseInt(dateArray[1]) - 1;
        if (num > 0) {
            return dateArray[0] + "-" + num;
        }
        int year = Integer.parseInt(dateArray[0]) - 1;
        return year + "-" + 2;
    }


    /**
     * 获取半年起始和结束时间
     *
     * @param year   年
     * @param period 1 前半年 2 后半年
     * @return DatePeriod
     */
    public static DatePeriod getHalfYearRange(int year, int period) {
        LocalDate startDate, endDate;
        switch (period) {
            case 1:
                // 01.01-06.30
                startDate = LocalDate.of(year, 1, 1);
                endDate = LocalDate.of(year, 6, 30);
                break;
            case 2:
                // 07.01-12.31
                startDate = LocalDate.of(year, 7, 1);
                endDate = LocalDate.of(year, 12, 31);
                break;
            default:
                throw new RuntimeException("quarter range [1-4]");
        }
        return new DatePeriod(toDateString(startDate.atTime(LocalTime.MIN)), toDateString(endDate.atTime(LocalTime.MAX)));
    }


    /**
     * 获取某半年的起始时间
     *
     * @param year   年
     * @param period 周期 1 前半年 2 后半年
     * @return
     */
    public static String getHalfYearDayRangeStartTime(int year, int period) {
        LocalDate startDate;
        switch (period) {
            case 1:
                // 01.01-06.30
                startDate = LocalDate.of(year, 1, 1);
                break;
            case 2:
                // 07.01-12.31
                startDate = LocalDate.of(year, 7, 1);
                break;
            default:
                throw new RuntimeException("quarter range [1-4]");
        }
        return toDateString(startDate.atTime(LocalTime.MIN));
    }


    /**
     * 获取半年结束时间
     *
     * @param year   年
     * @param period 1 前半年 2 后半年
     * @return DatePeriod
     */
    public static String getHalfYearDayRangeEndTime(int year, int period) {
        LocalDate endDate;
        switch (period) {
            case 1:
                // 01.01-06.30
                endDate = LocalDate.of(year, 6, 30);
                break;
            case 2:
                // 07.01-12.31
                endDate = LocalDate.of(year, 12, 31);
                break;
            default:
                throw new RuntimeException("quarter range [1-4]");
        }
        return toDateString(endDate.atTime(LocalTime.MAX));
    }


    /**
     * 获取某年月的第一天和最后一天
     *
     * @param year  年
     * @param month 月
     * @return DatePeriod
     */
    public static DatePeriod getMonthDayRange(int year, int month) {
        LocalDate localDate = LocalDate.of(year, month, 1);
        // 获取当前月的第一天
        LocalDate startDate = localDate.with(TemporalAdjusters.firstDayOfMonth());
        // 获取当前月的最后一天
        LocalDate endDate = localDate.with(TemporalAdjusters.lastDayOfMonth());
        return new DatePeriod(toDateString(startDate.atTime(LocalTime.MIN)), toDateString(endDate.atTime(LocalTime.MAX)));
    }


    /**
     * 获取某月的第一天
     *
     * @param year  年
     * @param month 月
     * @return 某月的第一天
     */
    public static String getMonthDayRangeStartTime(int year, int month) {
        LocalDate localDate = LocalDate.of(year, month, 1);
        // 获取当前月的第一天
        LocalDate startDate = localDate.with(TemporalAdjusters.firstDayOfMonth());
        return toDateString(startDate.atTime(LocalTime.MIN));
    }

    /**
     * 获取某月的最后一天
     *
     * @param year  年
     * @param month 月
     * @return 某月的最后一天
     */
    public static String getMonthDayRangeEndTime(int year, int month) {
        LocalDate localDate = LocalDate.of(year, month, 1);
        // 获取当前月的最后一天
        LocalDate endDate = localDate.with(TemporalAdjusters.lastDayOfMonth());
        return toDateString(endDate.atTime(LocalTime.MAX));
    }



    /**
     * 获取某年某季度的第一天和最后一天
     *
     * @param year    哪一年
     * @param quarter 第几季度
     * @return DatePeriod
     */
    public static DatePeriod getQuarterDayRange(int year, int quarter) {
        LocalDate startDate, endDate;
        switch (quarter) {
            case 1:
                // 01.01-03.31
                startDate = LocalDate.of(year, 1, 1);
                endDate = LocalDate.of(year, 3, 31);
                break;
            case 2:
                // 04.01-06.30
                startDate = LocalDate.of(year, 4, 1);
                endDate = LocalDate.of(year, 6, 30);
                break;
            case 3:
                // 07.01-09.30
                startDate = LocalDate.of(year, 7, 1);
                endDate = LocalDate.of(year, 9, 30);
                break;
            case 4:
                // 10.01-12.31
                startDate = LocalDate.of(year, 10, 1);
                endDate = LocalDate.of(year, 12, 31);
                break;
            default:
                throw new RuntimeException("quarter range [1-4]");
        }
        return new DatePeriod(toDateString(startDate.atTime(LocalTime.MIN)), toDateString(endDate.atTime(LocalTime.MAX)));
    }


    /**
     * 获取包含日期所在月份的所有月
     *
     * @param startDateTime 起始时间
     * @param endDateTime   结束时间
     * @return 月份集合 如: 2022-06、2022-07
     */
    public static List<String> getMonthList(String startDateTime, String endDateTime) {
        LocalDateTime startDateTimeLocal = toLocalDateTime(startDateTime);
        LocalDateTime endDateTimeLocal = toLocalDateTime(endDateTime);

        List<String> result = Lists.newArrayList();
        long distance = ChronoUnit.MONTHS.between(startDateTimeLocal, endDateTimeLocal);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM");
        Stream.iterate(startDateTimeLocal, d -> d.plusMonths(1)).limit(distance + 1).forEach(f -> result.add(df.format(f)));
        Collections.sort(result);
        return result;
    }

    /**
     * 获取季度集合
     *
     * @param startDateTime 起始时间
     * @param endDateTime   结束时间
     * @return 季度集合 如: 2022-01、2022-02
     */
    public static List<String> getQuarterList(String startDateTime, String endDateTime) {
        List<String> monthList = getMonthList(startDateTime, endDateTime);
        List<String> result = Lists.newArrayList();
        if (CollectionUtils.isEmpty(monthList)) {
            return result;
        }
        Set<String> monthSet = Sets.newHashSet();
        List<String> oneQuarter = Lists.newArrayList("01", "02", "03");
        List<String> twoQuarter = Lists.newArrayList("04", "05", "06");
        List<String> threeQuarter = Lists.newArrayList("07", "08", "09");
        List<String> fourQuarter = Lists.newArrayList("10", "11", "12");
        for (String month : monthList) {
            String[] split = month.split("-");
            String y = split[0];
            String p = split[1];
            if (oneQuarter.contains(p)) {
                monthSet.add(y + "-1");
            }
            if (twoQuarter.contains(p)) {
                monthSet.add(y + "-2");
            }
            if (threeQuarter.contains(p)) {
                monthSet.add(y + "-3");
            }
            if (fourQuarter.contains(p)) {
                monthSet.add(y + "-4");
            }
        }

        result.addAll(monthSet);
        Collections.sort(result);
        return result;
    }


    /**
     * 获取半年集合
     *
     * @param startDateTime 起始时间
     * @param endDateTime   结束时间
     * @return 半年集合 如: 2022-1、2022-2
     */
    public static List<String> getHalfYearList(String startDateTime, String endDateTime) {
        List<String> monthList = getMonthList(startDateTime, endDateTime);
        List<String> result = Lists.newArrayList();
        if (CollectionUtils.isEmpty(monthList)) {
            return result;
        }
        Set<String> monthSet = Sets.newHashSet();
        List<String> firstYear = Lists.newArrayList("01", "02", "03", "04", "05", "06");
        List<String> lastYear = Lists.newArrayList("07", "08", "09", "10", "11", "12");
        for (String month : monthList) {
            String[] split = month.split("-");
            String y = split[0];
            String p = split[1];
            if (firstYear.contains(p)) {
                monthSet.add(y + "-1");
            }
            if (lastYear.contains(p)) {
                monthSet.add(y + "-2");
            }
        }
        result.addAll(monthSet);
        Collections.sort(result);
        return result;
    }



    /**
     * 获取半年集合
     * @param month 2022-01
     * @return  半年集合
     */
    public static String getQuarter(String month){
        return getQuarterList(month + "-01 00:00:00", month + "-01 00:00:00").get(0);
    }


    /**
     * 获取半年集合
     * @param month 2022-01
     * @return  半年集合
     */
    public static String getHalfYear(String month){
        return getHalfYearList(month + "-01 00:00:00", month + "-01 00:00:00").get(0);
    }



    /**
     * 根据半年周期获取季度周期
     * @param halfYearList 半年周期
     * @return 季度周期
     */
    public static List<String> getQuarterList(List<String> halfYearList) {
        if (CollectionUtils.isEmpty(halfYearList)) {
            return Lists.newArrayList();
        }
        List<String> result = new ArrayList<>();
        for (String halfYear : halfYearList) {
            int y = Integer.parseInt(halfYear.split("-")[0]);
            int p = Integer.parseInt(halfYear.split("-")[1]);
            DatePeriod halfYearRange = getHalfYearRange(y, p);
            List<String> quarterList = getQuarterList(halfYearRange.getStartDateTime(), halfYearRange.getEndDateTime());
            result.addAll(quarterList);
        }
        return result;
    }

    /**
     * 根据季度周期获取月份周期
     * @param quarterList 季度周期
     * @return 月份周期
     */
    public static List<String> getMonthList(List<String> quarterList) {
        if (CollectionUtils.isEmpty(quarterList)) {
            return Lists.newArrayList();
        }
        List<String> result = new ArrayList<>();
        for (String quarter : quarterList) {
            int y = Integer.parseInt(quarter.split("-")[0]);
            int p = Integer.parseInt(quarter.split("-")[1]);
            DatePeriod halfYearRange = getQuarterDayRange(y, p);
            List<String> monthList = getMonthList(halfYearRange.getStartDateTime(), halfYearRange.getEndDateTime());
            result.addAll(monthList);
        }
        return result;
    }

    public static LocalDateTime toLocalDateTime(String dateStr) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(YYYY_MM_dd_HH_mm_ss);
        LocalDateTime parse = LocalDateTime.parse(dateStr, df);
        return parse;
    }

    public static String toDateString(LocalDateTime localDateTime) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(YYYY_MM_dd_HH_mm_ss);
        return df.format(localDateTime);
    }

    public static class DatePeriod implements Serializable {
        private static final long serialVersionUID = 7704935152782849727L;
        private String startDateTime;
        private String endDateTime;
        private String monthStart;
        private String monthEnd;

        public DatePeriod() {
        }

        public DatePeriod(String startDateTime, String endDateTime) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startLocalDateTime = LocalDateTime.parse(startDateTime, dateTimeFormatter);
            LocalDateTime endDateLocalDateTime = LocalDateTime.parse(endDateTime, dateTimeFormatter);

            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM");
            this.startDateTime = startDateTime;
            this.endDateTime = endDateTime;
            this.monthStart = df.format(startLocalDateTime);
            this.monthEnd = df.format(endDateLocalDateTime);
        }

        public static long getSerialVersionUID() {
            return serialVersionUID;
        }

        public String getStartDateTime() {
            return startDateTime;
        }

        public void setStartDateTime(String startDateTime) {
            this.startDateTime = startDateTime;
        }

        public String getEndDateTime() {
            return endDateTime;
        }

        public void setEndDateTime(String endDateTime) {
            this.endDateTime = endDateTime;
        }

        public String getMonthStart() {
            return monthStart;
        }

        public void setMonthStart(String monthStart) {
            this.monthStart = monthStart;
        }

        public String getMonthEnd() {
            return monthEnd;
        }

        public void setMonthEnd(String monthEnd) {
            this.monthEnd = monthEnd;
        }
    }


 /**
     * YYYY_MM_DD
     */
    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * 获取两个日期之间的所有日期
     *
     * @param pattern   日期格式
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return Set<String>
     */
    public static List<String> getDates(String pattern, Date startDate, Date endDate) {
        List<String> result = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        startCalendar.setTime(startDate);
        endCalendar.setTime(endDate);
        result.add(df.format(startCalendar.getTime()));
        while (true) {
            startCalendar.add(Calendar.DAY_OF_MONTH, 1);
            if (startCalendar.getTimeInMillis() <= endCalendar.getTimeInMillis()) {
                result.add(df.format(startCalendar.getTime()));
            } else {
                break;
            }
        }
        return result;
    }


}
