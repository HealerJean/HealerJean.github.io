package com.healerjean.proj.data.req;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 文件任务(FileTask)QueryReq对象
 *
 * @author zhangyujin
 * @date 2023-12-13 15:21:28
 */
@Accessors(chain = true)
@Data
public class FileTaskQueryReq implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;


    /**
     * 主键标识列
     */
    private Long id;

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 任务唯一id
     */
    private String taskId;

    /**
     * export 导出，import导入
     */
    private String taskType;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * processing 处理中，completed 完成，fail 失败
     */
    private String taskStatus;



}

