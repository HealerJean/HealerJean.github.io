package com.healerjean.proj.data.converter;

import com.healerjean.proj.data.bo.EsTradeLogBO;
import com.healerjean.proj.data.dto.EsTradeLogDTO;
import com.healerjean.proj.data.po.EsTradeLog;
import com.healerjean.proj.data.vo.EsTradeLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 订单险种投保日志(CkOrderLog)Converter
 *
 * @author zhangyujin
 * @date 2024-03-20 15:56:50
 */
@Mapper
public interface TradeLogConverter {

    /**
     * INSTANCE
     */
    TradeLogConverter INSTANCE = Mappers.getMapper(TradeLogConverter.class);


    /**
     * convertCkOrderLogPoToBO
     *
     * @param po po
     * @return CkOrderLogBO
     */
    default EsTradeLogBO convertOrderLogPoToBo(EsTradeLog po) {
        if (po == null) {
            return null;
        }
        EsTradeLogBO result = new EsTradeLogBO();
        result.setUuid(po.getUuid());
        result.setTraceSortId(po.getTraceSortId());
        result.setNodeSortId(po.getNodeSortId());
        result.setInsuranceId(po.getInsuranceId());
        result.setOrderId(po.getOrderId());
        result.setPolicyNo(po.getPolicyNo());
        result.setCustomType(po.getCustomType());
        result.setCustomCode(po.getCustomCode());
        result.setInValidateTime(po.getInValidateTime());
        result.setType(po.getType());
        result.setNode(po.getNode());
        result.setBizDesc(po.getBizDesc());
        result.setBizData(po.getBizData());
        result.setModifiedTime(po.getModifiedTime());
        result.setCreatedTime(po.getCreatedTime());
        return result;
    }


    /**
     * convertCkOrderLogPoToBoLists
     *
     * @param pos pos
     * @return CkOrderLogBO
     */
    default List<EsTradeLogBO> convertTradeLogPoToBoList(List<EsTradeLog> pos) {
        if (CollectionUtils.isEmpty(pos)) {
            return Collections.emptyList();
        }
        return pos.stream().map(this::convertOrderLogPoToBo).collect(Collectors.toList());
    }



    /**
     * convertCkOrderLogBoToDto
     *
     * @param bo bo
     * @return CkOrderLogDTO
     */
    default EsTradeLogVO convertCkOrderLogBoToDto(EsTradeLogBO bo) {
        if (bo == null) {
            return null;
        }
        EsTradeLogVO result = new EsTradeLogVO();
        result.setUuid(bo.getUuid());
        result.setTraceSortId(bo.getTraceSortId());
        result.setNodeSortId(bo.getNodeSortId());
        result.setInsuranceId(bo.getInsuranceId());
        result.setOrderId(bo.getOrderId());
        result.setPolicyNo(bo.getPolicyNo());
        result.setCustomType(bo.getCustomType());
        result.setCustomCode(bo.getCustomCode());
        result.setInValidateTime(bo.getInValidateTime());
        result.setType(bo.getType());
        result.setNode(bo.getNode());
        result.setBizDesc(bo.getBizDesc());
        result.setBizData(bo.getBizData());
        result.setModifiedTime(bo.getModifiedTime());
        result.setCreatedTime(bo.getCreatedTime());
        return result;
    }

    /**
     * convertCkOrderLogBoToDtoList
     *
     * @param bos bos
     * @return List<CkOrderLogDTO>
     */
    default List<EsTradeLogVO> convertCkOrderLogBoToDtoList(List<EsTradeLogBO> bos) {
        if (CollectionUtils.isEmpty(bos)) {
            return Collections.emptyList();
        }
        return bos.stream().map(this::convertCkOrderLogBoToDto).sorted(Comparator.comparing(EsTradeLogVO::getTraceSortId)).collect(Collectors.toList());
    }




    /**
     *
     * @param dto dto
     * @return CkTradeLog
     */
    default EsTradeLog convertCkOrderLogPoToDto(EsTradeLogVO dto){
        if (dto == null) {
            return null;
        }
        EsTradeLog result = new EsTradeLog();
        result.setUuid(dto.getUuid());
        result.setTraceSortId(dto.getTraceSortId());
        result.setNodeSortId(dto.getNodeSortId());
        result.setInsuranceId(dto.getInsuranceId());
        result.setOrderId(dto.getOrderId());
        result.setPolicyNo(dto.getPolicyNo());
        result.setCustomType(dto.getCustomType());
        result.setCustomCode(dto.getCustomCode());
        result.setInValidateTime(dto.getInValidateTime());
        result.setType(dto.getType());
        result.setNode(dto.getNode());
        result.setBizDesc(dto.getBizDesc());
        result.setBizData(dto.getBizData());
        result.setModifiedTime(dto.getModifiedTime());
        result.setCreatedTime(dto.getCreatedTime());
        return result;
    }

    /**
     * convertTradeLogDtoToPo
     *
     * @param tradeLogs tradeLogs
     * @return List<CkTradeLog>
     */
    default List<EsTradeLog> convertTradeLogBoToPoList(List<EsTradeLogBO> tradeLogs) {
        if (CollectionUtils.isEmpty(tradeLogs)){
            return Collections.emptyList();
        }

       return tradeLogs.stream().map(this::convertTradeLogBoToPo).collect(Collectors.toList());
    }

    /**
     * convertTradeLogBoToPo
     *
     * @param esTradeLog esTradeLog
     * @return {@link EsTradeLog}
     */
    default EsTradeLog convertTradeLogBoToPo(EsTradeLogBO esTradeLog){
        if (Objects.isNull(esTradeLog)){
            return null;
        }
        EsTradeLog result = new EsTradeLog();
        result.setTraceSortId(esTradeLog.getTraceSortId());
        result.setNodeSortId(esTradeLog.getNodeSortId());
        result.setInsuranceId(esTradeLog.getInsuranceId());
        result.setOrderId(esTradeLog.getOrderId());
        result.setPolicyNo(esTradeLog.getPolicyNo());
        result.setCustomType(esTradeLog.getCustomType());
        result.setCustomCode(esTradeLog.getCustomCode());
        result.setInValidateTime(esTradeLog.getInValidateTime());
        result.setType(esTradeLog.getType());
        result.setNode(esTradeLog.getNode());
        result.setBizDesc(esTradeLog.getBizDesc());
        result.setBizData(esTradeLog.getBizData());
        result.setModifiedTime(esTradeLog.getModifiedTime());
        result.setCreatedTime(esTradeLog.getCreatedTime());
        result.setUuid(esTradeLog.getUuid());
        return result;

    }

    /**
     * convertTradeLogDtoToPo
     *
     * @param tradeLog tradeLog
     * @return List<CkTradeLog>
     */
    default EsTradeLog convertTradeLogVoToPo(EsTradeLogVO tradeLog) {
        if (Objects.isNull(tradeLog)){
            return null;
        }
        EsTradeLog result = new EsTradeLog();
        result.setUuid(tradeLog.getUuid());
        result.setTraceSortId(tradeLog.getTraceSortId());
        result.setNodeSortId(tradeLog.getNodeSortId());
        result.setInsuranceId(tradeLog.getInsuranceId());
        result.setOrderId(tradeLog.getOrderId());
        result.setPolicyNo(tradeLog.getPolicyNo());
        result.setCustomType(tradeLog.getCustomType());
        result.setCustomCode(tradeLog.getCustomCode());
        result.setInValidateTime(tradeLog.getInValidateTime());
        result.setType(tradeLog.getType());
        result.setNode(tradeLog.getNode());
        result.setBizDesc(tradeLog.getBizDesc());
        result.setBizData(tradeLog.getBizData());
        result.setModifiedTime(tradeLog.getModifiedTime());
        result.setCreatedTime(tradeLog.getCreatedTime());
        return result;

    }


    /**
     * convertTradeLogDtoToBoList
     *
     * @param tradeLogs tradeLogs
     * @return {@link List< EsTradeLogBO>}
     */
    default List<EsTradeLogBO> convertTradeLogDtoToBoList(List<EsTradeLogDTO> tradeLogs){

        if (CollectionUtils.isEmpty(tradeLogs)){
            return Collections.emptyList();
        }

        return tradeLogs.stream().map(this::convertTradeLogDtoToBo).collect(Collectors.toList());
    }

    /**
     * convertTradeLogDtoToBo
     *
     * @param dto dto
     * @return {@link EsTradeLogBO}
     */
    default EsTradeLogBO convertTradeLogDtoToBo(EsTradeLogDTO dto){

        if (Objects.isNull(dto)){
            return null;
        }
        EsTradeLogBO result = new EsTradeLogBO();
        result.setTraceSortId(dto.getTraceSortId());
        result.setNodeSortId(dto.getNodeSortId());
        result.setInsuranceId(dto.getInsuranceId());
        result.setOrderId(dto.getOrderId());
        result.setPolicyNo(dto.getPolicyNo());
        result.setCustomType(dto.getCustomType());
        result.setCustomCode(dto.getCustomCode());
        result.setInValidateTime(dto.getInValidateTime());
        result.setType(dto.getType());
        result.setNode(dto.getNode());
        result.setBizDesc(dto.getBizDesc());
        result.setBizData(dto.getBizData());
        result.setModifiedTime(dto.getModifiedTime());
        result.setCreatedTime(dto.getCreatedTime());
        result.setUuid(dto.getUuid());
        return result;

    }
}

