package com.healerjean.proj.pojo;

import com.healerjean.proj.enmus.SystemEmum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class DictionaryTypeDTO {

    /** 字典类型Id  */
    private Long id;
    /**  字典类型键 */
    private String typeKey;
    /** 字典类型描述 */
    private String typeDesc;
    // /**  是否分页 true，分页 false 不分页 ,默认分页  */
    private Boolean flag;
    /** 状态   */
    private String status;
    /**  创建时间 */
    private LocalDateTime createTime;
    // 性别
    private SystemEmum.SexEnum sexEnum;
}
