package com.healerjean.proj.service;


import com.healerjean.proj.data.bo.FileTaskBO;
import com.healerjean.proj.data.bo.FileTaskQueryBO;

import java.util.List;

/**
 * 文件任务(FileTask)Service接口
 *
 * @author zhangyujin
 * @date 2023-12-13 15:21:29
 */
public interface FileTaskService {

    /**
     * 保存-FileTask
     *
     * @param bo bo
     * @return boolean
     */
    boolean saveFileTask(FileTaskBO bo);

    /**
     * 更新-FileTask
     *
     * @param bo bo
     * @return boolean
     */
    boolean updateFileTask(FileTaskBO bo);

    /**
     * 单条查询-FileTask
     *
     * @param queryBo queryBo
     * @return FileTaskBO
     */
    FileTaskBO queryFileTaskSingle(FileTaskQueryBO queryBo);

    /**
     * 列表查询-FileTask
     *
     * @param queryBo queryBo
     * @return List<FileTaskBO>
     */
    List<FileTaskBO> queryFileTaskList(FileTaskQueryBO queryBo);

}

