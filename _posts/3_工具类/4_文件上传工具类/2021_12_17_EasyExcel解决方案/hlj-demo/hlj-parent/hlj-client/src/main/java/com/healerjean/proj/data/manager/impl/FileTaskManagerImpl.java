package com.healerjean.proj.data.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.healerjean.proj.common.data.bo.PageBO;
import com.healerjean.proj.common.data.bo.PageQueryBO;
import com.healerjean.proj.data.bo.FileTaskBO;
import com.healerjean.proj.data.bo.FileTaskQueryBO;
import com.healerjean.proj.data.converter.FileTaskConverter;
import com.healerjean.proj.data.dao.FileTaskDao;
import com.healerjean.proj.data.manager.FileTaskManager;
import com.healerjean.proj.data.po.FileTask;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


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
     * 更新-FileTask
     *
     * @param po po
     * @return boolean
     */
    @Override
    public boolean updateFileTask(FileTask po) {
        LambdaUpdateWrapper<FileTask> updateWrapper = Wrappers.lambdaUpdate(FileTask.class)
                .set(StringUtils.isNotBlank(po.getUserId()), FileTask::getUserId, po.getUserId())
                .set(StringUtils.isNotBlank(po.getBusinessData()), FileTask::getBusinessData, po.getBusinessData())
                .set(StringUtils.isNotBlank(po.getTaskStatus()), FileTask::getTaskStatus, po.getTaskStatus())
                .set(StringUtils.isNotBlank(po.getResultUrl()), FileTask::getResultUrl, po.getResultUrl())
                .set(StringUtils.isNotBlank(po.getResultMessage()), FileTask::getResultMessage, po.getResultMessage())
                .set(StringUtils.isNotBlank(po.getUrl()), FileTask::getUrl, po.getUrl())
                .set(StringUtils.isNotBlank(po.getExt()), FileTask::getExt, po.getExt())

                .eq(FileTask::getTaskId, po.getTaskId());
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
                .eq(Objects.nonNull(queryBo.getId()), FileTask::getId, queryBo.getId())
                .eq(StringUtils.isNotBlank(queryBo.getUserId()), FileTask::getUserId, queryBo.getUserId())
                .eq(StringUtils.isNotBlank(queryBo.getTaskId()), FileTask::getTaskId, queryBo.getTaskId())
                .eq(StringUtils.isNotBlank(queryBo.getTaskType()), FileTask::getTaskType, queryBo.getTaskType())
                .eq(StringUtils.isNotBlank(queryBo.getBusinessType()), FileTask::getBusinessType, queryBo.getBusinessType())
                .eq(StringUtils.isNotBlank(queryBo.getTaskStatus()), FileTask::getTaskStatus, queryBo.getTaskStatus());
        List<FileTask> list = fileTaskDao.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return FileTaskConverter.INSTANCE.convertFileTaskPoToBo(list.get(0));
    }

    /**
     * 列表查询-FileTask
     *
     * @param queryBo query
     * @return List<FileTask>
     */
    @Override
    public List<FileTaskBO> queryFileTaskList(FileTaskQueryBO queryBo) {
        LambdaQueryWrapper<FileTask> queryWrapper = Wrappers.lambdaQuery(FileTask.class)
                .eq(Objects.nonNull(queryBo.getId()), FileTask::getId, queryBo.getId())
                .eq(StringUtils.isNotBlank(queryBo.getUserId()), FileTask::getUserId, queryBo.getUserId())
                .eq(StringUtils.isNotBlank(queryBo.getTaskId()), FileTask::getTaskId, queryBo.getTaskId())
                .eq(StringUtils.isNotBlank(queryBo.getTaskType()), FileTask::getTaskType, queryBo.getTaskType())
                .eq(StringUtils.isNotBlank(queryBo.getBusinessType()), FileTask::getBusinessType, queryBo.getBusinessType())
                .eq(StringUtils.isNotBlank(queryBo.getTaskStatus()), FileTask::getTaskStatus, queryBo.getTaskStatus());
        List<FileTask> pos = fileTaskDao.list(queryWrapper);
        return FileTaskConverter.INSTANCE.convertFileTaskPoToBoList(pos);
    }

    /**
     * 分页查询-FileTask
     *
     * @param pageQuery pageQuery
     * @return Page<FileTask>
     */
    @Override
    public PageBO<FileTaskBO> queryFileTaskPage(PageQueryBO<FileTaskQueryBO> pageQuery) {
        FileTaskQueryBO query = pageQuery.getData();
        LambdaQueryWrapper<FileTask> queryWrapper = Wrappers.lambdaQuery(FileTask.class)
                .eq(Objects.nonNull(query.getId()), FileTask::getId, query.getId())
                .eq(StringUtils.isNotBlank(query.getUserId()), FileTask::getUserId, query.getUserId())
                .eq(StringUtils.isNotBlank(query.getTaskId()), FileTask::getTaskId, query.getTaskId())
                .eq(StringUtils.isNotBlank(query.getTaskType()), FileTask::getTaskType, query.getTaskType())
                .eq(StringUtils.isNotBlank(query.getBusinessType()), FileTask::getBusinessType, query.getBusinessType())
                .eq(StringUtils.isNotBlank(query.getTaskStatus()), FileTask::getTaskStatus, query.getTaskStatus());
        Page<FileTask> pageReq = new Page<>(pageQuery.getCurrPage(), pageQuery.getPageSize(), pageQuery.getSearchCountFlag());
        Page<FileTask> page = fileTaskDao.page(pageReq, queryWrapper);
        PageBO<FileTaskBO> result = new PageBO<>( page.getTotal(),  page.getSize(),page.getPages(),page.getCurrent(), null);
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            result.setList(page.getRecords().stream().map(FileTaskConverter.INSTANCE::convertFileTaskPoToBo).collect(Collectors.toList()));
        }

        return result;
    }

}

