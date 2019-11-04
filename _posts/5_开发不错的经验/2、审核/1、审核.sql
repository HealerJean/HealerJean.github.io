

# 通过上面的主键和申请类型 用来唯一确定 
-- public class WorkflowTask implements Serializable {
-- 
--     private static final long serialVersionUID = 4198682330135502141L;
-- 
--     public static final int SPECIALMONEYENDSHOWFLAG_NO = 0;
--     public static final int SPECIALMONEYENDSHOWFLAG_YES = 1;

--     private Long id;
--     private Integer taskType;       //EnumWorkflowTaskType
--     private Long cuserId;       //申请人
--     private Long pid;           //申请类型对应的 具体申请记录的ID 比如预开发票申请 对应的 就是预开发票的ID
--     private Integer status;     //审批状态 EnumWorkflowTaskStatus
--     private Integer cuserType;  //申请人类型 EnumWorkflowTaskCuserType


select * from workflow_task w  where w.pid = '13' and w.taskType ='28'  order by w.cdate desc;   # 出现一条记录,主键为266


select * from workflow_task_examine_details w where w.taskId = '266' ;# 出现三条记录


select * from  workflow_task_default_appr_cc w where w.taskType = '28'; #出现2条记录 审批人 抄送人数据 

# 关于审核类型为 是否可用和是否可以修改审核人 
select * from  workflow_task_default w where w.taskType ='28' order by w.cdate desc;


select * from workflow_task_unread_badge w where taskType ='28'; #未读标记