package com.healerjean.proj.data.bo;

import com.healerjean.proj.enums.FileTaskEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件任务(FileTask)BO对象
 *
 * @author zhangyujin
 * @date 2023-12-13 15:21:28
 */
@Accessors(chain = true)
@Data
public class FileTaskBO implements Serializable {

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
     * export 导出，import导入  {@link  FileTaskEnum.TaskTypeEnum}
     */
    private String taskType;

    /**
     * 业务类型 {@link  FileTaskEnum.BusinessTypeEnum}
     */
    private String businessType;

    /**
     * 业务请求数据
     */
    private String businessData;


    /**
     * processing 处理中，completed 完成，fail 失败 {@link  FileTaskEnum.TaskStatusEnum}
     */
    private String taskStatus;

    /**
     * 返回的url地址
     */
    private String resultUrl;

    /**
     * 处理结果
     */
    private String resultMessage;

    /**
     * 上传文件地址
     */
    private String url;

    /**
     * 扩展信息
     */
    private String ext;

    /**
     * 记录创建时间
     */
    private Date createdTime;

    /**
     * 记录最后更新时间
     */
    private Date modifiedTime;

}

