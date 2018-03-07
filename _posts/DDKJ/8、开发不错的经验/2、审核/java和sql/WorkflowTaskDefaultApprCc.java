package com.duodian.admore.entity.db.workflow;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 类描述：
 * 创建人：lishiwei
 * 创建时间：2017/2/16
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
@Entity
@Table(name = "workflow_task_default_appr_cc",
        indexes = {
                @Index(name = "type_adm_uniq", columnList = "taskType,admId", unique = true)
        }
)
public class WorkflowTaskDefaultApprCc implements Serializable {

    private static final long serialVersionUID = 4198682330135502141L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer taskType;       //EnumWorkflowTaskType

    private Long admId;       //后台用户ID

    private Integer admType;     //EnumWorkflowTaskUserType     审批人还是抄送人

    private Integer examineOrder;     //处理顺序 多个人审批时


    public WorkflowTaskDefaultApprCc() {
    }

    public WorkflowTaskDefaultApprCc(Integer taskType, Long admId, Integer admType, Integer examineOrder) {
        this.taskType = taskType;
        this.admId = admId;
        this.admType = admType;
        this.examineOrder = examineOrder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public Long getAdmId() {
        return admId;
    }

    public void setAdmId(Long admId) {
        this.admId = admId;
    }

    public Integer getAdmType() {
        return admType;
    }

    public void setAdmType(Integer admType) {
        this.admType = admType;
    }

    public Integer getExamineOrder() {
        return examineOrder;
    }

    public void setExamineOrder(Integer examineOrder) {
        this.examineOrder = examineOrder;
    }
}
