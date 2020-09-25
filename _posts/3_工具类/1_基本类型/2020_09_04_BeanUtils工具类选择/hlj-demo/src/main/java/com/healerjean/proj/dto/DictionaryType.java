package com.healerjean.proj.dto;

import com.healerjean.proj.enmus.SystemEmum;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author HealerJean
 * @Description
 */
@Data
public class DictionaryType implements Serializable {

    /**  主键 */
    private Long dictionaryTypeId;
    /**  字典类型 */
    private String typeKey;
    /**  字典类型 描述 */
    private String typeDesc;
    /**  状态 */
    private String status;
    /**  创建时间 */
    private Date createTime;
    /** 性别 */
    private Integer sex ;

}
