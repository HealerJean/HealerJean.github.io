package com.healerjean.proj.config;

import org.apache.ibatis.type.LocalDateTypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * @author HealerJean
 * @ClassName CustomLocalDateTypeHandler
 * @date 2020/3/26  11:20.
 * @Description
 */
public class CustomLocalDateTypeHandler extends LocalDateTypeHandler {

    @Override
    public LocalDate getResult(ResultSet rs, String columnName) throws SQLException {
        Object object = rs.getObject(columnName);
        System.out.println(object);
        //在这里强行转换，将sql的时间转换为LocalDateTime
        if (object instanceof java.sql.Date) {
            LocalDate localDate = ((java.sql.Date) object).toLocalDate();
            return localDate;
        }
        return super.getResult(rs, columnName);
    }
}
