package com.healerjean.proj.code.com.healerjean.proj.template.converter;

import java.util.Date;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.stream.Collectors;

import com.healerjean.proj.template.po.PreSignRecord;
import com.healerjean.proj.template.bo.PreSignRecordBO;
import com.healerjean.proj.template.bo.PreSignRecordQueryBO;
import com.healerjean.proj.template.dto.PreSignRecordDTO;
import com.healerjean.proj.template.vo.PreSignRecordVO;
import com.healerjean.proj.template.req.PreSignRecordSaveReq;
import com.healerjean.proj.template.req.PreSignRecordQueryReq;
import com.healerjean.proj.template.req.PreSignRecordDeleteReq;


/**
 * 预签约记录(PreSignRecord)Converter
 *
 * @author zhangyujin
 * @date 2024-07-10 10:12:22
 */
@Mapper
public interface PreSignRecordConverter {

    /**
     * INSTANCE
     */
    PreSignRecordConverter INSTANCE = Mappers.getMapper(PreSignRecordConverter.class);


    /**
     * convertPreSignRecordPoToBO
     *
     * @param po po
     * @return PreSignRecordBO
     */
    default PreSignRecordBO convertPreSignRecordPoToBo(PreSignRecord po) {
        if (po == null) {
            return null;
        }
        PreSignRecordBO result = new PreSignRecordBO();
        result.setId(po.getId());
        result.setInsuranceId(po.getInsuranceId());
        result.setCustomerCode(po.getCustomerCode());
        result.setCustomerType(po.getCustomerType());
        result.setBizId(po.getBizId());
        result.setType(po.getType());
        result.setSignChannelSource(po.getSignChannelSource());
        result.setStartTime(po.getStartTime());
        result.setEndTime(po.getEndTime());
        result.setStatus(po.getStatus());
        result.setSignFailReason(po.getSignFailReason());
        result.setBizInfo(po.getBizInfo());
        result.setExt(po.getExt());
        result.setVersion(po.getVersion());
        result.setPreSignTime(po.getPreSignTime());
        result.setSignUser(po.getSignUser());
        result.setRefSignUpStatusId(po.getRefSignUpStatusId());
        result.setOperateTime(po.getOperateTime());
        result.setOperator(po.getOperator());
        result.setDeleteFlag(po.getDeleteFlag());
        result.setModifiedTime(po.getModifiedTime());
        result.setCreatedTime(po.getCreatedTime());
        return result;
    }


    /**
     * convertPreSignRecordPoToBoLists
     *
     * @param pos pos
     * @return PreSignRecordBO
     */
    default List<PreSignRecordBO> convertPreSignRecordPoToBoList(List<PreSignRecord> pos) {
        if (CollectionUtils.isEmpty(pos)) {
            return Collections.emptyList();
        }
        return pos.stream().map(this::convertPreSignRecordPoToBo).collect(Collectors.toList());
    }


    /**
     * convertPreSignRecordBoToPo
     *
     * @param bo bo
     * @return PreSignRecordBO
     */
    default PreSignRecord convertPreSignRecordBoToPo(PreSignRecordBO bo) {
        if (bo == null) {
            return null;
        }
        PreSignRecord result = new PreSignRecord();
        result.setId(bo.getId());
        result.setInsuranceId(bo.getInsuranceId());
        result.setCustomerCode(bo.getCustomerCode());
        result.setCustomerType(bo.getCustomerType());
        result.setBizId(bo.getBizId());
        result.setType(bo.getType());
        result.setSignChannelSource(bo.getSignChannelSource());
        result.setStartTime(bo.getStartTime());
        result.setEndTime(bo.getEndTime());
        result.setStatus(bo.getStatus());
        result.setSignFailReason(bo.getSignFailReason());
        result.setBizInfo(bo.getBizInfo());
        result.setExt(bo.getExt());
        result.setVersion(bo.getVersion());
        result.setPreSignTime(bo.getPreSignTime());
        result.setSignUser(bo.getSignUser());
        result.setRefSignUpStatusId(bo.getRefSignUpStatusId());
        result.setOperateTime(bo.getOperateTime());
        result.setOperator(bo.getOperator());
        result.setDeleteFlag(bo.getDeleteFlag());
        result.setModifiedTime(bo.getModifiedTime());
        result.setCreatedTime(bo.getCreatedTime());
        return result;
    }

    /**
     * convertPreSignRecordBoToPoList
     *
     * @param bos bos
     * @return List<PreSignRecord>
     */
    default List<PreSignRecord> convertPreSignRecordBoToPoList(List<PreSignRecordBO> bos) {
        if (CollectionUtils.isEmpty(bos)) {
            return Collections.emptyList();
        }
        return bos.stream().map(this::convertPreSignRecordBoToPo).collect(Collectors.toList());
    }


    /**
     * convertPreSignRecordBoToDto
     *
     * @param bo bo
     * @return PreSignRecordDTO
     */
    default PreSignRecordDTO convertPreSignRecordBoToDto(PreSignRecordBO bo) {
        if (bo == null) {
            return null;
        }
        PreSignRecordDTO result = new PreSignRecordDTO();
        result.setId(bo.getId());
        result.setInsuranceId(bo.getInsuranceId());
        result.setCustomerCode(bo.getCustomerCode());
        result.setCustomerType(bo.getCustomerType());
        result.setBizId(bo.getBizId());
        result.setType(bo.getType());
        result.setSignChannelSource(bo.getSignChannelSource());
        result.setStartTime(bo.getStartTime());
        result.setEndTime(bo.getEndTime());
        result.setStatus(bo.getStatus());
        result.setSignFailReason(bo.getSignFailReason());
        result.setBizInfo(bo.getBizInfo());
        result.setExt(bo.getExt());
        result.setVersion(bo.getVersion());
        result.setPreSignTime(bo.getPreSignTime());
        result.setSignUser(bo.getSignUser());
        result.setRefSignUpStatusId(bo.getRefSignUpStatusId());
        result.setOperateTime(bo.getOperateTime());
        result.setOperator(bo.getOperator());
        result.setDeleteFlag(bo.getDeleteFlag());
        result.setModifiedTime(bo.getModifiedTime());
        result.setCreatedTime(bo.getCreatedTime());
        return result;
    }

    /**
     * convertPreSignRecordBoToDtoList
     *
     * @param bos bos
     * @return List<PreSignRecordDTO>
     */
    default List<PreSignRecordDTO> convertPreSignRecordBoToDtoList(List<PreSignRecordBO> bos) {
        if (CollectionUtils.isEmpty(bos)) {
            return Collections.emptyList();
        }
        return bos.stream().map(this::convertPreSignRecordBoToDto).collect(Collectors.toList());
    }

    /**
     * convertPreSignRecordDtoToBo
     *
     * @param dto dto
     * @return PreSignRecordBO
     */
    default PreSignRecordBO convertPreSignRecordDtoToBo(PreSignRecordDTO dto) {
        if (dto == null) {
            return null;
        }
        PreSignRecordBO result = new PreSignRecordBO();
        result.setId(dto.getId());
        result.setInsuranceId(dto.getInsuranceId());
        result.setCustomerCode(dto.getCustomerCode());
        result.setCustomerType(dto.getCustomerType());
        result.setBizId(dto.getBizId());
        result.setType(dto.getType());
        result.setSignChannelSource(dto.getSignChannelSource());
        result.setStartTime(dto.getStartTime());
        result.setEndTime(dto.getEndTime());
        result.setStatus(dto.getStatus());
        result.setSignFailReason(dto.getSignFailReason());
        result.setBizInfo(dto.getBizInfo());
        result.setExt(dto.getExt());
        result.setVersion(dto.getVersion());
        result.setPreSignTime(dto.getPreSignTime());
        result.setSignUser(dto.getSignUser());
        result.setRefSignUpStatusId(dto.getRefSignUpStatusId());
        result.setOperateTime(dto.getOperateTime());
        result.setOperator(dto.getOperator());
        result.setDeleteFlag(dto.getDeleteFlag());
        result.setModifiedTime(dto.getModifiedTime());
        result.setCreatedTime(dto.getCreatedTime());
        return result;
    }

    /**
     * convertPreSignRecordDtoToBoList
     *
     * @param dtos dtos
     * @return PreSignRecordBO
     */
    default List<PreSignRecordBO> convertPreSignRecordDtoToBoList(List<PreSignRecordDTO> dtos) {
        if (CollectionUtils.isEmpty(dtos)) {
            return Collections.emptyList();
        }
        return dtos.stream().map(this::convertPreSignRecordDtoToBo).collect(Collectors.toList());
    }


    /**
     * convertPreSignRecordVO
     *
     * @param bo bo
     * @return PreSignRecordVO
     */
    default PreSignRecordVO convertPreSignRecordBoToVo(PreSignRecordBO bo) {
        if (bo == null) {
            return null;
        }
        PreSignRecordVO result = new PreSignRecordVO();
        result.setId(bo.getId());
        result.setInsuranceId(bo.getInsuranceId());
        result.setCustomerCode(bo.getCustomerCode());
        result.setCustomerType(bo.getCustomerType());
        result.setBizId(bo.getBizId());
        result.setType(bo.getType());
        result.setSignChannelSource(bo.getSignChannelSource());
        result.setStartTime(bo.getStartTime());
        result.setEndTime(bo.getEndTime());
        result.setStatus(bo.getStatus());
        result.setSignFailReason(bo.getSignFailReason());
        result.setBizInfo(bo.getBizInfo());
        result.setExt(bo.getExt());
        result.setVersion(bo.getVersion());
        result.setPreSignTime(bo.getPreSignTime());
        result.setSignUser(bo.getSignUser());
        result.setRefSignUpStatusId(bo.getRefSignUpStatusId());
        result.setOperateTime(bo.getOperateTime());
        result.setOperator(bo.getOperator());
        result.setDeleteFlag(bo.getDeleteFlag());
        result.setModifiedTime(bo.getModifiedTime());
        result.setCreatedTime(bo.getCreatedTime());
        return result;
    }

    /**
     * convertPreSignRecordBoToVoList
     *
     * @param bos bos
     * @return PreSignRecordVO
     */
    default List<PreSignRecordVO> convertPreSignRecordBoToVoList(List<PreSignRecordBO> bos) {
        if (CollectionUtils.isEmpty(bos)) {
            return Collections.emptyList();
        }
        return bos.stream().map(this::convertPreSignRecordBoToVo).collect(Collectors.toList());
    }

    /**
     * convertPreSignRecordSaveReqToBo
     *
     * @param req req
     * @return PreSignRecordBO
     */
    default PreSignRecordBO convertPreSignRecordSaveReqToBo(PreSignRecordSaveReq req) {
        if (req == null) {
            return null;
        }
        PreSignRecordBO result = new PreSignRecordBO();
        result.setId(req.getId());
        result.setInsuranceId(req.getInsuranceId());
        result.setCustomerCode(req.getCustomerCode());
        result.setCustomerType(req.getCustomerType());
        result.setBizId(req.getBizId());
        result.setType(req.getType());
        result.setSignChannelSource(req.getSignChannelSource());
        result.setStartTime(req.getStartTime());
        result.setEndTime(req.getEndTime());
        result.setStatus(req.getStatus());
        result.setSignFailReason(req.getSignFailReason());
        result.setBizInfo(req.getBizInfo());
        result.setExt(req.getExt());
        result.setVersion(req.getVersion());
        result.setPreSignTime(req.getPreSignTime());
        result.setSignUser(req.getSignUser());
        result.setRefSignUpStatusId(req.getRefSignUpStatusId());
        result.setOperateTime(req.getOperateTime());
        result.setOperator(req.getOperator());
        result.setDeleteFlag(req.getDeleteFlag());
        result.setModifiedTime(req.getModifiedTime());
        result.setCreatedTime(req.getCreatedTime());
        return result;
    }

    /**
     * convertPreSignRecordDeleteReqToBo
     *
     * @param req req
     * @return PreSignRecordBO
     */
    default PreSignRecordBO convertPreSignRecordDeleteReqToBo(PreSignRecordDeleteReq req) {
        if (req == null) {
            return null;
        }
        PreSignRecordBO result = new PreSignRecordBO();
        result.setId(req.getId());
        result.setInsuranceId(req.getInsuranceId());
        result.setCustomerCode(req.getCustomerCode());
        result.setCustomerType(req.getCustomerType());
        result.setBizId(req.getBizId());
        result.setType(req.getType());
        result.setSignChannelSource(req.getSignChannelSource());
        result.setStartTime(req.getStartTime());
        result.setEndTime(req.getEndTime());
        result.setStatus(req.getStatus());
        result.setSignFailReason(req.getSignFailReason());
        result.setBizInfo(req.getBizInfo());
        result.setExt(req.getExt());
        result.setVersion(req.getVersion());
        result.setPreSignTime(req.getPreSignTime());
        result.setSignUser(req.getSignUser());
        result.setRefSignUpStatusId(req.getRefSignUpStatusId());
        result.setOperateTime(req.getOperateTime());
        result.setOperator(req.getOperator());
        result.setDeleteFlag(req.getDeleteFlag());
        result.setModifiedTime(req.getModifiedTime());
        result.setCreatedTime(req.getCreatedTime());
        return result;
    }

    /**
     * convertPreSignRecordQueryReqToBo
     *
     * @param req req
     * @return PreSignRecordBO
     */
    default PreSignRecordQueryBO convertPreSignRecordQueryReqToBo(PreSignRecordQueryReq req) {
        if (req == null) {
            return null;
        }
        PreSignRecordQueryBO result = new PreSignRecordQueryBO();
        result.setId(req.getId());
        result.setInsuranceId(req.getInsuranceId());
        result.setCustomerCode(req.getCustomerCode());
        result.setCustomerType(req.getCustomerType());
        result.setBizId(req.getBizId());
        result.setType(req.getType());
        result.setSignChannelSource(req.getSignChannelSource());
        result.setStartTime(req.getStartTime());
        result.setEndTime(req.getEndTime());
        result.setStatus(req.getStatus());
        result.setSignFailReason(req.getSignFailReason());
        result.setBizInfo(req.getBizInfo());
        result.setExt(req.getExt());
        result.setVersion(req.getVersion());
        result.setPreSignTime(req.getPreSignTime());
        result.setSignUser(req.getSignUser());
        result.setRefSignUpStatusId(req.getRefSignUpStatusId());
        result.setOperateTime(req.getOperateTime());
        result.setOperator(req.getOperator());
        result.setDeleteFlag(req.getDeleteFlag());
        result.setModifiedTime(req.getModifiedTime());
        result.setCreatedTime(req.getCreatedTime());
        return result;
    }

}

