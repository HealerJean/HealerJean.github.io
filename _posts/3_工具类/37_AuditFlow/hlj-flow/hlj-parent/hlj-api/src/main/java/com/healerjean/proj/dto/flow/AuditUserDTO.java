package com.healerjean.proj.dto.flow;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healerjean.proj.common.group.ValidateGroup;
import com.healerjean.proj.constant.DictionaryConstants;
import com.healerjean.proj.validate.anno.DictionaryInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author HealerJean
 * @ClassName AuditUserDTO
 * @date 2019-11-03  16:29.
 * @Description
 */
@ApiModel(description = "审批用户DTO")
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditUserDTO {


    @ApiModelProperty("主键Id")
    private Long id;

    @ApiModelProperty("任务类型")
    private String taskType;

    @ApiModelProperty("抄送人、审批人类型 ")
    @NotBlank(message = "抄送人、审批人类型不能为空", groups = {ValidateGroup.ConfigDefultAuditUser.class})
    @DictionaryInclude(message = "用户类不在字典中", type = DictionaryConstants.AUDIT_USER_TYPE)
    private String auditUserType;

    @ApiModelProperty("审批对象类型：角色或ID ")
    @NotBlank(message = "审批对象类型不能为空", groups = {ValidateGroup.ConfigDefultAuditUser.class})
    @DictionaryInclude(message = "审批对象类型不在字典中", type = DictionaryConstants.AUDIT_OBJECT_TYPE)
    private String auditObjectType;

    @ApiModelProperty("审批对象对应的id")
    @NotNull(message = "审批对象对应的id不能为空", groups = {ValidateGroup.ConfigDefultAuditUser.class})
    private Long auditObjectId;

    @ApiModelProperty("处理顺序，抄送人0")
    @NotNull(message = "处理顺序不能为空", groups = {ValidateGroup.ConfigDefultAuditUser.class})
    private Integer step;

    @ApiModelProperty("审批状态")
    private String status;

    @ApiModelProperty("审批人名字")
    private String auditUserName;


}
