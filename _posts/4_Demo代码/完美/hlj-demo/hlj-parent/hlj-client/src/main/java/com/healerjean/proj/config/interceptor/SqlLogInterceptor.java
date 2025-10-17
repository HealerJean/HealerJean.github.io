package com.healerjean.proj.config.interceptor;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SqlLogInterceptor - MyBatis SQL 日志拦截器
 * 用于记录 SQL 执行的详细信息，包括 SQL 语句、参数、执行时间等
 *
 * @author zhangyujin
 * @date 2024/9/23
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class SqlLogInterceptor implements Interceptor {
    private static final Logger log = LoggerFactory.getLogger(SqlLogInterceptor.class);
    
    /**
     * SQL 执行时间阈值，超过该值将以 WARN 级别记录日志（单位：毫秒）
     */
    private long slowSqlThreshold = 1000;
    
    /**
     * 是否格式化 SQL
     */
    private boolean formatSql = false;
    
    /**
     * 是否打印参数
     */
    private boolean showParams = true;
    
    /**
     * 是否打印执行结果
     */
    private boolean showResult = true;
    
    /**
     * 最大 SQL 长度，超过该长度将被截断
     */
    private int maxSqlLength = 5000;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 如果日志级别不是 DEBUG，则直接执行原方法
        if (!log.isDebugEnabled()) {
            return invocation.proceed();
        }
        
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = null;
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
        }
        
        String sqlId = mappedStatement.getId();
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Configuration configuration = mappedStatement.getConfiguration();
        
        // 记录开始时间
        long startTime = System.currentTimeMillis();
        
        // 执行 SQL
        Object result = null;
        try {
            // 执行真正的 SQL
            result = invocation.proceed();
            return result;
        } finally {
            // 计算执行时间
            long endTime = System.currentTimeMillis();
            long costTime = endTime - startTime;
            
            try {
                // 生成完整的 SQL 语句
                String completeSql = generateCompleteSql(configuration, boundSql, parameter);
                
                // 如果需要格式化 SQL
                if (formatSql) {
                    completeSql = formatSql(completeSql);
                }
                
                // 如果 SQL 过长，则截断
                if (completeSql.length() > maxSqlLength) {
                    completeSql = completeSql.substring(0, maxSqlLength) + "... [SQL too long, truncated]";
                }
                
                // 构建日志信息
                StringBuilder logMessage = new StringBuilder(256);
                logMessage.append("\n=========================== SQL执行详情 ===========================\n");
                logMessage.append("SQL ID: ").append(sqlId).append("\n");
                logMessage.append("执行时间: ").append(costTime).append("ms\n");
                logMessage.append("完整SQL: ").append(completeSql).append("\n");
                
                // 如果需要显示执行结果
                if (showResult && result != null) {
                    if (result instanceof Collection) {
                        logMessage.append("返回结果数: ").append(((Collection<?>) result).size()).append("\n");
                    } else if (result instanceof Map) {
                        logMessage.append("返回结果数: ").append(((Map<?, ?>) result).size()).append("\n");
                    } else if (result instanceof Integer) {
                        logMessage.append("影响行数: ").append(result).append("\n");
                    }
                }
                logMessage.append("==================================================================");
                
                // 根据执行时间决定日志级别
                if (costTime > slowSqlThreshold) {
                    log.warn(logMessage.toString());
                } else {
                    log.debug(logMessage.toString());
                }
            } catch (Exception e) {
                log.error("SQL日志记录异常", e);
            }
        }
    }

    /**
     * 生成完整的 SQL 语句（包含参数值）
     */
    private String generateCompleteSql(Configuration configuration, BoundSql boundSql, Object parameterObject) {
        // 如果不显示参数，则直接返回原始 SQL
        if (!showParams) {
            return boundSql.getSql().replaceAll("[\\s]+", " ");
        }
        
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        
        // 如果没有参数，直接返回 SQL
        if (CollectionUtils.isEmpty(parameterMappings) || parameterObject == null) {
            return sql;
        }
        
        // 使用 StringBuilder 提高性能
        StringBuilder sqlBuilder = new StringBuilder(sql);
        
        // 使用 TypeHandlerRegistry 处理参数
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        
        // 如果参数是基本类型
        if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
            // 替换第一个问号
            replaceFirstPlaceholder(sqlBuilder, getParameterValue(parameterObject));
        } else {
            // 处理复杂参数
            MetaObject metaObject = configuration.newMetaObject(parameterObject);
            
            // 收集所有参数值
            List<String> paramValues = new ArrayList<>(parameterMappings.size());
            for (ParameterMapping parameterMapping : parameterMappings) {
                String propertyName = parameterMapping.getProperty();
                Object value = null;
                
                // 从对象中获取属性值
                if (metaObject.hasGetter(propertyName)) {
                    value = metaObject.getValue(propertyName);
                } else if (boundSql.hasAdditionalParameter(propertyName)) {
                    // 从附加参数中获取值
                    value = boundSql.getAdditionalParameter(propertyName);
                }
                
                paramValues.add(getParameterValue(value));
            }
            
            // 批量替换所有问号
            replacePlaceholders(sqlBuilder, paramValues);
        }
        
        return sqlBuilder.toString();
    }
    
    /**
     * 替换 SQL 中的第一个问号
     */
    private void replaceFirstPlaceholder(StringBuilder sql, String value) {
        int questionMarkIndex = sql.indexOf("?");
        if (questionMarkIndex != -1) {
            sql.replace(questionMarkIndex, questionMarkIndex + 1, value);
        }
    }
    
    /**
     * 批量替换 SQL 中的所有问号
     */
    private void replacePlaceholders(StringBuilder sql, List<String> values) {
        // 使用正则表达式查找所有问号
        Pattern pattern = Pattern.compile("\\?");
        Matcher matcher = pattern.matcher(sql);
        
        int valueIndex = 0;
        int offset = 0;
        
        // 逐个替换问号
        while (matcher.find() && valueIndex < values.size()) {
            int start = matcher.start() + offset;
            int end = matcher.end() + offset;
            String value = values.get(valueIndex++);
            
            // 替换问号为实际值
            sql.replace(start, end, value);
            
            // 更新偏移量
            offset += value.length() - 1;
            
            // 重新创建匹配器
            matcher = pattern.matcher(sql);
            matcher.region(start + value.length(), sql.length());
        }
    }
    
    /**
     * 获取参数的字符串表示
     */
    private String getParameterValue(Object obj) {
        if (obj == null) {
            return "NULL";
        }
        
        if (obj instanceof String) {
            return "'" + escapeString((String) obj) + "'";
        } else if (obj instanceof Date) {
            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            return "'" + dateFormat.format((Date) obj) + "'";
        } else if (obj instanceof Boolean || obj instanceof Number) {
            return obj.toString();
        } else if (obj instanceof Collection) {
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            Collection<?> collection = (Collection<?>) obj;
            int i = 0;
            for (Object item : collection) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(getParameterValue(item));
                i++;
            }
            sb.append(")");
            return sb.toString();
        } else if (obj.getClass().isArray()) {
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            Object[] array = (Object[]) obj;
            for (int i = 0; i < array.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(getParameterValue(array[i]));
            }
            sb.append(")");
            return sb.toString();
        } else {
            return "'" + escapeString(obj.toString()) + "'";
        }
    }
    
    /**
     * 转义字符串中的特殊字符
     */
    private String escapeString(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        return str.replace("'", "''");
    }
    
    /**
     * 格式化 SQL 语句
     */
    private String formatSql(String sql) {
        if (StringUtils.isEmpty(sql)) {
            return "";
        }
        
        // 简单的 SQL 格式化
        sql = sql.replaceAll("(?i)SELECT", "\nSELECT")
                .replaceAll("(?i)FROM", "\nFROM")
                .replaceAll("(?i)WHERE", "\nWHERE")
                .replaceAll("(?i)AND", "\n  AND")
                .replaceAll("(?i)OR", "\n   OR")
                .replaceAll("(?i)GROUP BY", "\nGROUP BY")
                .replaceAll("(?i)HAVING", "\nHAVING")
                .replaceAll("(?i)ORDER BY", "\nORDER BY")
                .replaceAll("(?i)LIMIT", "\nLIMIT")
                .replaceAll("(?i)OFFSET", "\nOFFSET")
                .replaceAll("(?i)UPDATE", "\nUPDATE")
                .replaceAll("(?i)SET", "\nSET")
                .replaceAll("(?i)INSERT INTO", "\nINSERT INTO")
                .replaceAll("(?i)VALUES", "\nVALUES")
                .replaceAll("(?i)DELETE FROM", "\nDELETE FROM");
        
        return sql;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 从配置中读取属性
        if (properties != null) {
            String threshold = properties.getProperty("slowSqlThreshold");
            if (threshold != null && !threshold.isEmpty()) {
                try {
                    this.slowSqlThreshold = Long.parseLong(threshold);
                } catch (NumberFormatException e) {
                    log.warn("Invalid slowSqlThreshold value: {}", threshold);
                }
            }
            
            String formatSql = properties.getProperty("formatSql");
            if (formatSql != null && !formatSql.isEmpty()) {
                this.formatSql = Boolean.parseBoolean(formatSql);
            }
            
            String showParams = properties.getProperty("showParams");
            if (showParams != null && !showParams.isEmpty()) {
                this.showParams = Boolean.parseBoolean(showParams);
            }
            
            String showResult = properties.getProperty("showResult");
            if (showResult != null && !showResult.isEmpty()) {
                this.showResult = Boolean.parseBoolean(showResult);
            }
            
            String maxSqlLength = properties.getProperty("maxSqlLength");
            if (maxSqlLength != null && !maxSqlLength.isEmpty()) {
                try {
                    this.maxSqlLength = Integer.parseInt(maxSqlLength);
                } catch (NumberFormatException e) {
                    log.warn("Invalid maxSqlLength value: {}", maxSqlLength);
                }
            }
        }
    }
    
    // Getter and Setter methods for configuration properties
    public long getSlowSqlThreshold() {
        return slowSqlThreshold;
    }
    
    public void setSlowSqlThreshold(long slowSqlThreshold) {
        this.slowSqlThreshold = slowSqlThreshold;
    }
    
    public boolean isFormatSql() {
        return formatSql;
    }
    
    public void setFormatSql(boolean formatSql) {
        this.formatSql = formatSql;
    }
    
    public boolean isShowParams() {
        return showParams;
    }
    
    public void setShowParams(boolean showParams) {
        this.showParams = showParams;
    }
    
    public boolean isShowResult() {
        return showResult;
    }
    
    public void setShowResult(boolean showResult) {
        this.showResult = showResult;
    }
    
    public int getMaxSqlLength() {
        return maxSqlLength;
    }
    
    public void setMaxSqlLength(int maxSqlLength) {
        this.maxSqlLength = maxSqlLength;
    }
}