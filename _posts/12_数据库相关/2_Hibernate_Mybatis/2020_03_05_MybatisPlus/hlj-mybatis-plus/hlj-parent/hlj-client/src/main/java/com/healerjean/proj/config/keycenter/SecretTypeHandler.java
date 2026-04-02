package com.healerjean.proj.config.keycenter;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author HealerJean
 * @ClassName SecretTypeHandler
 * @date 2020/4/9 14:27
 * @Description
 * 用于MyBatis ORM框架中自动处理数据的加密和解密操作
 * 在数据写入数据库前自动加密，从数据库读取后自动解密
 */
@MappedTypes(String.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class SecretTypeHandler extends BaseTypeHandler<String> {

    /**
     * 设置非空参数，将Java对象转换为JDBC类型
     * 在向数据库写入数据时，对非空字符串进行加密处理
     *
     * @param ps PreparedStatement对象
     * @param i 参数位置索引
     * @param parameter 参数值（待加密的字符串）
     * @param jdbcType JDBC类型
     * @throws SQLException SQL异常
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        // 只对非空字符串进行加密处理
        ps.setString(i, StringUtils.isNotBlank(parameter) ? KeyCenterUtils.encrypt(parameter) : parameter);
    }

    /**
     * 通过列名获取可为空的结果，并进行解密处理
     *
     * @param rs 结果集
     * @param columnName 列名
     * @return 解密后的字符串
     * @throws SQLException SQL异常
     */
    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return decryptString(rs.getString(columnName));
    }

    /**
     * 通过列索引获取可为空的结果，并进行解密处理
     *
     * @param rs 结果集
     * @param columnIndex 列索引
     * @return 解密后的字符串
     * @throws SQLException SQL异常
     */
    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return decryptString(rs.getString(columnIndex));
    }

    /**
     * 通过列索引获取存储过程调用的可为空的结果，并进行解密处理
     *
     * @param cs CallableStatement对象
     * @param columnIndex 列索引
     * @return 解密后的字符串
     * @throws SQLException SQL异常
     */
    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return decryptString(cs.getString(columnIndex));
    }

    /**
     * 解密字符串工具方法
     * 只对非空字符串进行解密处理
     *
     * @param value 待解密的字符串
     * @return 解密后的字符串
     */
    private String decryptString(String value) {
        if (StringUtils.isNotBlank(value)) {
            return KeyCenterUtils.decrypt(value);
        }
        return value;
    }
}
