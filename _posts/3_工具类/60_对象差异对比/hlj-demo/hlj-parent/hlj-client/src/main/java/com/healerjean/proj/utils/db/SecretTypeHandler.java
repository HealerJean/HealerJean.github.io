package com.healerjean.proj.utils.db;


import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * CustomTypeHandler
 * @author zhangyujin
 * @date 2023/6/15  13:48
 */
@Service
public class SecretTypeHandler<T> extends BaseTypeHandler<T> {

    /**
     * keyCenterUtils
     */
    @Autowired
    private KeyCenterUtils keyCenterUtils;

    /**
     * CustomTypeHandler
     */
    public SecretTypeHandler() {
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
        ;
        return StringUtils.isBlank(columnValue) ? (T)columnValue : (T)this.keyCenterUtils.decrypt(columnValue);
    }
}

