package com.healerjean.proj.template.bo;

import java.util.Date;
import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

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
     * 企业id标识,例如商家是商家id,保司是保司id
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
     * 业务请求数据
     */
    private String businessData;

    /**
     * processing 处理中，completed 完成，fail 失败
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

