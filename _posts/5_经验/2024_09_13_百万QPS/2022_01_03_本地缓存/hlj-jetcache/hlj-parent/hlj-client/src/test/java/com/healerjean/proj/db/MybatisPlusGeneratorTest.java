package com.healerjean.proj.db;

/**
 * @author zhangyujin
 * @date 2023/6/17  12:20.
 */

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.healerjean.proj.base.BaseJunit5SpringTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * MybatisPlusGenerator测试
 *
 * @author zhanghanlin6
 * @date 2023-04-13 15:54
 */
@Slf4j
public class MybatisPlusGeneratorTest extends BaseJunit5SpringTest {
    /**
     * MybatisPlus生成器
     */
    @Resource
    private MybatisPlusGenerator mybatisPlusGenerator;
    /**
     * TABLES
     */
    private final static String[] TABLES = new String[]{"vender_premium_rate_model"};

    /**
     * 该单元测试不能提交线上,会影响单元测试流水线执行
     */
    @Test
    public void generator() {
        // 内存数据库
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        // dataSourceConfig.setUrl("jdbc:h2:mem:healerjean");
        // dataSourceConfig.setDriverName("org.h2.Driver");

        // 本地数据库
        dataSourceConfig.setUrl("jdbc:mysql://localhost:3306/healerjean?characterEncoding=utf-8&useUnicode=true&autoReconnect=true&connectTimeout=3000&initialTimeout=1&socketTimeout=5000&useSSL=false&serverTimezone=CTT");
        dataSourceConfig.setDriverName("com.mysql.jdbc.Driver");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("12345678");
        mybatisPlusGenerator.generator(TABLES, null, dataSourceConfig);
    }
}