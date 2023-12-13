package com.healerjean.proj.template.converter;

import java.util.Date;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.stream.Collectors;

import com.healerjean.proj.template.po.BPrizeExchangeDetail;
import com.healerjean.proj.template.bo.BPrizeExchangeDetailBO;
import com.healerjean.proj.template.bo.BPrizeExchangeDetailQueryBO;
import com.healerjean.proj.template.dto.BPrizeExchangeDetailDTO;
import com.healerjean.proj.template.vo.BPrizeExchangeDetailVO;
import com.healerjean.proj.template.req.BPrizeExchangeDetailSaveReq;
import com.healerjean.proj.template.req.BPrizeExchangeDetailQueryReq;
import com.healerjean.proj.template.req.BPrizeExchangeDetailDeleteReq;


/**
 * 权益奖品兑换明细(BPrizeExchangeDetail)Converter
 *
 * @author zhangyujin
 * @date 2023-12-12 11:07:29
 */
@Mapper
public interface BPrizeExchangeDetailConverter {

    /**
     * INSTANCE
     */
    BPrizeExchangeDetailConverter INSTANCE = Mappers.getMapper(BPrizeExchangeDetailConverter.class);


    /**
     * convertBPrizeExchangeDetailPoToBO
     *
     * @param po po
     * @return BPrizeExchangeDetailBO
     */
    default BPrizeExchangeDetailBO convertBPrizeExchangeDetailPoToBo(BPrizeExchangeDetail po) {
        if (po == null) {
            return null;
        }
        BPrizeExchangeDetailBO result = new BPrizeExchangeDetailBO();
        result.setId(po.getId());
        result.setCreatedDate(po.getCreatedDate());
        result.setModifiedDate(po.getModifiedDate());
        result.setBenefitsId(po.getBenefitsId());
        result.setVenderId(po.getVenderId());
        result.setExchangeTime(po.getExchangeTime());
        result.setBenefitsName(po.getBenefitsName());
        result.setCostScore(po.getCostScore());
        result.setExchange(po.getExchange());
        return result;
    }


    /**
     * convertBPrizeExchangeDetailPoToBoLists
     *
     * @param pos pos
     * @return BPrizeExchangeDetailBO
     */
    default List<BPrizeExchangeDetailBO> convertBPrizeExchangeDetailPoToBoList(List<BPrizeExchangeDetail> pos) {
        if (CollectionUtils.isEmpty(pos)) {
            return Collections.emptyList();
        }
        return pos.stream().map(this::convertBPrizeExchangeDetailPoToBo).collect(Collectors.toList());
    }


    /**
     * convertBPrizeExchangeDetailBoToPo
     *
     * @param bo bo
     * @return BPrizeExchangeDetailBO
     */
    default BPrizeExchangeDetail convertBPrizeExchangeDetailBoToPo(BPrizeExchangeDetailBO bo) {
        if (bo == null) {
            return null;
        }
        BPrizeExchangeDetail result = new BPrizeExchangeDetail();
        result.setId(bo.getId());
        result.setCreatedDate(bo.getCreatedDate());
        result.setModifiedDate(bo.getModifiedDate());
        result.setBenefitsId(bo.getBenefitsId());
        result.setVenderId(bo.getVenderId());
        result.setExchangeTime(bo.getExchangeTime());
        result.setBenefitsName(bo.getBenefitsName());
        result.setCostScore(bo.getCostScore());
        result.setExchange(bo.getExchange());
        return result;
    }

    /**
     * convertBPrizeExchangeDetailBoToPoList
     *
     * @param bos bos
     * @return List<BPrizeExchangeDetail>
     */
    default List<BPrizeExchangeDetail> convertBPrizeExchangeDetailBoToPoList(List<BPrizeExchangeDetailBO> bos) {
        if (CollectionUtils.isEmpty(bos)) {
            return Collections.emptyList();
        }
        return bos.stream().map(this::convertBPrizeExchangeDetailBoToPo).collect(Collectors.toList());
    }


    /**
     * convertBPrizeExchangeDetailBoToDto
     *
     * @param bo bo
     * @return BPrizeExchangeDetailDTO
     */
    default BPrizeExchangeDetailDTO convertBPrizeExchangeDetailBoToDto(BPrizeExchangeDetailBO bo) {
        if (bo == null) {
            return null;
        }
        BPrizeExchangeDetailDTO result = new BPrizeExchangeDetailDTO();
        result.setId(bo.getId());
        result.setCreatedDate(bo.getCreatedDate());
        result.setModifiedDate(bo.getModifiedDate());
        result.setBenefitsId(bo.getBenefitsId());
        result.setVenderId(bo.getVenderId());
        result.setExchangeTime(bo.getExchangeTime());
        result.setBenefitsName(bo.getBenefitsName());
        result.setCostScore(bo.getCostScore());
        result.setExchange(bo.getExchange());
        return result;
    }

    /**
     * convertBPrizeExchangeDetailBoToDtoList
     *
     * @param bos bos
     * @return List<BPrizeExchangeDetailDTO>
     */
    default List<BPrizeExchangeDetailDTO> convertBPrizeExchangeDetailBoToDtoList(List<BPrizeExchangeDetailBO> bos) {
        if (CollectionUtils.isEmpty(bos)) {
            return Collections.emptyList();
        }
        return bos.stream().map(this::convertBPrizeExchangeDetailBoToDto).collect(Collectors.toList());
    }

    /**
     * convertBPrizeExchangeDetailDtoToBo
     *
     * @param dto dto
     * @return BPrizeExchangeDetailBO
     */
    default BPrizeExchangeDetailBO convertBPrizeExchangeDetailDtoToBo(BPrizeExchangeDetailDTO dto) {
        if (dto == null) {
            return null;
        }
        BPrizeExchangeDetailBO result = new BPrizeExchangeDetailBO();
        result.setId(dto.getId());
        result.setCreatedDate(dto.getCreatedDate());
        result.setModifiedDate(dto.getModifiedDate());
        result.setBenefitsId(dto.getBenefitsId());
        result.setVenderId(dto.getVenderId());
        result.setExchangeTime(dto.getExchangeTime());
        result.setBenefitsName(dto.getBenefitsName());
        result.setCostScore(dto.getCostScore());
        result.setExchange(dto.getExchange());
        return result;
    }

    /**
     * convertBPrizeExchangeDetailDtoToBoList
     *
     * @param dtos dtos
     * @return BPrizeExchangeDetailBO
     */
    default List<BPrizeExchangeDetailBO> convertBPrizeExchangeDetailDtoToBoList(List<BPrizeExchangeDetailDTO> dtos) {
        if (CollectionUtils.isEmpty(dtos)) {
            return Collections.emptyList();
        }
        return dtos.stream().map(this::convertBPrizeExchangeDetailDtoToBo).collect(Collectors.toList());
    }


    /**
     * convertBPrizeExchangeDetailVO
     *
     * @param bo bo
     * @return BPrizeExchangeDetailVO
     */
    default BPrizeExchangeDetailVO convertBPrizeExchangeDetailBoToVo(BPrizeExchangeDetailBO bo) {
        if (bo == null) {
            return null;
        }
        BPrizeExchangeDetailVO result = new BPrizeExchangeDetailVO();
        result.setId(bo.getId());
        result.setCreatedDate(bo.getCreatedDate());
        result.setModifiedDate(bo.getModifiedDate());
        result.setBenefitsId(bo.getBenefitsId());
        result.setVenderId(bo.getVenderId());
        result.setExchangeTime(bo.getExchangeTime());
        result.setBenefitsName(bo.getBenefitsName());
        result.setCostScore(bo.getCostScore());
        result.setExchange(bo.getExchange());
        return result;
    }

    /**
     * convertBPrizeExchangeDetailBoToVoList
     *
     * @param bos bos
     * @return BPrizeExchangeDetailVO
     */
    default List<BPrizeExchangeDetailVO> convertBPrizeExchangeDetailBoToVoList(List<BPrizeExchangeDetailBO> bos) {
        if (CollectionUtils.isEmpty(bos)) {
            return Collections.emptyList();
        }
        return bos.stream().map(this::convertBPrizeExchangeDetailBoToVo).collect(Collectors.toList());
    }

    /**
     * convertBPrizeExchangeDetailSaveReqToBo
     *
     * @param req req
     * @return BPrizeExchangeDetailBO
     */
    default BPrizeExchangeDetailBO convertBPrizeExchangeDetailSaveReqToBo(BPrizeExchangeDetailSaveReq req) {
        if (req == null) {
            return null;
        }
        BPrizeExchangeDetailBO result = new BPrizeExchangeDetailBO();
        result.setId(req.getId());
        result.setCreatedDate(req.getCreatedDate());
        result.setModifiedDate(req.getModifiedDate());
        result.setBenefitsId(req.getBenefitsId());
        result.setVenderId(req.getVenderId());
        result.setExchangeTime(req.getExchangeTime());
        result.setBenefitsName(req.getBenefitsName());
        result.setCostScore(req.getCostScore());
        result.setExchange(req.getExchange());
        return result;
    }

    /**
     * convertBPrizeExchangeDetailDeleteReqToBo
     *
     * @param req req
     * @return BPrizeExchangeDetailBO
     */
    default BPrizeExchangeDetailBO convertBPrizeExchangeDetailDeleteReqToBo(BPrizeExchangeDetailDeleteReq req) {
        if (req == null) {
            return null;
        }
        BPrizeExchangeDetailBO result = new BPrizeExchangeDetailBO();
        result.setId(req.getId());
        result.setCreatedDate(req.getCreatedDate());
        result.setModifiedDate(req.getModifiedDate());
        result.setBenefitsId(req.getBenefitsId());
        result.setVenderId(req.getVenderId());
        result.setExchangeTime(req.getExchangeTime());
        result.setBenefitsName(req.getBenefitsName());
        result.setCostScore(req.getCostScore());
        result.setExchange(req.getExchange());
        return result;
    }

    /**
     * convertBPrizeExchangeDetailQueryReqToBo
     *
     * @param req req
     * @return BPrizeExchangeDetailBO
     */
    default BPrizeExchangeDetailQueryBO convertBPrizeExchangeDetailQueryReqToBo(BPrizeExchangeDetailQueryReq req) {
        if (req == null) {
            return null;
        }
        BPrizeExchangeDetailQueryBO result = new BPrizeExchangeDetailQueryBO();
        result.setId(req.getId());
        result.setCreatedDate(req.getCreatedDate());
        result.setModifiedDate(req.getModifiedDate());
        result.setBenefitsId(req.getBenefitsId());
        result.setVenderId(req.getVenderId());
        result.setExchangeTime(req.getExchangeTime());
        result.setBenefitsName(req.getBenefitsName());
        result.setCostScore(req.getCostScore());
        result.setExchange(req.getExchange());
        return result;
    }

}

