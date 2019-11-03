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
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author HealerJea
 * @ClassName ConfigDefultAuditUser
 * @date 2019-11-03  16:23.
 * @Description
 */
@ApiModel(description = "审批默认配置DTO")
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditDefaultConfigDTO {


    @ApiModelProperty(value = "主键Id")
    private Long id;

    @ApiModelProperty(value = "任务类型")
    @NotBlank(message = "审批任务类型不能为空", groups = {ValidateGroup.ConfigDefultAuditUser.class})
    @DictionaryInclude(type = DictionaryConstants.AUDIT_TASK,  message = "审批任务类型枚举不存在")
    private String taskType;

    @ApiModelProperty(value = "是否可以更改默认审批人")
    @NotNull(message = "是否可以更改默认审批人不能为空", groups = {ValidateGroup.ConfigDefultAuditUser.class})
    private Boolean modify;


    @ApiModelProperty(value = "审批人集合")
    @NotNull(message = "审批人集合不能为空", groups = {ValidateGroup.ConfigDefultAuditUser.class})
    @Size(min = 1, message = "最少需要一个审批人", groups = {ValidateGroup.ConfigDefultAuditUser.class})
    private List<AuditUserDTO> approvers;

    @ApiModelProperty(value = "抄送人Id集合")
    private List<AuditUserDTO> copyUsers;


}
