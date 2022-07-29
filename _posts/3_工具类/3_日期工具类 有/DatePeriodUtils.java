package com.jd.baoxian.merchant.route.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;


/**
 * 日期区间工具，月、季度、半年 等
 *
 * @author zhangyujin
 * @date 2022/7/19  17:57.
 */
public final class DatePeriodUtils {

    private static final DateTimeFormatter DTF_YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DTF_YYYY_MM = DateTimeFormatter.ofPattern("yyyy-MM");



    /**
     * 获取某年某月的天数
     * @param date 2022-06
     * @return 30
     */
    public static int getMonthDay(String date){
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
        DatePeriod monthRange = getMonthRange(year, month);
        return (int) ChronoUnit.DAYS.between(monthRange.getStartDate(), monthRange.getEndDate()) + 1;
    }




    /**
     * 获取某年某季度的天数
     * @param date 2022-4
     * @return 92
     */
    public static int getQuarterDay(String date){
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
        DatePeriod monthRange = getQuarterRange(year, quarter);
        return (int) ChronoUnit.DAYS.between(monthRange.getStartDate(), monthRange.getEndDate()) + 1;
    }

    /**
     *
     * @param date
     * @return
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
        return (int) ChronoUnit.DAYS.between(monthRange.getStartDate(), monthRange.getEndDate()) + 1;
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
    public static String getTbHalfYea(String date) {
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
    public static String getHbHalfYea(String date) {
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
     * @param year    年
     * @param quarter 前半年或后半年
     * @return DatePeriod
     */
    public static DatePeriod getHalfYearRange(int year, int quarter) {
        LocalDate startDate, endDate;
        switch (quarter) {
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
        return new DatePeriod(startDate.atTime(LocalTime.MIN), endDate.atTime(LocalTime.MAX));
    }


    /**
     * 获取某年月的第一天和最后一天
     *
     * @param year  年
     * @param month 月
     * @return DatePeriod
     */
    public static DatePeriod getMonthRange(int year, int month) {
        LocalDate localDate = LocalDate.of(year, month, 1);
        // 获取当前月的第一天
        LocalDate startDate = localDate.with(TemporalAdjusters.firstDayOfMonth());
        // 获取当前月的最后一天
        LocalDate endDate = localDate.with(TemporalAdjusters.lastDayOfMonth());
        return new DatePeriod(startDate.atTime(LocalTime.MIN), endDate.atTime(LocalTime.MAX));
    }


    /**
     * 获取某年某季度的第一天和最后一天
     *
     * @param year    哪一年
     * @param quarter 第几季度
     * @return DatePeriod
     */
    public static DatePeriod getQuarterRange(int year, int quarter) {
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
        return new DatePeriod(startDate.atTime(LocalTime.MIN), endDate.atTime(LocalTime.MAX));
    }


    static class DatePeriod {
        private LocalDateTime startDate;
        private LocalDateTime endDate;

        public DatePeriod(LocalDateTime startDate, LocalDateTime endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public LocalDateTime getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDateTime startDate) {
            this.startDate = startDate;
        }

        public LocalDateTime getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDateTime endDate) {
            this.endDate = endDate;
        }
    }


}
