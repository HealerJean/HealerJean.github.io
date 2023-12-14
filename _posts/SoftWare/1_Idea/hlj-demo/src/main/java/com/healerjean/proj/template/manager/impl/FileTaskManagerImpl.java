package com.healerjean.proj.template.manager.impl;

import com.healerjean.proj.template.po.FileTask;
import com.healerjean.proj.template.bo.FileTaskBO;
import com.healerjean.proj.template.bo.PageBO;
import com.healerjean.proj.template.bo.FileTaskQueryBO;
import com.healerjean.proj.template.dao.FileTaskDao;
import com.healerjean.proj.template.manager.FileTaskManager;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.util.CollectionUtils;
import org.springframework.stereotype.Service;


/**
 * 文件任务(FileTask)Manager接口
 *
 * @author zhangyujin
 * @date 2023-12-13 15:21:29
 */
@Service
public class FileTaskManagerImpl implements FileTaskManager {

    /**
     * fileTaskDao
     */
    @Resource
    private FileTaskDao fileTaskDao;

    /**
     * 保存-FileTask
     *
     * @param po po
     * @return boolean
     */
    @Override
    public boolean saveFileTask(FileTask po) {
        return fileTaskDao.save(po);
    }

    /**
     * 删除-FileTask
     *
     * @param id id
     * @return boolean
     */
    @Override
    public boolean deleteFileTaskById(Long id) {
        //todo
        return false;
    }

    /**
     * 更新-FileTask
     *
     * @param po po
     * @return boolean
     */
    @Override
    public boolean updateFileTask(FileTask po) {
        LambdaUpdateWrapper<FileTask> updateWrapper = Wrappers.lambdaUpdate(FileTask.class)
                .set(FileTask::getId, po.getId())
                .set(FileTask::getUserId, po.getUserId())
                .set(FileTask::getTaskId, po.getTaskId())
                .set(FileTask::getTaskType, po.getTaskType())
                .set(FileTask::getBusinessType, po.getBusinessType())
                .set(FileTask::getBusinessData, po.getBusinessData())
                .set(FileTask::getTaskStatus, po.getTaskStatus())
                .set(FileTask::getResultUrl, po.getResultUrl())
                .set(FileTask::getResultMessage, po.getResultMessage())
                .set(FileTask::getUrl, po.getUrl())
                .set(FileTask::getExt, po.getExt())
                .set(FileTask::getCreatedTime, po.getCreatedTime())
                .set(FileTask::getModifiedTime, po.getModifiedTime())

                .eq(FileTask::getId, po.getId())
                .eq(FileTask::getUserId, po.getUserId())
                .eq(FileTask::getTaskId, po.getTaskId())
                .eq(FileTask::getTaskType, po.getTaskType())
                .eq(FileTask::getBusinessType, po.getBusinessType())
                .eq(FileTask::getBusinessData, po.getBusinessData())
                .eq(FileTask::getTaskStatus, po.getTaskStatus())
                .eq(FileTask::getResultUrl, po.getResultUrl())
                .eq(FileTask::getResultMessage, po.getResultMessage())
                .eq(FileTask::getUrl, po.getUrl())
                .eq(FileTask::getExt, po.getExt())
                .eq(FileTask::getCreatedTime, po.getCreatedTime())
                .eq(FileTask::getModifiedTime, po.getModifiedTime());
        //todo 多余删除，不足补齐
        return fileTaskDao.update(updateWrapper);
    }

    /**
     * 单条主键查询-FileTask
     *
     * @param id id
     * @return FileTask
     */
    @Override
    public FileTaskBO queryFileTaskById(Long id) {
        FileTask po = fileTaskDao.getById(id);
        return FileTaskConverter.INSTANCE.convertFileTaskPoToBo(po);
    }


    /**
     * 单条查询-FileTask
     *
     * @param queryBo queryBo
     * @return FileTask
     */
    @Override
    public FileTaskBO queryFileTaskSingle(FileTaskQueryBO queryBo) {
        LambdaQueryWrapper<FileTask> queryWrapper = Wrappers.lambdaQuery(FileTask.class)
                .eq(FileTask::getId, queryBo.getId())
                .eq(FileTask::getUserId, queryBo.getUserId())
                .eq(FileTask::getTaskId, queryBo.getTaskId())
                .eq(FileTask::getTaskType, queryBo.getTaskType())
                .eq(FileTask::getBusinessType, queryBo.getBusinessType())
                .eq(FileTask::getBusinessData, queryBo.getBusinessData())
                .eq(FileTask::getTaskStatus, queryBo.getTaskStatus())
                .eq(FileTask::getResultUrl, queryBo.getResultUrl())
                .eq(FileTask::getResultMessage, queryBo.getResultMessage())
                .eq(FileTask::getUrl, queryBo.getUrl())
                .eq(FileTask::getExt, queryBo.getExt())
                .eq(FileTask::getCreatedTime, queryBo.getCreatedTime())
                .eq(FileTask::getModifiedTime, queryBo.getModifiedTime());
        //todo 多余删除，不足补齐
        List<FileTask> list = fileTaskDao.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return FileTaskConverter.INSTANCE.convertFileTaskPoToBo(list.get(0));
    }

    /**
     * 列表查询-FileTask
     *
     * @param queryBo queryBo
     * @return List<FileTask>
     */
    @Override
    public List<FileTaskBO> queryFileTaskList(FileTaskQueryBO queryBo) {
        LambdaQueryWrapper<FileTask> queryWrapper = Wrappers.lambdaQuery(FileTask.class)
                .eq(FileTask::getId, queryBo.getId())
                .eq(FileTask::getUserId, queryBo.getUserId())
                .eq(FileTask::getTaskId, queryBo.getTaskId())
                .eq(FileTask::getTaskType, queryBo.getTaskType())
                .eq(FileTask::getBusinessType, queryBo.getBusinessType())
                .eq(FileTask::getBusinessData, queryBo.getBusinessData())
                .eq(FileTask::getTaskStatus, queryBo.getTaskStatus())
                .eq(FileTask::getResultUrl, queryBo.getResultUrl())
                .eq(FileTask::getResultMessage, queryBo.getResultMessage())
                .eq(FileTask::getUrl, queryBo.getUrl())
                .eq(FileTask::getExt, queryBo.getExt())
                .eq(FileTask::getCreatedTime, queryBo.getCreatedTime())
                .eq(FileTask::getModifiedTime, queryBo.getModifiedTime());

        List<FileTask> pos = fileTaskDao.list(queryWrapper);
        return FileTaskConverter.INSTANCE.convertFileTaskPoToBoList(pos);
    }

    /**
     * 分页查询-FileTask
     *
     * @param queryBo queryBo
     * @return Page<FileTask>
     */
    @Override
    public PageBO<FileTaskBO> queryFileTaskPage(FileTaskQueryBO queryBo) {
        LambdaQueryWrapper<FileTask> queryWrapper = Wrappers.lambdaQuery(FileTask.class)
                .eq(FileTask::getId, queryBo.getId())
                .eq(FileTask::getUserId, queryBo.getUserId())
                .eq(FileTask::getTaskId, queryBo.getTaskId())
                .eq(FileTask::getTaskType, queryBo.getTaskType())
                .eq(FileTask::getBusinessType, queryBo.getBusinessType())
                .eq(FileTask::getBusinessData, queryBo.getBusinessData())
                .eq(FileTask::getTaskStatus, queryBo.getTaskStatus())
                .eq(FileTask::getResultUrl, queryBo.getResultUrl())
                .eq(FileTask::getResultMessage, queryBo.getResultMessage())
                .eq(FileTask::getUrl, queryBo.getUrl())
                .eq(FileTask::getExt, queryBo.getExt())
                .eq(FileTask::getCreatedTime, queryBo.getCreatedTime())
                .eq(FileTask::getModifiedTime, queryBo.getModifiedTime());
        //todo 多余删除，不足补齐
        Page<FileTask> pageReq = new Page<>(0, 0, 0);
        Page<FileTask> page = fileTaskDao.page(pageReq, queryWrapper);
        PageBO<FileTaskBO> result = new PageBO<>((int) page.getTotal(), (int) page.getSize(), (int) page.getPages(), (int) page.getCurrent());
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            result.setList(page.getRecords().stream().map(FileTaskConverter.INSTANCE::convertFileTaskPoToBo).collect(Collectors.toList()));
        }

        return result;
    }

}

