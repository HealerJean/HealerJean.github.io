package com.healerjean.proj.dto.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healerjean.proj.common.group.ValidateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;


/**
 * @ClassName DictionaryDTO
 * @Author HealerJean
 * @Date 2019/4/11 15:15
 * @Description 字典数据传输实体
 */
@ApiModel(description = "字典数据DTO")
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DictionaryDataDTO {

    @ApiModelProperty(value = "主键Id")
    private Long id;

    @ApiModelProperty(value = "字典类型键")
    @NotBlank(message = "字典类型键不能为空", groups = {ValidateGroup.AddDictData.class})
    @Length(max = 32, message = "字典类型键 长度不能超过32", groups = {ValidateGroup.AddDictData.class})
    private String typeKey;

    @ApiModelProperty(value = "字典数据键")
    @NotBlank(message = "字典数据键不能为空", groups = {ValidateGroup.AddDictData.class, ValidateGroup.UpdateDictData.class})
    @Length(max = 32, message = "字典数据键 长度不能超过32", groups = {ValidateGroup.AddDictData.class, ValidateGroup.UpdateDictData.class})
    private String dataKey;

    @ApiModelProperty(value = "字典数据描述")
    @NotBlank(message = "字典数据描述不能为空", groups = {ValidateGroup.AddDictData.class, ValidateGroup.UpdateDictData.class})
    @Length(max = 64, message = "字典数据描述 长度不能超过64", groups = {ValidateGroup.AddDictData.class, ValidateGroup.UpdateDictData.class})
    private String dataValue;

    @ApiModelProperty(value = "是否分页 true，分页 false 不分页 ,默认分页")
    private Boolean flag;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "当前页码")
    private Integer pageNo;

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;

    @ApiModelProperty(value = "多typeKey")
    private List<String> typeKeys;
}



