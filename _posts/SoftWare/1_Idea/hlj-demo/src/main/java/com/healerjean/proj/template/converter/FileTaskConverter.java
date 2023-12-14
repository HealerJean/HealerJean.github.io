package com.healerjean.proj.template.converter;

import java.util.Date;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.stream.Collectors;

import com.healerjean.proj.template.po.FileTask;
import com.healerjean.proj.template.bo.FileTaskBO;
import com.healerjean.proj.template.bo.FileTaskQueryBO;
import com.healerjean.proj.template.dto.FileTaskDTO;
import com.healerjean.proj.template.vo.FileTaskVO;
import com.healerjean.proj.template.req.FileTaskSaveReq;
import com.healerjean.proj.template.req.FileTaskQueryReq;
import com.healerjean.proj.template.req.FileTaskDeleteReq;


/**
 * 文件任务(FileTask)Converter
 *
 * @author zhangyujin
 * @date 2023-12-13 15:21:28
 */
@Mapper
public interface FileTaskConverter {

    /**
     * INSTANCE
     */
    FileTaskConverter INSTANCE = Mappers.getMapper(FileTaskConverter.class);


    /**
     * convertFileTaskPoToBO
     *
     * @param po po
     * @return FileTaskBO
     */
    default FileTaskBO convertFileTaskPoToBo(FileTask po) {
        if (po == null) {
            return null;
        }
        FileTaskBO result = new FileTaskBO();
        result.setId(po.getId());
        result.setUserId(po.getUserId());
        result.setTaskId(po.getTaskId());
        result.setTaskType(po.getTaskType());
        result.setBusinessType(po.getBusinessType());
        result.setBusinessData(po.getBusinessData());
        result.setTaskStatus(po.getTaskStatus());
        result.setResultUrl(po.getResultUrl());
        result.setResultMessage(po.getResultMessage());
        result.setUrl(po.getUrl());
        result.setExt(po.getExt());
        result.setCreatedTime(po.getCreatedTime());
        result.setModifiedTime(po.getModifiedTime());
        return result;
    }


    /**
     * convertFileTaskPoToBoLists
     *
     * @param pos pos
     * @return FileTaskBO
     */
    default List<FileTaskBO> convertFileTaskPoToBoList(List<FileTask> pos) {
        if (CollectionUtils.isEmpty(pos)) {
            return Collections.emptyList();
        }
        return pos.stream().map(this::convertFileTaskPoToBo).collect(Collectors.toList());
    }


    /**
     * convertFileTaskBoToPo
     *
     * @param bo bo
     * @return FileTaskBO
     */
    default FileTask convertFileTaskBoToPo(FileTaskBO bo) {
        if (bo == null) {
            return null;
        }
        FileTask result = new FileTask();
        result.setId(bo.getId());
        result.setUserId(bo.getUserId());
        result.setTaskId(bo.getTaskId());
        result.setTaskType(bo.getTaskType());
        result.setBusinessType(bo.getBusinessType());
        result.setBusinessData(bo.getBusinessData());
        result.setTaskStatus(bo.getTaskStatus());
        result.setResultUrl(bo.getResultUrl());
        result.setResultMessage(bo.getResultMessage());
        result.setUrl(bo.getUrl());
        result.setExt(bo.getExt());
        result.setCreatedTime(bo.getCreatedTime());
        result.setModifiedTime(bo.getModifiedTime());
        return result;
    }

    /**
     * convertFileTaskBoToPoList
     *
     * @param bos bos
     * @return List<FileTask>
     */
    default List<FileTask> convertFileTaskBoToPoList(List<FileTaskBO> bos) {
        if (CollectionUtils.isEmpty(bos)) {
            return Collections.emptyList();
        }
        return bos.stream().map(this::convertFileTaskBoToPo).collect(Collectors.toList());
    }


    /**
     * convertFileTaskBoToDto
     *
     * @param bo bo
     * @return FileTaskDTO
     */
    default FileTaskDTO convertFileTaskBoToDto(FileTaskBO bo) {
        if (bo == null) {
            return null;
        }
        FileTaskDTO result = new FileTaskDTO();
        result.setId(bo.getId());
        result.setUserId(bo.getUserId());
        result.setTaskId(bo.getTaskId());
        result.setTaskType(bo.getTaskType());
        result.setBusinessType(bo.getBusinessType());
        result.setBusinessData(bo.getBusinessData());
        result.setTaskStatus(bo.getTaskStatus());
        result.setResultUrl(bo.getResultUrl());
        result.setResultMessage(bo.getResultMessage());
        result.setUrl(bo.getUrl());
        result.setExt(bo.getExt());
        result.setCreatedTime(bo.getCreatedTime());
        result.setModifiedTime(bo.getModifiedTime());
        return result;
    }

    /**
     * convertFileTaskBoToDtoList
     *
     * @param bos bos
     * @return List<FileTaskDTO>
     */
    default List<FileTaskDTO> convertFileTaskBoToDtoList(List<FileTaskBO> bos) {
        if (CollectionUtils.isEmpty(bos)) {
            return Collections.emptyList();
        }
        return bos.stream().map(this::convertFileTaskBoToDto).collect(Collectors.toList());
    }

    /**
     * convertFileTaskDtoToBo
     *
     * @param dto dto
     * @return FileTaskBO
     */
    default FileTaskBO convertFileTaskDtoToBo(FileTaskDTO dto) {
        if (dto == null) {
            return null;
        }
        FileTaskBO result = new FileTaskBO();
        result.setId(dto.getId());
        result.setUserId(dto.getUserId());
        result.setTaskId(dto.getTaskId());
        result.setTaskType(dto.getTaskType());
        result.setBusinessType(dto.getBusinessType());
        result.setBusinessData(dto.getBusinessData());
        result.setTaskStatus(dto.getTaskStatus());
        result.setResultUrl(dto.getResultUrl());
        result.setResultMessage(dto.getResultMessage());
        result.setUrl(dto.getUrl());
        result.setExt(dto.getExt());
        result.setCreatedTime(dto.getCreatedTime());
        result.setModifiedTime(dto.getModifiedTime());
        return result;
    }

    /**
     * convertFileTaskDtoToBoList
     *
     * @param dtos dtos
     * @return FileTaskBO
     */
    default List<FileTaskBO> convertFileTaskDtoToBoList(List<FileTaskDTO> dtos) {
        if (CollectionUtils.isEmpty(dtos)) {
            return Collections.emptyList();
        }
        return dtos.stream().map(this::convertFileTaskDtoToBo).collect(Collectors.toList());
    }


    /**
     * convertFileTaskVO
     *
     * @param bo bo
     * @return FileTaskVO
     */
    default FileTaskVO convertFileTaskBoToVo(FileTaskBO bo) {
        if (bo == null) {
            return null;
        }
        FileTaskVO result = new FileTaskVO();
        result.setId(bo.getId());
        result.setUserId(bo.getUserId());
        result.setTaskId(bo.getTaskId());
        result.setTaskType(bo.getTaskType());
        result.setBusinessType(bo.getBusinessType());
        result.setBusinessData(bo.getBusinessData());
        result.setTaskStatus(bo.getTaskStatus());
        result.setResultUrl(bo.getResultUrl());
        result.setResultMessage(bo.getResultMessage());
        result.setUrl(bo.getUrl());
        result.setExt(bo.getExt());
        result.setCreatedTime(bo.getCreatedTime());
        result.setModifiedTime(bo.getModifiedTime());
        return result;
    }

    /**
     * convertFileTaskBoToVoList
     *
     * @param bos bos
     * @return FileTaskVO
     */
    default List<FileTaskVO> convertFileTaskBoToVoList(List<FileTaskBO> bos) {
        if (CollectionUtils.isEmpty(bos)) {
            return Collections.emptyList();
        }
        return bos.stream().map(this::convertFileTaskBoToVo).collect(Collectors.toList());
    }

    /**
     * convertFileTaskSaveReqToBo
     *
     * @param req req
     * @return FileTaskBO
     */
    default FileTaskBO convertFileTaskSaveReqToBo(FileTaskSaveReq req) {
        if (req == null) {
            return null;
        }
        FileTaskBO result = new FileTaskBO();
        result.setId(req.getId());
        result.setUserId(req.getUserId());
        result.setTaskId(req.getTaskId());
        result.setTaskType(req.getTaskType());
        result.setBusinessType(req.getBusinessType());
        result.setBusinessData(req.getBusinessData());
        result.setTaskStatus(req.getTaskStatus());
        result.setResultUrl(req.getResultUrl());
        result.setResultMessage(req.getResultMessage());
        result.setUrl(req.getUrl());
        result.setExt(req.getExt());
        result.setCreatedTime(req.getCreatedTime());
        result.setModifiedTime(req.getModifiedTime());
        return result;
    }

    /**
     * convertFileTaskDeleteReqToBo
     *
     * @param req req
     * @return FileTaskBO
     */
    default FileTaskBO convertFileTaskDeleteReqToBo(FileTaskDeleteReq req) {
        if (req == null) {
            return null;
        }
        FileTaskBO result = new FileTaskBO();
        result.setId(req.getId());
        result.setUserId(req.getUserId());
        result.setTaskId(req.getTaskId());
        result.setTaskType(req.getTaskType());
        result.setBusinessType(req.getBusinessType());
        result.setBusinessData(req.getBusinessData());
        result.setTaskStatus(req.getTaskStatus());
        result.setResultUrl(req.getResultUrl());
        result.setResultMessage(req.getResultMessage());
        result.setUrl(req.getUrl());
        result.setExt(req.getExt());
        result.setCreatedTime(req.getCreatedTime());
        result.setModifiedTime(req.getModifiedTime());
        return result;
    }

    /**
     * convertFileTaskQueryReqToBo
     *
     * @param req req
     * @return FileTaskBO
     */
    default FileTaskQueryBO convertFileTaskQueryReqToBo(FileTaskQueryReq req) {
        if (req == null) {
            return null;
        }
        FileTaskQueryBO result = new FileTaskQueryBO();
        result.setId(req.getId());
        result.setUserId(req.getUserId());
        result.setTaskId(req.getTaskId());
        result.setTaskType(req.getTaskType());
        result.setBusinessType(req.getBusinessType());
        result.setBusinessData(req.getBusinessData());
        result.setTaskStatus(req.getTaskStatus());
        result.setResultUrl(req.getResultUrl());
        result.setResultMessage(req.getResultMessage());
        result.setUrl(req.getUrl());
        result.setExt(req.getExt());
        result.setCreatedTime(req.getCreatedTime());
        result.setModifiedTime(req.getModifiedTime());
        return result;
    }

}

