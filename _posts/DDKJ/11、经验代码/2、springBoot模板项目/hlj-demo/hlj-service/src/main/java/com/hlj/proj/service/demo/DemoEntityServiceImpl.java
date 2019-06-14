package com.hlj.proj.service.demo;

import com.hlj.proj.api.demo.DemoEntityService;
import com.hlj.proj.data.dao.db.demo.DemoEntityRepository;
import com.hlj.proj.data.dao.mybatis.demo.DemoEntityMapper;
import com.hlj.proj.data.pojo.demo.DemoEntity;
import com.hlj.proj.dto.Demo.DemoDTO;
import com.hlj.proj.utils.BeanUtils;
import com.hlj.proj.utils.EmptyUtil;
import lombok.extern.slf4j.Slf4j;
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
    public DemoDTO addDemoEntity(DemoDTO demoDTO) {
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.setName(demoDTO.getName());
        demoEntity.setAge(demoDTO.getAge());
        demoEntityRepository.save(demoEntity);
        demoDTO.setId(demoEntity.getId());
        return demoDTO;
    }

    @Override
    public DemoDTO findById(Long id) {
        DemoEntity demoEntity = demoEntityMapper.findById(id);
        return demoEntity == null ? null : BeanUtils.demoToDTO(demoEntity);
    }

    @Override
    public List<DemoDTO> findAll() {
        List<DemoDTO> collect = null;
        List<DemoEntity> list = demoEntityMapper.findAll();
        if (!EmptyUtil.isEmpty(list)) {
            collect = list.stream().map(BeanUtils::demoToDTO).collect(Collectors.toList());
        }
        return collect;
    }
}
