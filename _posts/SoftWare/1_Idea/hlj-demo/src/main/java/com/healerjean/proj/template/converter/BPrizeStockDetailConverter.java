package com.healerjean.proj.template.converter;

import java.util.Date;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.stream.Collectors;

import com.healerjean.proj.template.po.BPrizeStockDetail;
import com.healerjean.proj.template.bo.BPrizeStockDetailBO;
import com.healerjean.proj.template.bo.BPrizeStockDetailQueryBO;
import com.healerjean.proj.template.dto.BPrizeStockDetailDTO;
import com.healerjean.proj.template.vo.BPrizeStockDetailVO;
import com.healerjean.proj.template.req.BPrizeStockDetailSaveReq;
import com.healerjean.proj.template.req.BPrizeStockDetailQueryReq;
import com.healerjean.proj.template.req.BPrizeStockDetailDeleteReq;


/**
 * 权益奖品增减库存明细表(BPrizeStockDetail)Converter
 *
 * @author zhangyujin
 * @date 2023-12-12 11:07:29
 */
@Mapper
public interface BPrizeStockDetailConverter {

    /**
     * INSTANCE
     */
    BPrizeStockDetailConverter INSTANCE = Mappers.getMapper(BPrizeStockDetailConverter.class);


    /**
     * convertBPrizeStockDetailPoToBO
     *
     * @param po po
     * @return BPrizeStockDetailBO
     */
    default BPrizeStockDetailBO convertBPrizeStockDetailPoToBo(BPrizeStockDetail po) {
        if (po == null) {
            return null;
        }
        BPrizeStockDetailBO result = new BPrizeStockDetailBO();
        result.setId(po.getId());
        result.setCreatedDate(po.getCreatedDate());
        result.setModifiedDate(po.getModifiedDate());
        result.setBenefitsId(po.getBenefitsId());
        result.setChangeType(po.getChangeType());
        result.setChangeNum(po.getChangeNum());
        result.setChangeAfterNum(po.getChangeAfterNum());
        result.setOperator(po.getOperator());
        return result;
    }


    /**
     * convertBPrizeStockDetailPoToBoLists
     *
     * @param pos pos
     * @return BPrizeStockDetailBO
     */
    default List<BPrizeStockDetailBO> convertBPrizeStockDetailPoToBoList(List<BPrizeStockDetail> pos) {
        if (CollectionUtils.isEmpty(pos)) {
            return Collections.emptyList();
        }
        return pos.stream().map(this::convertBPrizeStockDetailPoToBo).collect(Collectors.toList());
    }


    /**
     * convertBPrizeStockDetailBoToPo
     *
     * @param bo bo
     * @return BPrizeStockDetailBO
     */
    default BPrizeStockDetail convertBPrizeStockDetailBoToPo(BPrizeStockDetailBO bo) {
        if (bo == null) {
            return null;
        }
        BPrizeStockDetail result = new BPrizeStockDetail();
        result.setId(bo.getId());
        result.setCreatedDate(bo.getCreatedDate());
        result.setModifiedDate(bo.getModifiedDate());
        result.setBenefitsId(bo.getBenefitsId());
        result.setChangeType(bo.getChangeType());
        result.setChangeNum(bo.getChangeNum());
        result.setChangeAfterNum(bo.getChangeAfterNum());
        result.setOperator(bo.getOperator());
        return result;
    }

    /**
     * convertBPrizeStockDetailBoToPoList
     *
     * @param bos bos
     * @return List<BPrizeStockDetail>
     */
    default List<BPrizeStockDetail> convertBPrizeStockDetailBoToPoList(List<BPrizeStockDetailBO> bos) {
        if (CollectionUtils.isEmpty(bos)) {
            return Collections.emptyList();
        }
        return bos.stream().map(this::convertBPrizeStockDetailBoToPo).collect(Collectors.toList());
    }


    /**
     * convertBPrizeStockDetailBoToDto
     *
     * @param bo bo
     * @return BPrizeStockDetailDTO
     */
    default BPrizeStockDetailDTO convertBPrizeStockDetailBoToDto(BPrizeStockDetailBO bo) {
        if (bo == null) {
            return null;
        }
        BPrizeStockDetailDTO result = new BPrizeStockDetailDTO();
        result.setId(bo.getId());
        result.setCreatedDate(bo.getCreatedDate());
        result.setModifiedDate(bo.getModifiedDate());
        result.setBenefitsId(bo.getBenefitsId());
        result.setChangeType(bo.getChangeType());
        result.setChangeNum(bo.getChangeNum());
        result.setChangeAfterNum(bo.getChangeAfterNum());
        result.setOperator(bo.getOperator());
        return result;
    }

    /**
     * convertBPrizeStockDetailBoToDtoList
     *
     * @param bos bos
     * @return List<BPrizeStockDetailDTO>
     */
    default List<BPrizeStockDetailDTO> convertBPrizeStockDetailBoToDtoList(List<BPrizeStockDetailBO> bos) {
        if (CollectionUtils.isEmpty(bos)) {
            return Collections.emptyList();
        }
        return bos.stream().map(this::convertBPrizeStockDetailBoToDto).collect(Collectors.toList());
    }

    /**
     * convertBPrizeStockDetailDtoToBo
     *
     * @param dto dto
     * @return BPrizeStockDetailBO
     */
    default BPrizeStockDetailBO convertBPrizeStockDetailDtoToBo(BPrizeStockDetailDTO dto) {
        if (dto == null) {
            return null;
        }
        BPrizeStockDetailBO result = new BPrizeStockDetailBO();
        result.setId(dto.getId());
        result.setCreatedDate(dto.getCreatedDate());
        result.setModifiedDate(dto.getModifiedDate());
        result.setBenefitsId(dto.getBenefitsId());
        result.setChangeType(dto.getChangeType());
        result.setChangeNum(dto.getChangeNum());
        result.setChangeAfterNum(dto.getChangeAfterNum());
        result.setOperator(dto.getOperator());
        return result;
    }

    /**
     * convertBPrizeStockDetailDtoToBoList
     *
     * @param dtos dtos
     * @return BPrizeStockDetailBO
     */
    default List<BPrizeStockDetailBO> convertBPrizeStockDetailDtoToBoList(List<BPrizeStockDetailDTO> dtos) {
        if (CollectionUtils.isEmpty(dtos)) {
            return Collections.emptyList();
        }
        return dtos.stream().map(this::convertBPrizeStockDetailDtoToBo).collect(Collectors.toList());
    }


    /**
     * convertBPrizeStockDetailVO
     *
     * @param bo bo
     * @return BPrizeStockDetailVO
     */
    default BPrizeStockDetailVO convertBPrizeStockDetailBoToVo(BPrizeStockDetailBO bo) {
        if (bo == null) {
            return null;
        }
        BPrizeStockDetailVO result = new BPrizeStockDetailVO();
        result.setId(bo.getId());
        result.setCreatedDate(bo.getCreatedDate());
        result.setModifiedDate(bo.getModifiedDate());
        result.setBenefitsId(bo.getBenefitsId());
        result.setChangeType(bo.getChangeType());
        result.setChangeNum(bo.getChangeNum());
        result.setChangeAfterNum(bo.getChangeAfterNum());
        result.setOperator(bo.getOperator());
        return result;
    }

    /**
     * convertBPrizeStockDetailBoToVoList
     *
     * @param bos bos
     * @return BPrizeStockDetailVO
     */
    default List<BPrizeStockDetailVO> convertBPrizeStockDetailBoToVoList(List<BPrizeStockDetailBO> bos) {
        if (CollectionUtils.isEmpty(bos)) {
            return Collections.emptyList();
        }
        return bos.stream().map(this::convertBPrizeStockDetailBoToVo).collect(Collectors.toList());
    }

    /**
     * convertBPrizeStockDetailSaveReqToBo
     *
     * @param req req
     * @return BPrizeStockDetailBO
     */
    default BPrizeStockDetailBO convertBPrizeStockDetailSaveReqToBo(BPrizeStockDetailSaveReq req) {
        if (req == null) {
            return null;
        }
        BPrizeStockDetailBO result = new BPrizeStockDetailBO();
        result.setId(req.getId());
        result.setCreatedDate(req.getCreatedDate());
        result.setModifiedDate(req.getModifiedDate());
        result.setBenefitsId(req.getBenefitsId());
        result.setChangeType(req.getChangeType());
        result.setChangeNum(req.getChangeNum());
        result.setChangeAfterNum(req.getChangeAfterNum());
        result.setOperator(req.getOperator());
        return result;
    }

    /**
     * convertBPrizeStockDetailDeleteReqToBo
     *
     * @param req req
     * @return BPrizeStockDetailBO
     */
    default BPrizeStockDetailBO convertBPrizeStockDetailDeleteReqToBo(BPrizeStockDetailDeleteReq req) {
        if (req == null) {
            return null;
        }
        BPrizeStockDetailBO result = new BPrizeStockDetailBO();
        result.setId(req.getId());
        result.setCreatedDate(req.getCreatedDate());
        result.setModifiedDate(req.getModifiedDate());
        result.setBenefitsId(req.getBenefitsId());
        result.setChangeType(req.getChangeType());
        result.setChangeNum(req.getChangeNum());
        result.setChangeAfterNum(req.getChangeAfterNum());
        result.setOperator(req.getOperator());
        return result;
    }

    /**
     * convertBPrizeStockDetailQueryReqToBo
     *
     * @param req req
     * @return BPrizeStockDetailBO
     */
    default BPrizeStockDetailQueryBO convertBPrizeStockDetailQueryReqToBo(BPrizeStockDetailQueryReq req) {
        if (req == null) {
            return null;
        }
        BPrizeStockDetailQueryBO result = new BPrizeStockDetailQueryBO();
        result.setId(req.getId());
        result.setCreatedDate(req.getCreatedDate());
        result.setModifiedDate(req.getModifiedDate());
        result.setBenefitsId(req.getBenefitsId());
        result.setChangeType(req.getChangeType());
        result.setChangeNum(req.getChangeNum());
        result.setChangeAfterNum(req.getChangeAfterNum());
        result.setOperator(req.getOperator());
        return result;
    }

}

