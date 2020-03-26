package com.healerjean.proj.config;

import org.apache.ibatis.type.LocalDateTimeTypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * @author HealerJean
 * @ClassName CustomLocalDateTimeTypeHandler
 * @date 2020/3/26  11:16.
 * @Description
 */
public class CustomLocalDateTimeTypeHandler extends LocalDateTimeTypeHandler {

    @Override
    public LocalDateTime getResult(ResultSet rs, String columnName) throws SQLException {
        Object object = rs.getObject(columnName);
        System.out.println(object);
        //在这里强行转换，将sql的时间转换为LocalDateTime
        if (object instanceof java.sql.Timestamp) {
            LocalDateTime localDateTime = ((java.sql.Timestamp) object).toLocalDateTime();
            return localDateTime;
        }
        return super.getResult(rs, columnName);
    }
}
