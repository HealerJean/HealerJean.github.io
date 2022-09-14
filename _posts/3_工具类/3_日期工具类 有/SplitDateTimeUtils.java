package com.hlj.util.Z012日期.D01Local;

import com.google.common.collect.Lists;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 切割时间工具类
 */
public class SplitDateTimeUtils {


    private static final String START_TIME = "startTime";
    private static final String END_TIME = "endTime";

    private static final String DAY = "DAY";
    private static final String HOUR = "HOUR";
    private static final String MINUTE = "MINUTE";
    private static final String SECOND = "SECOND";


    /**
     * 按天数分割时间段
     *
     * @param startTime 起始时间
     * @param endTime   结束时间
     * @param daysum    分割数
     * @return 返回集合
     */
    public static List<Map<String, LocalDateTime>> splitDateByDay(LocalDateTime startTime, LocalDateTime endTime, long daysum) {
        List<Map<String, LocalDateTime>> result = Lists.newArrayList();
        if (daysum <= 0) {
            return result;
        }
        Duration between = Duration.between(startTime, endTime);
        long betweenDay = between.toDays();


        HashMap<String, LocalDateTime> map;
        if (betweenDay > daysum) {
            for (long i = 0; i <= betweenDay; i = i + daysum + 1) {
                map = new HashMap<>();
                LocalDateTime startDateTime = LocalDateTime.of(startTime.toLocalDate(), LocalTime.MIN).plusDays(i);
                LocalDateTime endDateTime = LocalDateTime.of(startDateTime.plusDays(daysum).toLocalDate(), LocalTime.MAX);
                map.put(START_TIME, startDateTime);
                if (Duration.between(endDateTime, endTime).toDays() < 0) {
                    endDateTime = endTime;
                }
                map.put(END_TIME, endDateTime);
                result.add(map);
            }
        } else if (betweenDay < 0) {
            return result;
        } else {
            commonMethod(startTime, endTime, result);
        }
        return result;
    }


    /**
     * 按小时↑分割时间段
     *
     * @param startTime 起始时间
     * @param endTime   结束时间
     * @param hourSum   分割小时数
     * @return 返回集合
     */

    public static List<Map<String, LocalDateTime>> splitDateByHours(LocalDateTime startTime, LocalDateTime endTime, long hourSum) {
        --hourSum;
        List<Map<String, LocalDateTime>> dayList = null;
        if (hourSum > 0) {
            dayList = new ArrayList<>();
            Duration between = Duration.between(startTime, endTime);
            long betweenHour = between.toHours();
            HashMap<String, LocalDateTime> map;
            if (betweenHour > hourSum) {
                for (long i = 0; i <= betweenHour; i = i + hourSum + 1) {
                    map = new HashMap<>();
                    LocalDateTime startDateTime = LocalDateTime.of(startTime.toLocalDate(), LocalTime.MIN).plusHours(i);
                    LocalDateTime endDateTime = LocalDateTime.of(startTime.toLocalDate(), LocalTime.MIN).plusHours(i + hourSum + 1).minusNanos(1);
                    map.put(START_TIME, startDateTime);
                    if (Duration.between(endDateTime, endTime).toDays() < 0) {
                        endDateTime = endTime;
                    }
                    map.put(END_TIME, endDateTime);
                    dayList.add(map);
                }
            } else if (betweenHour < 0) {
                return dayList;
            } else {
                commonMethod(startTime, endTime, dayList);
            }
        }
        return dayList;
    }

    /**
     * 按分钟↑分割时间段
     *
     * @param startTime 起始时间
     * @param endTime   结束时间
     * @param minuteSum 分割分钟数
     * @return 时段端
     */
    public static List<Map<String, LocalDateTime>> splitDateByMinute(LocalDateTime startTime, LocalDateTime endTime, long minuteSum) {
        --minuteSum;
        List<Map<String, LocalDateTime>> dayList = null;
        if (minuteSum > 0) {
            dayList = new ArrayList<>();
            Duration between = Duration.between(startTime, endTime);
            long minutes = between.toMinutes();
            HashMap<String, LocalDateTime> map;
            if (minutes > minuteSum) {
                for (long i = 0; i <= minutes; i = i + minuteSum + 1) {
                    map = new HashMap<>();
                    LocalDateTime startDateTime = LocalDateTime.of(startTime.toLocalDate(), LocalTime.MIN).plusMinutes(i);
                    LocalDateTime endDateTime = LocalDateTime.of(startTime.toLocalDate(), LocalTime.MIN).plusMinutes(i + minuteSum + 1).minusNanos(1);
                    map.put(START_TIME, startDateTime);
                    if (Duration.between(endDateTime, endTime).toMinutes() < 0) {
                        endDateTime = endTime;
                    }
                    map.put(END_TIME, endDateTime);
                    dayList.add(map);
                }
            } else if (minutes < 0) {
                return dayList;
            } else {
                commonMethod(startTime, endTime, dayList);
            }
        }
        return dayList;
    }

    /**
     * 按秒↑分割时间段
     *
     * @param startTime 起始时间
     * @param endTime   结束时间
     * @param secondSum 分割分钟数
     * @return 时段端
     */
    public static List<Map<String, LocalDateTime>> splitDateBySecond(LocalDateTime startTime, LocalDateTime endTime, long secondSum) {
        --secondSum;
        List<Map<String, LocalDateTime>> dayList = null;
        if (secondSum > 0) {
            dayList = new ArrayList<>();
            Duration between = Duration.between(startTime, endTime);
            long seconds = between.toMillis() / 1000;
            HashMap<String, LocalDateTime> map;
            if (seconds > secondSum) {
                for (long i = 0; i <= seconds; i = i + secondSum + 1) {
                    map = new HashMap<>();
                    LocalDateTime startDateTime = LocalDateTime.of(startTime.toLocalDate(), LocalTime.MIN).plusSeconds(i);
                    LocalDateTime endDateTime = LocalDateTime.of(startTime.toLocalDate(), LocalTime.MIN).plusSeconds(i + secondSum + 1).minusNanos(1);
                    map.put(START_TIME, startDateTime);
                    if (Duration.between(endDateTime, endTime).toMillis() < 0) {
                        endDateTime = endTime;
                    }
                    map.put(END_TIME, endDateTime);
                    dayList.add(map);
                }
            } else if (seconds < 0) {
                return dayList;
            } else {
                commonMethod(startTime, endTime, dayList);
            }
        }
        return dayList;
    }

    private static void commonMethod(LocalDateTime startTime, LocalDateTime endTime, List<Map<String, LocalDateTime>> dayList) {
        HashMap<String, LocalDateTime> map = new HashMap<>(4);
        map.put(START_TIME, startTime);
        map.put(END_TIME, endTime);
        dayList.add(map);
    }

    public static void main(String[] args) {
        LocalDateTime startTime = LocalDateTime.parse("2020-06-12 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endTime = LocalDateTime.parse("2020-06-12 13:01:58", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        List<Map<String, LocalDateTime>> mapList = splitDateByMinute(startTime, endTime, 60);
        mapList.forEach(e -> System.out.println(e.get(START_TIME) + "--->" + e.get(END_TIME)));
    }
}

