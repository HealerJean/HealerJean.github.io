package com.healerjean.proj.db;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.healerjean.proj.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangyujin
 * @date 2023/6/17  12:21.
 */
@Slf4j
@Component
public class MybatisPlusGenerator {

    /**
     * 默认项目Key
     */
    private static final String PROJECT_KEY = "hlj-project";
    /**
     * 需要替换的目录
     */
    private static final String REPLACE_DIRECTORY = PROJECT_KEY + "-web";

    /**
     * 生成表
     *
     * @param tableNameArray 表名列表
     * @param projectPath    项目路径
     */
    public void generator(String[] tableNameArray, String projectPath, DataSourceConfig sourceConfig) {
        if (StringUtils.isBlank(projectPath)) {
            projectPath = System.getProperty("user.dir");
        }
        log.info("projectPath:{},tableNameArray:{}", projectPath, JsonUtils.toString(tableNameArray));
        if (projectPath.contains(REPLACE_DIRECTORY)) {
            // 如果是从单元测试执行,则该目录会定位到web层,需要替换掉
            projectPath = projectPath.replace(REPLACE_DIRECTORY, StringUtils.EMPTY);
        }
        for (ProjectModuleEnum projectModule : ProjectModuleEnum.values()) {
            StringBuilder pathBuilder = new StringBuilder(projectPath);
            pathBuilder.append("/").append(PROJECT_KEY).append("-").append(projectModule.getName());
            // 代码生成器
            AutoGenerator autoGenerator = new AutoGenerator();
            // 全局配置
            autoGenerator.setGlobalConfig(createGlobalConfig(pathBuilder.toString()));
            // 数据源配置
            autoGenerator.setDataSource(sourceConfig);
            // 包配置
            PackageConfig packageConfig = new PackageConfig();
            packageConfig.setParent("com")
                    .setService("dao.service")
                    .setServiceImpl("dao.service.impl")
                    .setMapper("dao.mapper")
                    .setEntity("domain.po");
            autoGenerator.setPackageInfo(packageConfig);
            // 自定义配置
            autoGenerator.setCfg(createInjectionConfig(pathBuilder.toString(), projectModule, packageConfig));
            // 模版配置
            autoGenerator.setTemplate(createTemplateConfig(projectModule));
            // 策略配置
            autoGenerator.setStrategy(createStrategyConfig(tableNameArray, packageConfig.getModuleName()));
            autoGenerator.setTemplateEngine(new VelocityTemplateEngine());
            autoGenerator.execute();
        }
    }

    /**
     * 数据源配置
     *
     * @return 数据源配置
     */
    private static DataSourceConfig createDataSourceConfig() {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl("jdbc:mysql://[ip]:[port]/[db_name]?characterEncoding=utf-8&useUnicode=true&autoReconnect=true&connectTimeout=3000&initialTimeout=1&socketTimeout=5000&useSSL=false&serverTimezone=CTT");
        dataSourceConfig.setDriverName("com.mysql.jdbc.Driver");
        dataSourceConfig.setUsername("");
        dataSourceConfig.setPassword("");
        return dataSourceConfig;
    }

    /**
     * 策略配置
     *
     * @param tableNameArray 表名
     * @param moduleName     模块名
     * @return 策略配置
     */
    private static StrategyConfig createStrategyConfig(String[] tableNameArray, String moduleName) {
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setInclude(tableNameArray);
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setEntityColumnConstant(true);
        strategy.setTablePrefix(moduleName + "_");
        return strategy;
    }

    /**
     * 模版配置
     *
     * @return 模版配置
     */
    private static TemplateConfig createTemplateConfig(ProjectModuleEnum projectModuleEnum) {
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        templateConfig.setController(null);
        if (ProjectModuleEnum.DOMAIN.equals(projectModuleEnum)) {
            templateConfig.setService(null);
            templateConfig.setServiceImpl(null);
            templateConfig.setMapper(null);
            templateConfig.setXml(null);
        } else if (ProjectModuleEnum.DAO.equals(projectModuleEnum)) {
            templateConfig.setEntity(null);
        }
        return templateConfig;
    }

    /**
     * 自定义配置
     *
     * @return 自定义配置
     */
    private static InjectionConfig createInjectionConfig(String projectPath, ProjectModuleEnum projectModuleEnum, PackageConfig packageConfig) {
        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {
            }
        };
        if (ProjectModuleEnum.DOMAIN.equals(projectModuleEnum)) {
            return injectionConfig;
        }
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 如果模板引擎是 velocity
        String templatePath = "/templates/mapper.xml.vm";
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String xmlPath = packageConfig.getParent().replace(StringPool.DOT, StringPool.SLASH) + "/" + packageConfig.getMapper().replace(StringPool.DOT, StringPool.SLASH);
                // 自定义输出文件名,如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/" + xmlPath + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        injectionConfig.setFileOutConfigList(focList);
        return injectionConfig;
    }

    /**
     * 全局配置
     *
     * @param projectPath 项目路径
     * @return 全局配置
     */
    private static GlobalConfig createGlobalConfig(String projectPath) {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(projectPath + "/src/main/java");
        globalConfig.setAuthor("example author");
        globalConfig.setOpen(false);
        globalConfig.setFileOverride(true);
        globalConfig.setBaseResultMap(true);
        globalConfig.setBaseColumnList(true);
        globalConfig.setDateType(DateType.ONLY_DATE);
        globalConfig.setIdType(IdType.AUTO);
        globalConfig.setActiveRecord(true);
        globalConfig.setServiceName("%sDao");
        globalConfig.setServiceImplName("%sDaoImpl");
        return globalConfig;
    }

    /**
     * 项目模块枚举
     */
    @Getter
    @AllArgsConstructor
    public enum ProjectModuleEnum {
        /**
         * Domain
         */
        DOMAIN("domain"),
        /**
         * Dao
         */
        DAO("dao");
        /**
         * 项目模块名称
         */
        private final String name;
    }
}

