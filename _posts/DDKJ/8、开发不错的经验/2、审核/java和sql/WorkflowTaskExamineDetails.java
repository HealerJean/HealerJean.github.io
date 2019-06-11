package com.duodian.admore.entity.db.workflow;

import com.duodian.admore.enums.workflow.EnumWorkflowTaskExamineStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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
@Table(name = "workflow_task_examine_details",
        indexes = {
                @Index(name = "task_adm_uniq", columnList = "taskId,admId", unique = true)
        }
)
public class WorkflowTaskExamineDetails implements Serializable {

    private static final long serialVersionUID = 4198682330135502141L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long taskId;           //WorkflowTask id

    private Long admId;     //申请人 审批人 抄送人

    private Integer admType;     //EnumWorkflowTaskUserType

    private Integer examineOrder;     //处理顺序 多个人审批时

    private Integer examineStatus;     //当前处理状态 EnumWorkflowTaskExamineStatus

    private String remark;              //审批人审批时 批注、回复、说明

    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = true, updatable = false)
    private Date cdate;                      //创建时间

    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = true, updatable = true)
    private Date udate;                      //最后处理时间


    @Transient
    private String admName;

    @Transient
    private String examineStatusDesc;

    @Transient
    private Boolean currAdmUserFlag = false;        //是否是当前登录人

    @Transient
    private Boolean currNodeFlag = false;        //流程是否走到该节点 是否轮到该记录处理了

    @Transient
    private Boolean apprDetailsShowFlag = true;        //后面等待审批的审批人


    public WorkflowTaskExamineDetails() {
    }

    public WorkflowTaskExamineDetails(Long taskId, Long admId, Integer admType, Integer examineOrder, Integer examineStatus, String remark) {
        this.taskId = taskId;
        this.admId = admId;
        this.admType = admType;
        this.examineOrder = examineOrder;
        this.examineStatus = examineStatus;
        this.remark = remark;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
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

    public Integer getExamineStatus() {
        return examineStatus;
    }

    public void setExamineStatus(Integer examineStatus) {
        this.examineStatus = examineStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCdate() {
        return cdate;
    }

    public void setCdate(Date cdate) {
        this.cdate = cdate;
    }

    public Date getUdate() {
        return udate;
    }

    public void setUdate(Date udate) {
        this.udate = udate;
    }

    public String getAdmName() {
        return admName;
    }

    public void setAdmName(String admName) {
        this.admName = admName;
    }

    public String getExamineStatusDesc() {
        return EnumWorkflowTaskExamineStatus.getDes(this.examineStatus);
    }

    public void setExamineStatusDesc(String examineStatusDesc) {
        this.examineStatusDesc = examineStatusDesc;
    }

    public Integer getExamineOrder() {
        return examineOrder;
    }

    public void setExamineOrder(Integer examineOrder) {
        this.examineOrder = examineOrder;
    }

    public Boolean getCurrAdmUserFlag() {
        return currAdmUserFlag;
    }

    public void setCurrAdmUserFlag(Boolean currAdmUserFlag) {
        this.currAdmUserFlag = currAdmUserFlag;
    }

    public Boolean getCurrNodeFlag() {
        return currNodeFlag;
    }

    public void setCurrNodeFlag(Boolean currNodeFlag) {
        this.currNodeFlag = currNodeFlag;
    }

    public Boolean getApprDetailsShowFlag() {
        return apprDetailsShowFlag;
    }

    public void setApprDetailsShowFlag(Boolean apprDetailsShowFlag) {
        this.apprDetailsShowFlag = apprDetailsShowFlag;
    }
}
