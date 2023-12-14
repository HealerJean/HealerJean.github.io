package com.healerjean.proj.template.service;

import com.healerjean.proj.template.bo.FileTaskQueryBO;
import com.healerjean.proj.template.bo.FileTaskBO;

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
     * 删除-FileTask
     *
     * @param bo bo
     * @return boolean
     */
    boolean deleteFileTask(FileTaskBO bo);

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

