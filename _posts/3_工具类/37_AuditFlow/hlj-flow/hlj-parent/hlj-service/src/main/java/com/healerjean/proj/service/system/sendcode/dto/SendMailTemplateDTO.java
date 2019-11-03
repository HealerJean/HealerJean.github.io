package com.healerjean.proj.service.system.sendcode.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName SendMailTemplateDTO
 * @date 2019/5/10  15:03.
 * @Description
 */
@Data
@Accessors(chain = true)
public class SendMailTemplateDTO extends SendMailDTO {


    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板中的参数名
     */
    private Map map;

}
