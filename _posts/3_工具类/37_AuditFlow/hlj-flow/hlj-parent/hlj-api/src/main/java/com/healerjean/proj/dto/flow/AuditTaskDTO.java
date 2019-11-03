package com.healerjean.proj.dto.flow;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healerjean.proj.common.group.ValidateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author HealerJean
 * @ClassName AuditTaskDTO
 * @date 2019-11-03  17:19.
 * @Description 审批任务DTO
 */

@ApiModel(description = "审批任务DTO")
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditTaskDTO {

    @ApiModelProperty("主键Id")
    @NotNull(message = "任务类型不能为空", groups = {ValidateGroup.AuditTask.class})
    private Long auditTaskId;

    @ApiModelProperty("任务类型")
    @NotBlank(message = "任务类型不能为空", groups = {ValidateGroup.TaskAuditApply.class})
    private String taskType;


    @ApiModelProperty(value = "审批任务名字")
    private String taskName;

    @ApiModelProperty("任务数据Json格式")
    @NotBlank(message = "任务数据Json格式不能为空", groups = {ValidateGroup.TaskAuditApply.class})
    private String taskData;

    @ApiModelProperty("审批状态")
    private String status;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("创建人,发起人")
    private Long createUser;

    @ApiModelProperty(value = "审批人集合")
    private List<AuditUserDTO> approvers;

    @ApiModelProperty(value = "抄送人Id集合")
    private List<AuditUserDTO> copyUsers;

    @ApiModelProperty("审批意见")
    @NotNull(message = "审批意见不能为空", groups = {ValidateGroup.AuditTask.class})
    private String auditMessage;

    @ApiModelProperty("审批附件Josn集合")
    private String refFileIds;

    @ApiModelProperty("审批第几步")
    private Integer step;

    @ApiModelProperty("审批对象类型：角色或ID ")
    private String auditObjectType;

    @ApiModelProperty("审批对象对应的id ")
    private Long auditObjectId;

    @ApiModelProperty(value = "审批结果")
    @NotNull(message = "审批结果不能为空", groups = {ValidateGroup.AuditTask.class})
    private Boolean auditResult;

    @ApiModelProperty(value = "任务数量")
    private Long count;



}
