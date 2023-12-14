package com.healerjean.proj.data.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件任务(FileTask)表实体类
 *
 * @author zhangyujin
 * @date 2023-12-13 15:21:28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FileTask extends Model<FileTask> {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;


    /**
     * 主键标识列
     */
    @TableId(value = "id", type = IdType.AUTO)
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


    /**
     * id
     */
    public static final String ID = "id";

    /**
     * user_id
     */
    public static final String USER_ID = "user_id";

    /**
     * task_id
     */
    public static final String TASK_ID = "task_id";

    /**
     * task_type
     */
    public static final String TASK_TYPE = "task_type";

    /**
     * business_type
     */
    public static final String BUSINESS_TYPE = "business_type";

    /**
     * business_data
     */
    public static final String BUSINESS_DATA = "business_data";

    /**
     * task_status
     */
    public static final String TASK_STATUS = "task_status";

    /**
     * result_url
     */
    public static final String RESULT_URL = "result_url";

    /**
     * result_message
     */
    public static final String RESULT_MESSAGE = "result_message";

    /**
     * url
     */
    public static final String URL = "url";

    /**
     * ext
     */
    public static final String EXT = "ext";

    /**
     * created_time
     */
    public static final String CREATED_TIME = "created_time";

    /**
     * modified_time
     */
    public static final String MODIFIED_TIME = "modified_time";

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}

