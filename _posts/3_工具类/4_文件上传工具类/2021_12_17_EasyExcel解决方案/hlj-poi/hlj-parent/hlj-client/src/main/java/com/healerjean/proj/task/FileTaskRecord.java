package com.healerjean.proj.task;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * FileTaskRecord
 *
 * @author zhangyujin
 * @date 2023/6/25$  21:28$
 */
@Accessors(chain = true)
@Data
public class FileTaskRecord implements Serializable {

    /**
     * 任务Id
     */
    private String taskId;

    /**
     * 任务类型：{@link TaskEnum.TaskTypeEnum}
     */
    private String type;

    /**
     * 处理状态： {@link TaskEnum.TaskStatusEnum}
     */
    private String processStatus;

    /**
     * 业务类型：{@link TaskEnum.BusinessTypeEnum}
     */
    private String businessType;

    /**
     * 业务类型：json
     */
    private String businessData;

    /**
     * 结果文件名称
     */
    private String resultFileName;

    /**
     * 上传地址
     */
    private String uploadUrl;

    /**
     * 下载地址（返回结果的url）
     */
    private String downloadUrl;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 扩展信息
     */
    private String ext;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime modifiedTime;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误原因
     */
    private String errorMsg;
}
