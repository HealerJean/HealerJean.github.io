package com.healerjean.proj.config.keycenter.one;

/**
 * @author HealerJean
 * @ClassName AESTypeHandler
 * @date 2020/4/9  14:27.
 * @Description
 */

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomTypeHandler<T> extends BaseTypeHandler<T> {

    @Autowired
    private KeyCenterUtils keyCenterUtils;

    public CustomTypeHandler() {
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, this.keyCenterUtils.encrypt((String)parameter));
    }
    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String columnValue = rs.getString(columnName);
        //有一些可能是空字符
        return StringUtils.isBlank(columnValue) ? (T)columnValue : (T)this.keyCenterUtils.decrypt(columnValue);
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String columnValue = rs.getString(columnIndex);
        return StringUtils.isBlank(columnValue) ? (T)columnValue : (T)this.keyCenterUtils.decrypt(columnValue);
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String columnValue = cs.getString(columnIndex);
        return StringUtils.isBlank(columnValue) ? (T)columnValue : (T)this.keyCenterUtils.decrypt(columnValue);
    }
}

