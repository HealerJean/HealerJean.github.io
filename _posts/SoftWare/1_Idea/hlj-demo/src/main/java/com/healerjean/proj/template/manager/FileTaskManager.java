package com.healerjean.proj.template.manager;

import com.healerjean.proj.template.po.FileTask;
import com.healerjean.proj.template.bo.FileTaskBO;
import com.healerjean.proj.template.bo.PageBO;
import com.healerjean.proj.template.bo.FileTaskQueryBO;

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
     * 删除-FileTask
     *
     * @param id id
     * @return boolean
     */
    boolean deleteFileTaskById(Long id);

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
    PageBO<FileTaskBO> queryFileTaskPage(FileTaskQueryBO queryBo);

}

