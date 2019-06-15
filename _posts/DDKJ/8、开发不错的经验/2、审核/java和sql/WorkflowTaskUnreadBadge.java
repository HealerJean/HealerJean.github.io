package com.duodian.admore.entity.db.workflow;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 类描述：未读条数徽章
 * 创建人：lishiwei
 * 创建时间：2017/2/16
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
@Entity
@Table(name = "workflow_task_unread_badge",
        indexes = {
                @Index(name = "i_admId", columnList = "admId")
        }
)
public class WorkflowTaskUnreadBadge implements Serializable {

    private static final long serialVersionUID = 4198682330135502141L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long taskId;

    private Long admId;

    private Integer pageType;   //EnumWorkflowPageType


    public WorkflowTaskUnreadBadge() {
    }

    public WorkflowTaskUnreadBadge(Long taskId, Long admId, Integer pageType) {
        this.taskId = taskId;
        this.admId = admId;
        this.pageType = pageType;
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

    public Integer getPageType() {
        return pageType;
    }

    public void setPageType(Integer pageType) {
        this.pageType = pageType;
    }
}
