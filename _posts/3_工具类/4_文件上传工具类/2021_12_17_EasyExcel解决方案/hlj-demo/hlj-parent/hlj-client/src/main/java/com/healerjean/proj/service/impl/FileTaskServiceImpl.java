package com.healerjean.proj.service.impl;

import com.healerjean.proj.data.bo.FileTaskBO;
import com.healerjean.proj.data.bo.FileTaskQueryBO;
import com.healerjean.proj.data.converter.FileTaskConverter;
import com.healerjean.proj.data.manager.FileTaskManager;
import com.healerjean.proj.data.po.FileTask;
import com.healerjean.proj.service.FileTaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
        boolean flag =  fileTaskManager.saveFileTask(po);
        po.setId(bo.getId());
        return flag;
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

