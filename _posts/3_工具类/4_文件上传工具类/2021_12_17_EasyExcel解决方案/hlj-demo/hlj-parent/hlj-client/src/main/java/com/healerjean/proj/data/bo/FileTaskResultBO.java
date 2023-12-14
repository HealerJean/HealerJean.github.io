package com.healerjean.proj.data.bo;

import com.healerjean.proj.enums.FileTaskEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * FileTaskResultBO
 *
 * @author zhangyujin
 * @date 2023/12/13
 */
@Accessors(chain = true)
@Data
public class FileTaskResultBO implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -7865290338813842668L;

    /**
     * 任务状态
     */
    private FileTaskEnum.TaskStatusEnum taskStatusEnum;

    /**
     * 结果描述
     */
    private String resultDesc;

    /**
     * 任务执行结束待更新数据
     */
    private FileTaskBO updateFileTaskBO;



    /**
     * 执行成功
     */
    public static FileTaskResultBO success(FileTaskBO updateFileTaskBO) {
        return new FileTaskResultBO()
                .setTaskStatusEnum(FileTaskEnum.TaskStatusEnum.COMPLETED)
                .setResultDesc(FileTaskEnum.TaskErrorEnum.SUCCESS.getDesc())
                .setUpdateFileTaskBO(updateFileTaskBO);
    }

    /**
     * 执行失败
     */
    public static FileTaskResultBO fail(FileTaskBO updateFileTaskBO, String errorMsg) {
        return new FileTaskResultBO()
                .setTaskStatusEnum(FileTaskEnum.TaskStatusEnum.FAIL)
                .setResultDesc(errorMsg)
                .setUpdateFileTaskBO(updateFileTaskBO);
    }
}
