package com.healerjean.proj.template.service.impl;

import com.healerjean.proj.template.po.FileTask;
import com.healerjean.proj.template.bo.FileTaskQueryBO;
import com.healerjean.proj.template.bo.FileTaskBO;
import com.healerjean.proj.template.manager.FileTaskManager;
import com.healerjean.proj.template.service.FileTaskService;
import com.healerjean.proj.template.converter.FileTaskConverter;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * 文件任务(FileTask)Service
 *
 * @author zhangyujin
 * @date 2023-12-13 15:21:29
 */
@Service
public class FileTaskServiceImpl implements FileTaskService {

    /**
     * fileTaskManager
     */
    @Resource
    private FileTaskManager fileTaskManager;

    /**
     * 保存-FileTask
     *
     * @param bo bo
     * @return boolean
     */
    @Override
    public boolean saveFileTask(FileTaskBO bo) {
        FileTask po = FileTaskConverter.INSTANCE.convertFileTaskBoToPo(bo);
        return fileTaskManager.saveFileTask(po);
    }

    /**
     * 删除-FileTask
     *
     * @param bo bo
     * @return boolean
     */
    @Override
    public boolean deleteFileTask(FileTaskBO bo) {
        return fileTaskManager.deleteFileTaskById(bo.getId());
    }

    /**
     * 更新-FileTask
     *
     * @param bo bo
     * @return boolean
     */
    @Override
    public boolean updateFileTask(FileTaskBO bo) {
        FileTask po = FileTaskConverter.INSTANCE.convertFileTaskBoToPo(bo);
        return fileTaskManager.updateFileTask(po);
    }

    /**
     * 单条查询-FileTask
     *
     * @param queryBo queryBo
     * @return FileTaskBO
     */
    @Override
    public FileTaskBO queryFileTaskSingle(FileTaskQueryBO queryBo) {
        return fileTaskManager.queryFileTaskSingle(queryBo);
    }

    /**
     * 列表查询-FileTask
     *
     * @param queryBo queryBo
     * @return List<FileTaskBO>
     */
    @Override
    public List<FileTaskBO> queryFileTaskList(FileTaskQueryBO queryBo) {
        return fileTaskManager.queryFileTaskList(queryBo);
    }

}

