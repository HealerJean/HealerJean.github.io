package org.springframework.test.context.jdbc;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.jdbc.SqlConfig.ErrorMode;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.context.util.TestContextResourceUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * BxDirectScriptsTestExecutionListener
 *
 * @author zhangyujin
 * @date 2023/6/15  15:14
 */
@Sql
public class BxDirectScriptsTestExecutionListener extends AbstractTestExecutionListener {
    /**
     * logger
     */
    private static final Log logger = LogFactory.getLog(SqlScriptsTestExecutionListener.class);

    /**
     * BxDirectScriptsTestExecutionListener
     */
    public BxDirectScriptsTestExecutionListener() {
    }


    @Override
    public final int getOrder() {
        return 5001;
    }

    @Override
    public void beforeTestMethod(TestContext testContext) {
        this.executeSqlScripts(testContext, HSql.ExecutionPhase.BEFORE_TEST_METHOD);
    }

    @Override
    public void afterTestMethod(TestContext testContext) {
        this.executeSqlScripts(testContext, HSql.ExecutionPhase.AFTER_TEST_METHOD);
    }

    /**
     * executeSqlScripts
     *
     * @param testContext    TestContext
     * @param executionPhase NoTXSql.ExecutionPhase
     */
    private void executeSqlScripts(TestContext testContext, HSql.ExecutionPhase executionPhase) {
        boolean classLevel = false;
        Set<HSql> sqlAnnotations = AnnotatedElementUtils.getMergedRepeatableAnnotations(testContext.getTestMethod(), HSql.class, HSqlGroup.class);
        if (sqlAnnotations.isEmpty()) {
            sqlAnnotations = AnnotatedElementUtils.getMergedRepeatableAnnotations(testContext.getTestClass(), HSql.class, HSqlGroup.class);
            if (!sqlAnnotations.isEmpty()) {
                classLevel = true;
            }
        }
        for (HSql sql : sqlAnnotations) {
            this.executeSqlScripts(sql, executionPhase, testContext, classLevel);
        }
    }

    /**
     * executeSqlScripts
     *
     * @param sql            NoTXSql
     * @param executionPhase NoTXSql.ExecutionPhase
     * @param testContext    TestContext
     * @param classLevel     classLevel
     */
    private void executeSqlScripts(HSql sql, HSql.ExecutionPhase executionPhase, TestContext testContext, boolean classLevel) {
        if (executionPhase == sql.executionPhase()) {
            MergedSqlConfig mergedSqlConfig = new MergedSqlConfig(sql.config(), testContext.getTestClass());
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.setSqlScriptEncoding(mergedSqlConfig.getEncoding());
            populator.setSeparator(mergedSqlConfig.getSeparator());
            populator.setCommentPrefix(mergedSqlConfig.getCommentPrefixes()[0]);
            populator.setBlockCommentStartDelimiter(mergedSqlConfig.getBlockCommentStartDelimiter());
            populator.setBlockCommentEndDelimiter(mergedSqlConfig.getBlockCommentEndDelimiter());
            populator.setContinueOnError(mergedSqlConfig.getErrorMode() == ErrorMode.CONTINUE_ON_ERROR);
            populator.setIgnoreFailedDrops(mergedSqlConfig.getErrorMode() == ErrorMode.IGNORE_FAILED_DROPS);
            String[] scripts = this.getScripts(sql, testContext, classLevel);
            scripts = TestContextResourceUtils.convertToClasspathResourcePaths(testContext.getTestClass(), scripts);
            List<Resource> scriptResources = TestContextResourceUtils.convertToResourceList(testContext.getApplicationContext(), scripts);
            String[] var9 = sql.statements();
            for (String s : var9) {
                String stmt = s;
                if (StringUtils.hasText(stmt)) {
                    stmt = stmt.trim();
                    scriptResources.add(new ByteArrayResource(stmt.getBytes(), String.format("from inlined SQL statement: %s", stmt)));
                }
            }
            populator.setScripts(scriptResources.toArray(new Resource[0]));
            String dsName = mergedSqlConfig.getDataSource();
            DynamicDataSourceContextHolder.push(dsName);
            DynamicRoutingDataSource dynamicRoutingDataSource = testContext.getApplicationContext().getBean(DynamicRoutingDataSource.class);
            DataSource dataSource = dynamicRoutingDataSource.determineDataSource();
            assert dataSource != null;
            populator.execute(dataSource);
            DynamicDataSourceContextHolder.poll();

        }
    }

    /**
     * getScripts
     *
     * @param sql         NoTXSql
     * @param testContext TestContext
     * @param classLevel  classLevel
     * @return String[]
     */
    private String[] getScripts(HSql sql, TestContext testContext, boolean classLevel) {
        String[] scripts = sql.scripts();
        if (ObjectUtils.isEmpty(scripts) && ObjectUtils.isEmpty(sql.statements())) {
            scripts = new String[]{this.detectDefaultScript(testContext, classLevel)};
        }

        return scripts;
    }

    /**
     * detectDefaultScript
     *
     * @param testContext TestContext
     * @param classLevel  classLevel
     * @return String
     */
    private String detectDefaultScript(TestContext testContext, boolean classLevel) {
        Class<?> clazz = testContext.getTestClass();
        Method method = testContext.getTestMethod();
        String elementType = classLevel ? "class" : "method";
        String elementName = classLevel ? clazz.getName() : method.toString();
        String resourcePath = ClassUtils.convertClassNameToResourcePath(clazz.getName());
        if (!classLevel) {
            resourcePath = resourcePath + "." + method.getName();
        }

        resourcePath = resourcePath + ".sql";
        String prefixedResourcePath = "classpath:" + resourcePath;
        ClassPathResource classPathResource = new ClassPathResource(resourcePath);
        if (classPathResource.exists()) {
            if (logger.isInfoEnabled()) {
                logger.info(String.format("Detected default SQL script \"%s\" for test %s [%s]", prefixedResourcePath, elementType, elementName));
            }

            return prefixedResourcePath;
        } else {
            String msg = String.format("Could not detect default SQL script for test %s [%s]: %s does not exist. Either declare statements or scripts via @Sql or make the default SQL script available.", elementType, elementName, classPathResource);
            logger.error(msg);
            throw new IllegalStateException(msg);
        }
    }
}