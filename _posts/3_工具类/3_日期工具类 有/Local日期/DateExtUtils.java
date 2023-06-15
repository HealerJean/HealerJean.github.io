package com.healerjean.proj.utils.date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 切割时间工具类
 */
public class DateExtUtils {


    private static final String START_TIME = "startTime";
    private static final String END_TIME = "endTime";

    private static final String DAY = "DAY";
    private static final String HOUR = "HOUR";
    private static final String MINUTE = "MINUTE";
    private static final String SECOND = "SECOND";


    /**
     * 获取两个日期直接所有的日期构成集合
     *
     * @param startDate startDate
     * @param endDate   endDate
     * @return List<LocalDate>
     */
    public static List<LocalDate> getBetweenDate(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> result = new ArrayList<>();
        long distance = ChronoUnit.DAYS.between(startDate, endDate);
        if (distance < 1) {
            return result;
        }
        Stream.iterate(startDate, d -> d.plusDays(1)).limit(distance + 1).forEach(result::add);
        return result;
    }


    public static void main(String[] args) {
        LocalDate startDate = LocalDate.parse("2020-06-12", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = LocalDate.parse("2020-07-02", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        List<LocalDate> betweenDate = getBetweenDate(startDate, endDate);
        System.out.println(betweenDate);

    }
}

