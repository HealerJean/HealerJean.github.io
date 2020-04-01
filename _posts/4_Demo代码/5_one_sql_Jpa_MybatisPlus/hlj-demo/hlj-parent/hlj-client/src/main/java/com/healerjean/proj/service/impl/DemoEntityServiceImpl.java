package com.healerjean.proj.service.impl;

import com.healerjean.proj.common.dto.page.PageDTO;
import com.healerjean.proj.common.enums.StatusEnum;
import com.healerjean.proj.dao.mapper.DemoEntityMapper;
import com.healerjean.proj.dao.repository.DemoEntityRepository;
import com.healerjean.proj.dto.DemoDTO;
import com.healerjean.proj.pojo.DemoEntity;
import com.healerjean.proj.pojo.DemoEntityQuery;
import com.healerjean.proj.service.DemoEntityService;
import com.healerjean.proj.utils.BeanUtils;
import com.healerjean.proj.utils.EmptyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @Desc:
 * @Author HealerJean
 * @Date 2018/9/17  下午2:39.
 */
@Service
@Slf4j
public class DemoEntityServiceImpl implements DemoEntityService {


    @Resource
    private DemoEntityMapper demoEntityMapper;

    @Resource
    private DemoEntityRepository demoEntityRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public DemoDTO jpaInsert(DemoDTO demoDTO) {
        DemoEntity demoEntity = BeanUtils.dtoToDemo(demoDTO);
        demoEntity.setStatus(StatusEnum.生效.code);
        demoEntityRepository.save(demoEntity);
        demoDTO.setId(demoEntity.getId());
        return demoDTO;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public DemoDTO mybatisPlusInsert(DemoDTO demoDTO) {
        DemoEntity demoEntity = BeanUtils.dtoToDemo(demoDTO);
        demoEntity.setStatus(StatusEnum.生效.code);
        demoEntityMapper.insert(demoEntity);
        demoDTO.setId(demoEntity.getId());
        return demoDTO;
    }

    @Override
    public DemoDTO jpaFindById(Long id) {
        DemoEntity demoEntity = demoEntityRepository.findById(id).orElse(null);
        return demoEntity == null ? null : BeanUtils.demoToDTO(demoEntity);
    }

    @Override
    public DemoDTO mybatisPlusFindById(Long id) {
        DemoEntity demoEntity = demoEntityMapper.selectById(id);
        return demoEntity == null ? null : BeanUtils.demoToDTO(demoEntity);
    }

    @Override
    public List<DemoDTO> queryDemoList(DemoDTO demoDTO) {
        DemoEntityQuery query = BeanUtils.dtoToDemoQuery(demoDTO);
        List<DemoDTO> collect = null;
        List<DemoEntity> list = demoEntityMapper.queryList(query);
        if (!EmptyUtil.isEmpty(list)) {
            collect = list.stream().map(BeanUtils::demoToDTO).collect(Collectors.toList());
        }
        return collect;
    }

    @Override
    public PageDTO<DemoDTO> queryDemoPage(DemoDTO demoDTO) {

        PageDTO<DemoDTO> collect = null;
        Pageable pageable = new PageRequest(demoDTO.getPageNo(), demoDTO.getPageSize());
        DemoEntityQuery query = BeanUtils.dtoToDemoQuery(demoDTO);
        Long count = demoEntityMapper.countQueryContion(query);
        if (count == 0) {
            collect = BeanUtils.toPageDTO(null);
        } else {
            query.setItemCount(count);
            List<DemoEntity> data = demoEntityMapper.queryList(query);
            List<DemoDTO> dtoDatas = data.stream().map(BeanUtils::demoToDTO).collect(Collectors.toList());
            collect = BeanUtils.toPageDTO(new PageImpl<>(dtoDatas, pageable, count));
        }
        return collect;
    }

}
