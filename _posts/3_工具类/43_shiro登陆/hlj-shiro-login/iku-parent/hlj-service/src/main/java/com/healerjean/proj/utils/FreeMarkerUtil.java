package com.healerjean.proj.utils;

import com.healerjean.proj.exception.BusinessException;
import com.healerjean.proj.exception.ParameterErrorException;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.Map;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName FreeMarkerUtil
 * @date 2019/5/10  17:44.
 * @Description freemarker模板 工具类
 */
@Slf4j
public class FreeMarkerUtil {

    /**
     * 模板目录 /resource/ftl/……
     *
     * @param templateName 模板名字 example.ftl -> example
     * @return
     */
    public static String ftlTemplate(String templateName, Map params) {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_27);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setClassLoaderForTemplateLoading(FreeMarkerUtil.class.getClassLoader(), "/ftl/");
        try {
            Template template = configuration.getTemplate(templateName + ".ftl", "UTF-8");
            if (template == null) {
                log.info("=========邮件模板为空=========");
                throw new RuntimeException("邮件模板为空！");
            }
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, params);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (TemplateException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 字符串模板解析
     *
     * @param html   字符串的HTML模板 举例 ： <h3>你好，${name}, 这是一封模板邮件! ，我叫HealerJean</h3>
     * @param params
     * @return
     */
    public static String stringTemplate(String html, Map params) {

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_27);
        String tempName = "templateName";
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate(tempName, html);

        configuration.setTemplateLoader(stringTemplateLoader);
        configuration.setDefaultEncoding("UTF-8");

        try {
            Template template = configuration.getTemplate(tempName, "UTF-8");
            if (template == null) {
                log.info("=========邮件模板为空=========");
                throw new RuntimeException("邮件模板为空！");
            }
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, params);
        } catch (Exception e) {
            throw new BusinessException("模板匹配数据异常：" , e);
        }
    }

}
