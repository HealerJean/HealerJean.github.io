package com.healerjean.proj.template.resource;

import com.healerjean.proj.template.dto.FileTaskDTO;
import com.healerjean.proj.template.req.FileTaskSaveReq;
import com.healerjean.proj.template.req.FileTaskQueryReq;
import com.healerjean.proj.template.req.FileTaskDeleteReq;

import java.util.List;

/**
 * 文件任务(FileTask)Service接口
 *
 * @author zhangyujin
 * @date 2023-12-13 15:21:29
 */
public interface FileTaskResource {

    /**
     * 保存-FileTask
     *
     * @param req req
     * @return boolean
     */
    boolean saveFileTask(FileTaskSaveReq req);

    /**
     * 删除-FileTask
     *
     * @param req req
     * @return boolean
     */
    boolean deleteFileTask(FileTaskDeleteReq req);

    /**
     * 更新-FileTask
     *
     * @param req req
     * @return boolean
     */
    boolean updateFileTask(FileTaskSaveReq req);

    /**
     * 单条查询-FileTask
     *
     * @param req req
     * @return FileTaskDTO
     */
    FileTaskDTO queryFileTaskSingle(FileTaskQueryReq req);

    /**
     * 列表查询-FileTask
     *
     * @param req req
     * @return List<FileTaskDTO>
     */
    List<FileTaskDTO> queryFileTaskList(FileTaskQueryReq req);

}

