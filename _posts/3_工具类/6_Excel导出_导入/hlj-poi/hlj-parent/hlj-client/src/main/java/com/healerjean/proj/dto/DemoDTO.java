package com.healerjean.proj.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.healerjean.proj.common.dto.ValidateGroup;
import com.healerjean.proj.common.dto.page.query.PageQuery;
import com.healerjean.proj.utils.json.JsonLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;
@Data
@Accessors(chain = true)
@ApiModel(value = "demo实体类")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DemoDTO extends PageQuery {

    private Long id;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "是否删除，10可用，99删除 ", hidden = true)
    private String status;

    @ApiModelProperty(value = "创建人", hidden = true)
    private Long createUser;

    @ApiModelProperty(value = "创建人名字", hidden = true)
    private String createName;

    @ApiModelProperty(value = "创建时间", hidden = true)
    private java.util.Date createTime;

    @ApiModelProperty(value = "更新人", hidden = true)
    private Long updateUser;

    @ApiModelProperty(value = "更新人名称", hidden = true)
    private String updateName;

    @ApiModelProperty(hidden = true)
    private java.util.Date updateTime;

}
