package com.healerjean.proj.common.data.convert;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.healerjean.proj.common.data.bo.OrderByBO;
import com.healerjean.proj.common.data.bo.PageBO;
import com.healerjean.proj.common.data.bo.PageQueryBO;
import com.healerjean.proj.common.data.dto.OrderByDTO;
import com.healerjean.proj.common.data.dto.PageQueryDTO;
import com.healerjean.proj.common.data.vo.PageVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author zhangyujin
 * @date 2023/6/14  13:56.
 */
@Mapper
public interface PageCoverter {

    /**
     * 实例
     */
    PageCoverter INSTANCE = Mappers.getMapper(PageCoverter.class);


    /**
     * covertPageBoToVo
     *
     * @param pageBo pageBo
     * @param <T>    <T>
     * @return PageVO
     */
    default <T> PageVO<T> covertPageBoToVo(PageBO<?> pageBo, List<T> list) {
        return new PageVO<>(pageBo.getTotalCount(), pageBo.getPageSize(), pageBo.getTotalPage(), pageBo.getCurrPage(), list);
    }


    /**
     * covertPageBoToVo
     *
     * @param iPage iPage
     * @param list  list
     * @param <T>   <T>
     * @return PageVO
     */
    default <T> PageBO<T> covertPageBoToBo(Page<?> iPage, List<T> list) {
        return new PageBO<>(iPage.getTotal(), iPage.getSize(), iPage.getPages(), iPage.getCurrent(), list);
    }


    /**
     * pageQueryDtoToBo
     *
     * @param pageQuery pageQuery
     * @return PageQueryBO
     */
    default <T> PageQueryBO<T> pageQueryDtoToBo(PageQueryDTO pageQuery) {
        if (Objects.isNull(pageQuery)) {
            pageQuery = new PageQueryDTO();
        }
        PageQueryBO<T> result = new PageQueryBO<>();
        if (Objects.nonNull(pageQuery.getCurrPage())) {
            result.setCurrPage(pageQuery.getCurrPage());
        }
        if (Objects.nonNull(pageQuery.getPageSize())) {
            result.setPageSize(pageQuery.getPageSize());
        }
        return result;
    }


    /**
     * coverOrderByDtoToBo
     *
     * @param orderByBo orderByBo
     * @return PageQueryBO.OrderByBO
     */
    default OrderByBO coverOrderByDtoToBo(OrderByDTO orderByBo) {
        OrderByBO orderByBO = new OrderByBO();
        orderByBO.setProperty(orderByBo.getProperty());
        orderByBO.setDirection(orderByBo.getDirection());
        return orderByBO;
    }

    /**
     * coverOrderByDtoToBoList
     *
     * @param orderByBos orderByBos
     * @return List<PageQueryBO.OrderByBO>
     */
    default List<OrderByBO> coverOrderByDtoToBoList(List<OrderByDTO> orderByBos) {
        if (CollectionUtils.isEmpty(orderByBos)) {
            return Collections.emptyList();
        }
        return orderByBos.stream().map(this::coverOrderByDtoToBo).collect(Collectors.toList());
    }


}
