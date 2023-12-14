package com.healerjean.proj.template.resource.impl;

import com.healerjean.proj.template.bo.FileTaskQueryBO;
import com.healerjean.proj.template.bo.FileTaskBO;
import com.healerjean.proj.template.dto.FileTaskDTO;
import com.healerjean.proj.template.req.FileTaskSaveReq;
import com.healerjean.proj.template.req.FileTaskQueryReq;
import com.healerjean.proj.template.req.FileTaskDeleteReq;
import com.healerjean.proj.template.service.FileTaskService;
import com.healerjean.proj.template.resource.FileTaskResource;
import com.healerjean.proj.template.converter.FileTaskConverter;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * 文件任务(FileTask)Resource接口
 *
 * @author zhangyujin
 * @date 2023-12-13 15:21:29
 */
@Service
public class FileTaskResourceImpl implements FileTaskResource {

    /**
     * fileTaskService
     */
    @Resource
    private FileTaskService fileTaskService;


    /**
     * 保存-FileTask
     *
     * @param req req
     * @return boolean
     */
    @Override
    public boolean saveFileTask(FileTaskSaveReq req) {
        FileTaskBO bo = FileTaskConverter.INSTANCE.convertFileTaskSaveReqToBo(req);
        return fileTaskService.saveFileTask(bo);
    }

    /**
     * 删除-FileTask
     *
     * @param req req
     * @return boolean
     */
    @Override
    public boolean deleteFileTask(FileTaskDeleteReq req) {
        FileTaskBO bo = FileTaskConverter.INSTANCE.convertFileTaskDeleteReqToBo(req);
        return fileTaskService.deleteFileTask(bo);
    }

    /**
     * 更新-FileTask
     *
     * @param req req
     * @return boolean
     */
    @Override
    public boolean updateFileTask(FileTaskSaveReq req) {
        FileTaskBO bo = FileTaskConverter.INSTANCE.convertFileTaskSaveReqToBo(req);
        return fileTaskService.updateFileTask(bo);
    }

    /**
     * 单条查询-FileTask
     *
     * @param req req
     * @return FileTaskDTO
     */
    @Override
    public FileTaskDTO queryFileTaskSingle(FileTaskQueryReq req) {
        FileTaskQueryBO queryBo = FileTaskConverter.INSTANCE.convertFileTaskQueryReqToBo(req);
        FileTaskBO bo = fileTaskService.queryFileTaskSingle(queryBo);
        return FileTaskConverter.INSTANCE.convertFileTaskBoToDto(bo);
    }

    /**
     * 列表查询-FileTask
     *
     * @param req req
     * @return List<FileTaskDTO>
     */
    @Override
    public List<FileTaskDTO> queryFileTaskList(FileTaskQueryReq req) {
        FileTaskQueryBO queryBo = FileTaskConverter.INSTANCE.convertFileTaskQueryReqToBo(req);
        List<FileTaskBO> bos = fileTaskService.queryFileTaskList(queryBo);
        return FileTaskConverter.INSTANCE.convertFileTaskBoToDtoList(bos);
    }

}

