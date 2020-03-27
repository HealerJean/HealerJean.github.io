package com.healerjean.proj.service.impl;

import com.healerjean.proj.common.enums.StatusEnum;
import com.healerjean.proj.dao.mapper.DemoEntityMapper;
import com.healerjean.proj.dto.DemoDTO;
import com.healerjean.proj.pojo.DemoEntity;
import com.healerjean.proj.service.DemoEntityService;
import com.healerjean.proj.utils.BeanUtils;
import com.healerjean.proj.utils.EmptyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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



    @Override
    public DemoDTO insert(DemoDTO demoDTO) {
        DemoEntity demoEntity = BeanUtils.dtoToDemo(demoDTO);
        demoEntity.setStatus(StatusEnum.生效.code);
        demoEntityMapper.insert(demoEntity);
        demoDTO.setId(demoEntity.getId());
        return demoDTO;
    }


    @Override
    public DemoDTO findById(Long id) {
        DemoEntity demoEntity = demoEntityMapper.selectById(id);
        return demoEntity == null ? null : BeanUtils.demoToDTO(demoEntity);
    }

    @Override
    public List<DemoDTO> list() {
        List<DemoDTO> collect = null;
        List<DemoEntity> list = demoEntityMapper.selectList(null);
        if (!EmptyUtil.isEmpty(list)) {
            collect = list.stream().map(BeanUtils::demoToDTO).collect(Collectors.toList());
        }
        return collect;
    }

}
