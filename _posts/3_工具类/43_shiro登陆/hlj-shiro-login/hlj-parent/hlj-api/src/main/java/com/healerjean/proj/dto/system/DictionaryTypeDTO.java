package com.healerjean.proj.dto.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healerjean.proj.common.group.ValidateGroup;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @ClassName DictionaryTypeDTO
 * @Author TD
 * @Date 2019/4/11 15:16
 * @Description 字典类型传输实体
 */
@ApiModel(description = "字典类型DTO")
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DictionaryTypeDTO {

    /**
     * 字典类型Id
     */
    private Long id;

    /**
     * 字典类型键
     */
    @NotBlank(message = "字典类型键不能为空", groups = {ValidateGroup.AddDictType.class, ValidateGroup.UpdateDictType.class})
    @Length(max = 32, message = "字典类型键 长度不能超过32", groups = {ValidateGroup.AddDictType.class, ValidateGroup.UpdateDictType.class})
    private String typeKey;
    /**
     * 字典类型描述
     */
    @NotBlank(message = "字典类型描述不能为空", groups = {ValidateGroup.AddDictType.class, ValidateGroup.UpdateDictType.class})
    @Length(max = 64, message = "字典类型描述 长度不能超过64", groups = {ValidateGroup.AddDictType.class, ValidateGroup.UpdateDictType.class})
    private String typeDesc;

    /**
     * 是否分页 true，分页 false 不分页 ,默认分页
     */
    private Boolean flag;

    /**
     * 状态
     */
    private String status;

    /**
     * 当前页码
     */
    private Integer pageNo;

    /**
     * 每页大小
     */
    private Integer pageSize;
}
