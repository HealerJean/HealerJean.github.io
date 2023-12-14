package com.healerjean.proj.data.mq;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * FileTaskMq
 *
 * @author zhangyujin
 * @date 2023/12/13
 */
@Accessors(chain = true)
@Data
public class FileTaskMq implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3445555258769522901L;


    /**
     * 业务类型：{@link com.healerjean.proj.enums.FileTaskEnum.BusinessTypeEnum}
     */
    private String businessType;

    /**
     * taskId
     */
    private String taskId;

}
