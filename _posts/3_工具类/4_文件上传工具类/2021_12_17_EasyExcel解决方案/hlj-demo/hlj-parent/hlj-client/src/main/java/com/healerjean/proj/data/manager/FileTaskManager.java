package com.healerjean.proj.data.manager;


import com.healerjean.proj.common.data.bo.PageBO;
import com.healerjean.proj.common.data.bo.PageQueryBO;
import com.healerjean.proj.data.bo.FileTaskBO;
import com.healerjean.proj.data.bo.FileTaskQueryBO;
import com.healerjean.proj.data.po.FileTask;

import java.util.List;

/**
 * 文件任务(FileTask)Manager接口
 *
 * @author zhangyujin
 * @date 2023-12-13 15:21:29
 */
public interface FileTaskManager {

    /**
     * 保存-FileTask
     *
     * @param po po
     * @return boolean
     */
    boolean saveFileTask(FileTask po);


    /**
     * 更新-FileTask
     *
     * @param po po
     * @return boolean
     */
    boolean updateFileTask(FileTask po);

    /**
     * 单条主键查询-FileTask
     *
     * @param id id
     * @return FileTask
     */
    FileTaskBO queryFileTaskById(Long id);


    /**
     * 单条查询-FileTask
     *
     * @param queryBo queryBo
     * @return FileTask
     */
    FileTaskBO queryFileTaskSingle(FileTaskQueryBO queryBo);

    /**
     * 列表查询-FileTask
     *
     * @param queryBo queryBo
     * @return List<FileTask>
     */
    List<FileTaskBO> queryFileTaskList(FileTaskQueryBO queryBo);

    /**
     * 分页查询-FileTask
     *
     * @param queryBo queryBo
     * @return Page<FileTask>
     */
    PageBO<FileTaskBO> queryFileTaskPage(PageQueryBO<FileTaskQueryBO> queryBo);

}

